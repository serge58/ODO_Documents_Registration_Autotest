package tests.run.incoming.testitself;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static tests.commons.pageobjects.Common.WaitForDocumentLoading;
import static tests.commons.pageobjects.UPD.GetDepartmentShortName;
import static tests.run.incoming.pages.IncomingMainPage.*;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import tests.run.incoming.pages.IncomingMainPage;

public class odo39_test {
	static SelenideElement documentButtons = $$(byClassName("x-box-target")).exclude(hidden).last();
	static SelenideElement registeredDoc;
	static String registeredDocNumber;
	static String recallDateTime;
	static String receiver;
	static String receiverDepShortName;
	final static String COMMENT = "Комментарий 12312уйвычсыые4564";
	
	public static void FindingRegisteredDoc() {
		Reporter.log("Запоминаем имя и подразделение принимающего работника<br>"); 
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
 		receiver = userInfo.getText().split("\n")[0];
 		receiverDepShortName = GetDepartmentShortName(userInfo.getText().split("\n")[1]);
 		
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Ищем зарегистрированный документ<br>");
		registeredDoc = FindRegisteredDoc("", "");
		registeredDoc.click();
		
		Reporter.log("Запоминаем номер<br>");
		registeredDocNumber = registeredDoc.$(byClassName("number")).getText();
		System.setProperty("registeredDocNumber", registeredDocNumber);
		
		Reporter.log("Передаём номер входящего документа для следующего теста<br>");
		System.setProperty("docNum", GetCaseNumber());
		
		WaitForDocumentLoading();
	}
	
	public static void RecallDocument() {
		recallDateTime = IncomingMainPage.RecallDocument();
	}

	public static void CheckDocumentRecalled() {
		Reporter.log("Нажимаем кнопку 'Обновить'<br>");
		SelenideElement cardsPanel = $$(byClassName("docflow-list")).find(visible);
		SelenideElement cardsPaging = cardsPanel.$(byClassName("x-toolbar-docked-bottom"));
		cardsPaging.$(byClassName("x-tbar-loading")).click();
		
		registeredDoc = FindRegisteredDoc(registeredDocNumber, "");
		registeredDoc.click();
		
		Reporter.log("Проверяем что 'Зарегистрирован' изменилось на 'Отозван'<br>");
		registeredDoc.$(byClassName("state")).shouldHave(text("Отозван"));
		
		Reporter.log("Проверяем Историю обработки<br>");
		ElementsCollection cardInfo = GetCardInfo();
		SelenideElement cardInfoHistory = cardInfo.find(text("История обработки"));
		ElementsCollection rows = cardInfoHistory.$$(By.tagName("table")).last().$$(By.tagName("td"));
		rows.get(0).shouldHave(text(recallDateTime));
		rows.get(1).shouldHave(text("Отозван"));
		rows.get(2).shouldHave(text(receiver + ", " + receiverDepShortName));
		rows.get(3).shouldHave(text(COMMENT));
	}
}
