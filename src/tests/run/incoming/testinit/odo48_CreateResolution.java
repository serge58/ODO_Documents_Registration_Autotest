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
import tests.run.incoming.testitself.odo48_test;

@Listeners({ TestListener.class })

public class odo48_CreateResolution {
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
	
	@Test(description = "Авторизуемся пользователем, который будет добавлять резолюцию")
	@Parameters({ "acceptor", "password" })
	public void AcceptorAutorisation(String login, String password) {
		//Configuration.assertionMode = Configuration.AssertionMode.STRICT;
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@Test(description = "Находим зарегистрированный документ")
	public void FindingRegisteredDoc() {
		odo48_test.FindingRegisteredDoc();
	}
	
	@Test(description = "Создаём резолюцию", dataProvider = "resolutionFields", dataProviderClass = ODODataProviders.class)
	public void AddingResolution(HashMap<String, String> resolutionFields) {
		odo48_test.AddingResolution(resolutionFields);
	}
	
	@Test(description = "Проверяем поля созданной резолюции")
	public void CheckAddedResolution() {
		//Configuration.assertionMode = Configuration.AssertionMode.SOFT;
		//long savedTimeout = Configuration.timeout;
		//Configuration.timeout = 1000;
		
		odo48_test.CheckResolutionFields();
		
		//Configuration.assertionMode = Configuration.AssertionMode.STRICT;
		//Configuration.timeout = savedTimeout;
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
