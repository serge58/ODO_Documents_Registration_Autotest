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
import tests.commons.pageobjects.Common;
import tests.commons.testdata.dataproviders.ODODataProviders;
import tests.run.incoming.testitself.odo47_test;

@Listeners({ TestListener.class })

public class odo47_EditingDoc {
	
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
	
	@Test(description = "Авторизуемся пользователем, который будет редактировать документ")
	@Parameters({ "acceptor", "password" })
	public void AcceptorAutorisation(String login, String password) {
		//Configuration.assertionMode = Configuration.AssertionMode.STRICT;
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@Test(description = "Находим отозванный документ и проверяем что кнопка 'Редактировать' недоступна")
	public void CheckRevokedDocNotEdited() {
		odo47_test.CheckRevokedDocNotEdited();
	}
	
	@Test(description = "Находим зарегистрированный документ")
	public void FindingRegisteredDoc() {
		odo47_test.FindingRegisteredDoc();
	}
	
	@Test(description = "Редактируем документ с новыми значениями", dataProvider = "regCard", dataProviderClass = ODODataProviders.class)
	public void EditDocument(HashMap<String, String> regCardFields) {
		odo47_test.EditDocument(regCardFields);
	}
	
	@Test(description = "Проверяем новые значения полей отредактированного документа")
	public void CheckEditedFields() {
		//Configuration.assertionMode = Configuration.AssertionMode.SOFT;
		//long savedTimeout = Configuration.timeout;
		//Configuration.timeout = 1000;
		
		odo47_test.CheckEditedFields();
		
		//Configuration.assertionMode = Configuration.AssertionMode.STRICT;
		//Configuration.timeout = savedTimeout;
	}
	
	@Test(description = "Проверяем новые значения полей отредактированного документа")
	public void CheckEditedRegCard() {
		odo47_test.CheckEditedRegCard();
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
