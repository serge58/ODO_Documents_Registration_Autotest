package tests.run.incoming.testinit;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;

import common.listeners.TestListener;
import common.utilities.DriverTools;
import tests.commons.pageobjects.Common;
import tests.commons.testdata.dataproviders.ODODataProviders;
import tests.run.incoming.testitself.odo37_test;

import ru.yandex.qatools.allure.annotations.*;

@Listeners({ TestListener.class })

public class odo37_RegIncomingDoc {
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
	
	@TestCaseId("1")
	@Step("Авторизация пользователем {0}")
	@Title("Авторизация передающего пользователя")
	@Description("Авторизуемся пользователем, который будет передавать документ")
	@Test(description = "Авторизуемся пользователем, который будет передавать документ")
	@Parameters({ "sender", "password" })
	public void SenderAutorisation(String login, String password) {
		//Configuration.assertionMode = Configuration.AssertionMode.STRICT;
		Common.Login(login, password);
		$(byClassName("x-userinfo")).shouldBe(Condition.visible);
	}
	
	@TestCaseId("2")
	@Step("Принимаем к учёту новый документ в УПД")
	@Title("Новый документ УПД")
	@Description("Принимаем к учёту новый документ во вкладке УПД")
	@Test(description = "Принимаем к учёту новый документ", dataProvider = "document", dataProviderClass = ODODataProviders.class)
	public void AccountNewDocument(HashMap<String, String> docFields) {
		odo37_test.accountNewDocument(docFields);
	}
	
	@TestCaseId("3")
	@Step("Авторизация принимающим пользователем {0}")
	@Title("Авторизация принимающего пользователя")
	@Description("Авторизуемся пользователем, который будет регистрировать документ")
	@Test(description = "Авторизуемся пользователем, который будет регистрировать документ")
	@Parameters({ "acceptor", "password" , "acceptorName" })
	public void AcceptorAutorisation(String acceptor, String password, String acceptorName) {
		odo37_test.AcceptorAutorisation(acceptor, password, acceptorName);
	}
	
	@TestCaseId("4")
	@Step("Переходим на вкладку ОДО")
	@Title("Переход на вкладку ОДО")
	@Description("Переходим на вкладку ОДО и проверяем что вкладка 'Входящие' открыта по умолчанию, проверяем поля которые подтянулись из карточки документа")
	@Test(description = "Переходим на вкладку ОДО и проверяем что вкладка 'Входящие' открыта по умолчанию, проверяем поля которые подтянулись из карточки документа")
	public void EnterDocNumAndCheckDocFields() {
		odo37_test.EnterDocNumAndCheckDocFields();
	}
	
	@TestCaseId("5")
	@Step("Заполняем поля карточки")
	@Title("Заполнение полей карточки")
	@Description("Заполняем все поля карточки за исключением персоналии")
	@Test(description = "Заполняем все поля карточки за исключением персоналии", 
			  dataProvider = "regCard", dataProviderClass = ODODataProviders.class)
	public void FillIncomingDocFields(HashMap<String, String> regCardFields) {
		odo37_test.FillIncomingDocFields(regCardFields);
	}
	
	@TestCaseId("6")
	@Step("Добавляем корреспондента")
	@Title("Корреспондент")
	@Description("Добавляем корреспондента")
	@Test(description = "Добавляем корреспондента", dataProvider = "correspondentFields", dataProviderClass = ODODataProviders.class)
	public void AddCorrespondents(HashMap<String, String> correspondentFields) {
		odo37_test.AddCorrespondents(correspondentFields);
	}
	
	@TestCaseId("7")
	@Step("Проверяем параметры и сохраняем карточку")
	@Title("Параметры регистрационного номера")
	@Description("Проверяем параметры регистрационного номера")
	@Test(description = "Проверяем параметры регистрационного номера")
	public void СheckRegNumberParameters() {
		odo37_test.СheckRegNumberParameters();
	}
	
	@TestCaseId("8")
	@Step("Проверяем поля новой карточки")
	@Title("Проверка полей новой карточки")
	@Description("Проверяем все поля карточки после регистрации документа")
	@Test(description = "Проверяем все поля карточки после регистрации документа")
	public void CheckIncomingDocFields() {
		//Configuration.assertionMode = Configuration.AssertionMode.SOFT;
		//long savedTimeout = Configuration.timeout;
		//Configuration.timeout = 1000;
		
		odo37_test.CheckIncomingDocFields();
		
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
