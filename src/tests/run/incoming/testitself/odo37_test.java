package tests.run.incoming.testitself;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import common.utilities.Tools;
import tests.commons.pageobjects.ARM_MainPage;
import tests.commons.pageobjects.Common;
import tests.commons.pageobjects.UPD;
import tests.run.incoming.pages.CorrespondentPage;
import tests.run.incoming.pages.IncomingMainPage;

public class odo37_test {
	static String lastCaseNum;
	
	static String sender;
	static String senderDep;
	static String receiver;
	static String receiverDep;
	static String receiverDepShortName;
	
	static HashMap<String, String> docFieldsSaved;
	static HashMap<String, String> regCardFieldsSaved;
	
	static SelenideElement regCard;
	
	static List<String> oldCardParams;
	
	static String registrationDateTime;
	
	static List<String> accauntCardInfo;
	
	static List<String> personData;
	
	static ARM_MainPage armMainPage = new ARM_MainPage();
	
	public static void accountNewDocument(HashMap<String, String> docFields) {
		docFieldsSaved = docFields;
		
		Reporter.log("Запоминаем имя и подразделение работника<br>");
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
 		sender = userInfo.getText().split("\n")[0];
 		senderDep = userInfo.getText().split("\n")[1];
 		
 		armMainPage.OpenUPD();
 		
		$(byClassName("x-panel")).$$(byClassName("x-item")).findBy(text("Учёт новых документов")).click();
		
		String department = docFields.get("Департамент");
		
		Reporter.log("Выбираем подразделение<br>");
		UPD.DepartmentSelect(department);
		
		Reporter.log("Принимаем к учету документ<br>");
		UPD.CreateEditDocument(docFields, true);
 		
		Reporter.log("Запоминаем номер дела, для дальнейшей проверки карточки<br>");
		List<String> newListDoc = $(byClassName("case-list")).$(By.tagName("table")).$$(By.tagName("tr")).texts();
		lastCaseNum = newListDoc.get(0).split(" ")[0];
		receiverDep = newListDoc.get(0).split(" ")[1];
		
		Reporter.log("Ищем последний созданный документ<br>");
		SelenideElement currDoc = UPD.FindingDocumentByNumber(lastCaseNum);
		
		System.setProperty("lastCaseNum", lastCaseNum);
		
		Reporter.log("Открываем карточку документа<br>");
		currDoc.doubleClick();
		
		Reporter.log("Запоминаем информацию об учётной карточке<br>");
		ElementsCollection card = $$(byClassName("x-autocontainer-innerCt")).find(visible).$$(byClassName("x-panel")).filter(visible);
		ElementsCollection transmission = card.findBy(text("ЖУРНАЛ ПЕРЕДАЧ")).$$(By.tagName("tr")).get(0).$$(By.tagName("td"));
		accauntCardInfo = transmission.texts();
		
	}
	
	public static void AcceptorAutorisation(String acceptor, String password, String acceptorName) {
		Common.Relogin(acceptor, password, acceptorName);
		
		Reporter.log("Запоминаем имя и подразделение принимающего работника<br>"); 
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
 		receiver = userInfo.getText().split("\n")[0];
 		receiverDep = userInfo.getText().split("\n")[1];
 		
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Common.WaitingForUpdateAllWindows();

		Reporter.log("Запоминаем старый список карточек для того, чтобы вычислить номер новой карточки<br>");
		oldCardParams = $$(byClassName("docflow-list")).find(visible).$$(byClassName("x-dataview-item")).texts();
	}
	
	public static void EnterDocNumAndCheckDocFields() {
		Reporter.log("Проверяем что вкладка 'Входящие' выбрана по умолчанию<br>");
		ElementsCollection verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
		verticalTab.find(text("Входящие")).shouldHave(Condition.cssClass("x-item-selected"));
		
		Reporter.log("Нажимаем клавишу 'Зарегистрировать документ'<br>");
		SelenideElement cardListPanel = $$(byClassName("docflow-list")).find(visible);
		cardListPanel.$(byText("")).click();
		
		Reporter.log("Проверяем что открылось окно ввода номера учётной карточки'<br>");
		SelenideElement cardLinkWindow = $$(byClassName("x-window")).exclude(hidden).find(not(cssClass("x-toast")));
		cardLinkWindow.$(byClassName("x-title")).shouldHave(text("Связь с учетной карточкой входящего документа"));
		cardLinkWindow.$(By.tagName("label")).shouldHave(text("Введите номер документа, расположенный в левом нижнем углу " + 
															  "этикетки, размещенной на документе, или произведите " + 
															  "сканирование штрих-кода, расположенного на этикетке"));
		cardLinkWindow.$(byName("regNumber")).sendKeys(lastCaseNum);
		cardLinkWindow.$(byText("Ok (Enter)")).click();
		
		Reporter.log("Проверяем предварительно заполненные поля карточки<br>");
		regCard = $$(byClassName("x-window")).find(text("Регистрационная карточка входящего документа"));
		
		Reporter.log("Получаем короткие названия подразделений работников<br>");
		String senderDepShortName = UPD.GetDepartmentShortName(senderDep);
		receiverDepShortName = UPD.GetDepartmentShortName(receiverDep);
		
		IncomingMainPage.CheckPrefilledDocFields(regCard, docFieldsSaved, lastCaseNum, sender, senderDep, receiver, receiverDep, senderDepShortName, receiverDepShortName);
	}
	
	public static void FillIncomingDocFields(HashMap<String, String> regCardFields) {
		regCardFieldsSaved = regCardFields;
		
		IncomingMainPage.FillDocFields(regCardFieldsSaved, false);
	}
	
	public static void AddCorrespondents(HashMap<String, String> correspondentFields) {
		personData = CorrespondentPage.AddCorrespondent(correspondentFields);
		
		Reporter.log("Нажимаем кнопку 'Вперёд >>'<br>");
		regCard.$(byText("Вперёд >>")).click();
	}
	
	public static void СheckRegNumberParameters() {
		SelenideElement regNumParamsWindow = $$(byClassName("x-window")).find(Condition.text("Параметры регистрационного номера"));
		regNumParamsWindow.shouldHave(text("Уточните параметры регистрационного номера"));

		regNumParamsWindow.$$(byClassName("x-field")).find(text("Номер наряда:")).shouldBe(visible);
		regNumParamsWindow.$$(byName("order")).find(visible).shouldHave(value(regCardFieldsSaved.get("Номер наряда")));
		
		SelenideElement dspCheckBox = regNumParamsWindow.$$(byClassName("x-form-type-checkbox")).find(visible);
		dspCheckBox.shouldHave(text("Для служебного пользования"));
		Common.CheckSelected(dspCheckBox, true);
		
		regNumParamsWindow.$(byText("Зарегистрировать")).click();
		registrationDateTime = Tools.getNowDate();
	}
	
	public static void CheckIncomingDocFields() {
		Common.WaitForDocumentLoading();
		
		IncomingMainPage.CheckFinalDocFields(oldCardParams, lastCaseNum, registrationDateTime, accauntCardInfo, receiver, receiverDep, receiverDepShortName, personData);
	}
}
