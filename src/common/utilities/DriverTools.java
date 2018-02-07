package common.utilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.OperaDriverManager;

import common.listeners.HighlightElements;

public class DriverTools {
	public static WebDriver createDriver(String baseUrl, String holdBrowser, String browser, String reportsFolder, String reportsUrl, String timeout, String highlightElements) {
		WebDriver driver = null; 
		
		boolean highlight = highlightElements.contentEquals("true") ? true : false;
		
		Configuration.holdBrowserOpen = holdBrowser.contentEquals("true") ? true : false;
		Configuration.timeout = Long.parseLong(timeout);
		Configuration.pageLoadStrategy = "normal"; 	// Possible values: "none", "normal" and "eager".
		Configuration.savePageSource = false;
		Configuration.screenshots = true;
		
		if (!reportsFolder.isEmpty())
			Configuration.reportsFolder = reportsFolder;

		if (!reportsUrl.isEmpty())
			Configuration.reportsUrl = reportsUrl;

		if (browser.contains("firefox")) {
			//System.setProperty("webdriver.gecko.driver", "C:/Autotest_Projects/Drivers/geckodriver.exe");

			FirefoxDriverManager.getInstance().setup();
			//ProfilesIni iniProfile = new ProfilesIni();
			//FirefoxProfile profile = iniProfile.getProfile(autotestProfile);
			
			/*FirefoxProfile profile = new FirefoxProfile();
			profile.setAssumeUntrustedCertificateIssuer(false);
			profile.setAcceptUntrustedCertificates(true);

			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			capabilities.setCapability("marionette", true);*/
			
			driver = new FirefoxDriver();
			
		} else if (browser.contains("chrome")) {
			//System.setProperty("webdriver.chrome.driver", "C:/Autotest_Projects/Drivers/chromedriver.exe");
			
			ChromeDriverManager.getInstance().setup();
			
			//DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			//String[] switches = {"user-data-dir = C:/Autotest_Projects/Profiles/ChromeProfile"};
			//String[] switches = {"select_file_dialogs.allowed = false"};
			//capabilities.setCapability("chrome.switches", switches);
			//capabilities.setCapability("select_file_dialogs.allowed", false);
			
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			Map<String, Object> prefs = new HashMap<>();
			Map<String, Object> content_setting = new HashMap <>();

			content_setting.put("multiple-automatic-downloads", 1);
			prefs.put("download.prompt_for_download", "false");
			prefs.put("select_file_dialogs.allowed", "false");
			prefs.put("profile.default_content_settings", content_setting);
			prefs.put("profile.default_content_settings.popups", 0);
			//prefs.put("download.default_directory", new File(".").getAbsolutePath() + "\\download");

			options.setExperimentalOption("prefs", prefs);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			//--------------------------------------------------------

			driver = new ChromeDriver(options);
			
			//driver = new ChromeDriver(capabilities);
						
		} else if (browser.contains("ie")) {
			//System.setProperty("webdriver.ie.driver", "C:/Autotest_Projects/Drivers/IEDriverServer.exe");

			InternetExplorerDriverManager.getInstance().setup();
			driver = new InternetExplorerDriver();
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else if (browser.contains("opera")) {
			//System.setProperty("webdriver.opera.driver", "C:/Autotest_Projects/Drivers/operadriver.exe");

			OperaDriverManager.getInstance().setup();
			driver = new OperaDriver();
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} /*else if (browser.contains("phantomjs")) {
			System.setProperty("phantomjs.binary.path", "C:/Autotest_Projects/Drivers/phantomjs.exe");

			//PhantomJsDriverManager.getInstance().setup();
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("takesScreenshot", true);
			capabilities.setCapability("phantomjs.cli.args", new String[] {"--ignore-ssl-errors=true"});
			driver = new PhantomJSDriver();
		}*/
		
		if (highlight) {
			EventFiringWebDriver highlightDriver = new EventFiringWebDriver(driver);  
			highlightDriver.register(new HighlightElements());
			
			WebDriverRunner.setWebDriver(highlightDriver);
			highlightDriver.manage().window().maximize();
			return highlightDriver;
		} else {
			WebDriverRunner.setWebDriver(driver);
			
			driver.manage().window().setPosition(new Point(-1440, 87));
			driver.manage().window().maximize();
			
			return driver;
		}
	}
}
