package common.utilities;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

public class Tools {
	public static void pressESC() { // Необходимо чтобы уйти с нативного окна
									// (Печать загрузка и т.д.)
		Robot robot;
		int keyESC = KeyEvent.VK_ESCAPE;

		try {
			robot = new Robot();
			robot.keyPress(keyESC);
			robot.keyRelease(keyESC);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void pressKey(int keyCode) {
		Robot robot;
		
		try {
			robot = new Robot();
			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public static String getShortDate(String date) {
		DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

		LocalDate outDate = LocalDate.parse(date, inFormatter);

		return outDate.format(outFormatter);
	}

	public static String getLongDate(String shortDate) {
		DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
		DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		LocalDate outDate = LocalDate.parse(shortDate, inFormatter);

		return outDate.format(outFormatter);
	}
	
	public static String getShortDateTime(String date) {
		DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");

		LocalDateTime outDate = LocalDateTime.parse(date, inFormatter);

		return outDate.format(outFormatter);
	}
	
	public static String getLongDateTime(String date) {
		DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
		DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

		LocalDateTime outDate = LocalDateTime.parse(date, inFormatter);

		return outDate.format(outFormatter);
	}
	
	public static String getNowDate() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yy"));
	}

	public static String getNowLongDate() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	public static String getNowLongDateTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
	}
	
	public static String getNowShortDateTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
	}
	
	public static String getNowLongDateWithoutLeadNull() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("d.MM.yyyy"));
	}
	
	public static String getTomorrowDate() {
		return LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	public static String getNextWorkDate(boolean shortDate) {
		DateTimeFormatter outFormatter;
		LocalDate nextWork = LocalDate.now();
		DayOfWeek dayOfWeek = nextWork.getDayOfWeek();
		int dayPlus = 1;

		switch (dayOfWeek.name()) {
		case "FRIDAY":
			dayPlus = 3;
			break;
		case "SATURDAY":
			dayPlus = 2;
			break;
		}

		if (shortDate) 
			outFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
		else
			outFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		return nextWork.plusDays(dayPlus).format(outFormatter);
	}

	public static String getPreviousWorkDate(boolean shortDate) {
		DateTimeFormatter outFormatter;
		LocalDate previousWork = LocalDate.now();
		DayOfWeek dayOfWeek = previousWork.getDayOfWeek();
		int dayMinus = 1;

		switch (dayOfWeek.name()) {
		case "MONDAY":
			dayMinus = 3;
			break;
		case "SUNDAY":
			dayMinus = 2;
			break;
		}

		if (shortDate)
			outFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
		else
			outFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		return previousWork.minusDays(dayMinus).format(outFormatter);
	}
	
	public static String getRandomLongDate(String fromDate, String toDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		if (fromDate.isEmpty()) {
			LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
			fromDate = startOfYear.format(formatter);
		}
		
		if (toDate.isEmpty()) 
			toDate = LocalDate.now().format(formatter);
		
		
		LocalDate from = LocalDate.parse(fromDate, formatter);
		LocalDate to = LocalDate.parse(toDate, formatter);
		
		long randomEpochDay = from.toEpochDay() + Tools.getRandomLong(to.toEpochDay() - from.toEpochDay());
		
		return LocalDate.ofEpochDay(randomEpochDay).format(formatter);
	}
	
	public static String getRandomLongDate() {
		String fromDate;
		String toDate;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		fromDate = startOfYear.format(formatter);
		toDate = LocalDate.now().format(formatter);
		
		LocalDate from = LocalDate.parse(fromDate, formatter);
		LocalDate to = LocalDate.parse(toDate, formatter);
		
		long randomEpochDay = from.toEpochDay() + Tools.getRandomLong(to.toEpochDay() - from.toEpochDay());
		
		return LocalDate.ofEpochDay(randomEpochDay).format(formatter);
	}
	
	public static String getShortTomorrowDate() {
		return LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yy"));
	}

	public static String getShortYesterdeyDate() {
		return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yy"));
	}

	public static String getYesterdayDate() {
		return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	public static String getTimestamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}
	
	public static String getTime(long minus) {
		String[] time = LocalTime.now().minusHours(minus).format(DateTimeFormatter.ISO_TIME).split(":");
		String out = time[0] + ":" + time[1];
		
		return out;
	}
	
	public static String getShortYear() {
		return Integer.toString(LocalDateTime.now().getYear()).substring(2);
	}
	
	public static int getRandomInt(int number) {
		return (int) (Math.random() * number);
	}
	
	public static long getRandomLong(long number) {
		return (long) (Math.random() * number);
	}
	
	public static String generateRandomSymbolsEN(int number) {
		String symbols = "";
		Random rand = new Random();
		                       
		String[] stringArray = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		
		for (int i = 0; i < number; i++) {
			symbols += stringArray[rand.nextInt(stringArray.length - 1)];
		}
		
		return symbols;
	}
	
	public static String generateRandomSymbolsRU(int number) {
		String symbols = "";
		Random rand = new Random();
		
		String[] stringArray = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я"};
		
		for (int i = 0; i < number; i++) {
			symbols += stringArray[rand.nextInt(stringArray.length - 1)];
		}
		
		return symbols;
	}

	public static String getRandomFirstMiddleName() {
		String symbols = "";
		Random rand = new Random();
		
		String[] stringArray = new String[] {"А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Э", "Ю", "Я"};
		
		symbols = stringArray[rand.nextInt(stringArray.length - 1)] + stringArray[rand.nextInt(stringArray.length - 1)].toLowerCase() + ".";
		symbols += stringArray[rand.nextInt(stringArray.length - 1)] + stringArray[rand.nextInt(stringArray.length - 1)].toLowerCase() + ".";
				
		return symbols;
	}
	
	public static String generateRandomTabNumber() {
		String symbols = "";
		Random rand = new Random();
		
		symbols = "M" + Integer.toString(rand.nextInt(999999));
		
		return symbols;
	}
	
	public static SelenideElement getXpath(SelenideElement input) {
        return $(byXpath(generateXPATH(input, "")));
    }
	
	private static String generateXPATH(WebElement childElement, String current) {
		String childTag = childElement.getTagName();
		
		if (childTag.equals("html")) {
			return "/html[1]" + current;
		}
		
		WebElement parentElement = childElement.findElement(By.xpath(".."));
		
		List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
		
		int count = 0;
		
		for (int i = 0; i < childrenElements.size(); i++) {
			WebElement childrenElement = childrenElements.get(i);
			
			String childrenElementTag = childrenElement.getTagName();
			
			if (childTag.equals(childrenElementTag)) {
				count++;
			}
			
			if (childElement.equals(childrenElement)) {
				return generateXPATH(parentElement, "/" + childTag + "[" + count + "]" + current);
			}
		}
		
		return null;
	}
	
	public static void deleteFiles() {
		File dir = new File(System.getenv("USERPROFILE") + "\\Downloads\\");
		
		File[] listFiles = dir.listFiles(new FileNameFilter("docx"));

		if (listFiles.length > 0){
			for(File f : listFiles)
                f.delete();
        }
	}
	
	public static class FileNameFilter implements FilenameFilter{
        
        private String ext;
         
        public FileNameFilter(String ext){
            this.ext = ext.toLowerCase();
        }
        
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(ext);
        }
    }
	
	public static String getDocxContext(String docxFile) {
		StringWriter writer = new StringWriter();
		
		WordprocessingMLPackage wordMLPackage;
		try {
			wordMLPackage = WordprocessingMLPackage.load(new File(docxFile));
			MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
	        TextUtils.extractText(documentPart.getContents(), writer);

		} catch (Exception e) {
			e.printStackTrace();
		}		
        
        return writer.toString();
	}
	
	
    //загружает файл на элемент сенчи
    public static void UploadFile(SelenideElement uploadButton, String relativePath) {
    	WebDriver driver = WebDriverRunner.getWebDriver();
    			
        File uploadFile = new File(relativePath);

        // disable the click event on an `<input>` file
        ((JavascriptExecutor) driver).executeScript("HTMLInputElement.prototype.click = function() { if(this.type !== 'file') HTMLElement.prototype.click.call(this);}; ");

        uploadButton.click();

        // assign the file to the `<input>`
        driver.findElement(By.cssSelector("input[type=file]")).sendKeys(uploadFile.getAbsolutePath());

    }
    
  //загружает файл на элемент сенчи
    public static void UploadFile(SelenideElement uploadButton, SelenideElement input, String relativePath) {
    	WebDriver driver = WebDriverRunner.getWebDriver();
    			
        File uploadFile = new File(relativePath);

        // disable the click event on an `<input>` file
        ((JavascriptExecutor) driver).executeScript("HTMLInputElement.prototype.click = function() { if(this.type !== 'file') HTMLElement.prototype.click.call(this);}; ");

        uploadButton.click();

        // assign the file to the `<input>`
        input.sendKeys(uploadFile.getAbsolutePath());

    }
}
