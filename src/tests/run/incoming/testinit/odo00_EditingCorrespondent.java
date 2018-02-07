package tests.run.incoming.testinit;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

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
import tests.commons.pageobjects.Common;
import tests.run.incoming.testitself.odo00_test;

@Listeners({ TestListener.class })

public class odo00_EditingCorrespondent {
	
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
	
	@Test(description = "Авторизуемся пользователем, который будет редактировать корреспондента")
	@Parameters({ "acceptor", "password" })
	public void Autorisation(String login, String password) {
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@Test(description = "Находим зарегистрированный документ", dependsOnMethods = { "Autorisation" })
	public void FindingRegisteredDoc() {
		odo00_test.FindingRegisteredDoc();
	}
	
	@Test(description = "Редактируем корреспондента", dependsOnMethods = { "FindingRegisteredDoc" })
	public void EditCorrespondent() {
		odo00_test.EditCorrespondent();
	}
	
	@Test(description = "Проверяем новые значения полей отредактированного документа", dependsOnMethods = { "EditCorrespondent" })
	public void CheckEditedCorrespondentFields() {
		odo00_test.CheckEditedCorrespondent();

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
