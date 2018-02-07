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
import tests.run.incoming.testitself.odo41_test;

@Listeners({ TestListener.class })

public class odo41_RestorationRevokedDoc {
	
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
	
	@Test(description = "Авторизуемся пользователем, который будет восстанавливать документ")
	@Parameters({ "acceptor", "password" })
	public void AcceptorAutorisation(String login, String password) {
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@Test(description = "Находим отозванный документ", dependsOnMethods = { "AcceptorAutorisation" })
	public void FindingRevokedDoc() {
		odo41_test.FindingRevokedDoc();
	}
	
	@Test(description = "Восстанавливаем документ", dataProvider = "regCard", dataProviderClass = ODODataProviders.class, dependsOnMethods = { "FindingRevokedDoc" })
	public void RestoreDocument(HashMap<String, String> regCardFields) {
		odo41_test.RestoreDocument(regCardFields);
	}
	
	@Test(description = "Проверяем что документ восстановлен", dependsOnMethods = { "RestoreDocument" })
	public void CheckRestoredDocument() {
		odo41_test.CheckRestoredDocument();
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
