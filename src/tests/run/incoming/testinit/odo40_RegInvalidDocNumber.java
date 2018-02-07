package tests.run.incoming.testinit;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;

import common.listeners.TestListener;
import common.utilities.DriverTools;
import ru.yandex.qatools.allure.annotations.Issue;
import tests.commons.pageobjects.Common;
import tests.commons.testdata.dataproviders.ODODataProviders;
import tests.run.incoming.testitself.odo40_test;

@Listeners({ TestListener.class })

public class odo40_RegInvalidDocNumber {
	static WebDriver driver;
	
	@Parameters({ "baseUrl", "holdBrowser", "browser", "reportsFolder", "reportsUrl", "timeout", "highlightElements" })
	@BeforeClass
	public static void setUp(String baseUrl, String holdBrowser, String browser, String reportsFolder, String reportsUrl, String timeout, String highlightElements) {
		String baseURL = System.getProperty("baseURL");

		if (baseURL == null)
			baseURL = baseUrl;
		
		driver = DriverTools.createDriver(baseURL, holdBrowser, browser, reportsFolder, reportsUrl, timeout, highlightElements);
		open(baseURL);
	}
	
	@Test(description = "Авторизуемся пользователем, который будет передавать документ")
	@Parameters({ "acceptor", "password" })
	public void Autorisation(String login, String password) {
		//Configuration.assertionMode = Configuration.AssertionMode.STRICT;
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@Test(description = "Принимаем к учёту новый документ", dataProvider = "document", dataProviderClass = ODODataProviders.class)
	public void AccountNewDocument(HashMap<String, String> docFields) {
		odo40_test.accountNewDocument(docFields);
	}

	@Issue("8739")
	@Test(description = "Попытка зарегистрировать входящий по УНКпо которому уже зарегистрирован входящий")
	public void NumDocAlreadyRegistered() {
		odo40_test.numDocAlreadyRegistered();
	}
	
	@Issue("8739")
	@Test(description = "Попытка зарегистрировать входящий по УНК который не передавался в наше подразделение")
	public void NumDocInAnotherDepartment() {
		odo40_test.numDocInAnotherDepartment();
	}
	
	@Test(description = "Попытка зарегистрировать входящий входящий по копии")
	public void NumDocCopy() {
		odo40_test.numDocCopy();
	}

	@Test(description = "Попытка зарегистрировать входящий по несуществующему УНК")
	public void NumDocNonexistent() {
		odo40_test.numDocNonexistent();
	}
	
	@Parameters({ "holdBrowser" })
	@AfterClass(description = "Закрываем драйвер")
	public static void tearDown(String holdBrowser) {
		WebDriver driver = WebDriverRunner.getWebDriver();
		
		if (driver != null) {
			if (!holdBrowser.contentEquals("true") ? true : false)
				driver.quit();
		}
	}
}