package tests.run.outgoing.testinit;

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
import tests.run.outgoing.testitself.odo54_test;

@Listeners({ TestListener.class })

public class odo54_RegisterDocDraft {
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
	@Parameters({ "sender", "password" })
	public void Autorisation(String login, String password) {
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@Test(description = "Принимаем к учёту новый документ", dataProvider = "outgoingDocDraft", dataProviderClass = ODODataProviders.class,
		  dependsOnMethods = { "Autorisation" })
	public void CreateDocDraft(HashMap<String, String> docDraftFields) {
		odo54_test.CreateDocDraft(docDraftFields);
	}
	
	@Test(description = "Проверяем все поля карточки после регистрации документа", dependsOnMethods = { "CreateDocDraft" })
	public void RegisterDocDraft() {
		odo54_test.RegisterDocDraft();
	}
	
	@Test(description = "Проверяем все поля карточки после регистрации документа", dependsOnMethods = { "RegisterDocDraft" })
	public void CheckRegisteredDocDraft() {
		odo54_test.CheckRegisteredDocDraft();
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