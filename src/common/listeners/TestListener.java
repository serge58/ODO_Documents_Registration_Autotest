package common.listeners;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import common.utilities.VideoRecorder;
import ru.yandex.qatools.allure.annotations.Attachment;

public class TestListener implements ITestListener{
	static String screenshotURL;
	static String reportsFolder;
	
	static boolean recordVideo;
	static boolean recordVideoAlways;
	
	static String videoFolder;
	static String videoURL;
	
	static Logger logger;
	
	VideoRecorder recorder;	
	
	@Attachment(value = "{0}", type = "text/plain")
	public static String saveTextLog(String attachName, String message) {
	    return message;
	}
	
	@Attachment(value = "{0}", type = "image/png")
	public static byte[] saveImageAttach(String attachName, String attachFileName) {
	    try {
	        File imageFile = new File(System.getProperty("user.dir") + attachFileName);
	        return toByteArray(imageFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return new byte[0];
	}

	@Attachment(value = "{0}", type = "text/html")
	public static byte[] saveHtmlAttach(String attachName, String attachFileName) {
	    try {
	    	File imageFile = new File(System.getProperty("user.dir") + attachFileName);
	        return toByteArray(imageFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return new byte[0];
	}
	
	private static byte[] toByteArray(File file) throws IOException {
	    return Files.readAllBytes(Paths.get(file.getPath()));
	}
	
    @Override
    public void onFinish(ITestContext context) {

    } 
    
	@Override
	public void onStart(ITestContext arg0) {
		String testClass = arg0.getCurrentXmlTest().getClasses().get(0).getName();
		testClass = testClass.substring(testClass.lastIndexOf(".") + 1);
		
		logger = Logger.getLogger(testClass);
		logger.info("Начало теста: " + arg0.getName());
		
		screenshotURL = arg0.getCurrentXmlTest().getParameter("reportsUrl");
		
		File screenshotFolder = new File(arg0.getCurrentXmlTest().getParameter("reportsFolder"));
		if (!screenshotFolder.exists()) 
			screenshotFolder.mkdirs();
		
		recordVideoAlways = arg0.getCurrentXmlTest().getParameter("recordVideoAlways").contains("true") ? true : false;
		recordVideo = arg0.getCurrentXmlTest().getParameter("recordVideo").contains("true") ? true : false;
		
		videoFolder = arg0.getCurrentXmlTest().getParameter("videoFolder");
		videoURL = arg0.getCurrentXmlTest().getParameter("videoUrl");
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		logger.error("Тест: " + arg0.getName() + " завершился с ошибкой!");
		
		String screenshot = takeScreenshotPath(arg0.getThrowable().toString(), arg0.getName());

		String buildURL = System.getenv("BUILD_URL");
		
		String artifactURL = buildURL + "artifact" + screenshotURL + screenshot;
		
		if (!screenshot.isEmpty()) {
			Reporter.log("<br><a href='" + artifactURL + "' target='_blank'><img src=" + artifactURL + " style=\"width:10%; border:2px solid red;\"></a>");
			saveImageAttach("Снимок экрана", screenshotURL + screenshot);
		}
		
		if (recordVideo) {
			String videoName = recorder.stopRecording(arg0.getName());
	    	Reporter.log("<br><a href='" + videoURL + videoName + "' target='_blank'><img src='" + screenshotURL + "/pictures/video.png' width='50' height='50' alt='Video'></a>");
		}
		
		SelenideElement errorWindow = $(byClassName("x-message-box"));
		if (errorWindow.exists())
			errorWindow.$(byText("OK")).click();
		
		SelenideElement cardLinkWindow = $$(byClassName("x-window")).exclude(hidden).find(not(cssClass("x-toast")));
		if (cardLinkWindow.exists())
			cardLinkWindow.$(byText("Закрыть (Esc)")).click();
		
		//*************************************************
		/*logger.error(String.format("FAIL %s.%s", arg0.getTestClass().getName(), arg0.getMethod().getMethodName()));
	    
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    Throwable cause = arg0.getThrowable();
	    
	    if (null != cause) {
	        cause.printStackTrace(pw);
	        logger.error(sw.getBuffer().toString());
	    }*/
	    //*************************************************
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		if (recordVideo) {
			recorder = new VideoRecorder(videoFolder);
			recorder.startRecording(WebDriverRunner.getWebDriver());
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		if (recordVideo & recordVideoAlways) {
			String videoName = recorder.stopRecording(arg0.getName());
	    	Reporter.log("<br><a href='" + videoURL + videoName + "' target='_blank'><img src='" + screenshotURL + "/pictures/video.png' width='50' height='50' alt='Video'></a>");
		} else if (recordVideo & !recordVideoAlways)
			recorder.stopRecording(null);
	}
	
	private String takeScreenshotPath(String buff, String className) {
		String ret = "";

		if (buff.contains("Screenshot")) {
			String[] temp = buff.split("\n");
			for (String str: temp) {
				if (str.contains("Screenshot")) {
					String[] ru = str.split(" ");
					if (ru[1] != null) {
						ret = ru[1].substring(ru[1].lastIndexOf("/") + 1);
					}
					break;
				}
			}
		}
		
		return ret;
	}
 }