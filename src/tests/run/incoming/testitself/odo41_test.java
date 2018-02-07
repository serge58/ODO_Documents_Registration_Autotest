package tests.run.incoming.testitself;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import static common.utilities.Tools.*;

import java.util.HashMap;

import org.testng.Reporter;

import com.codeborne.selenide.SelenideElement;

import tests.commons.pageobjects.Common;
import tests.run.incoming.pages.CorrespondentPage;
import tests.run.incoming.pages.IncomingMainPage;

public class odo41_test {
	static String worker;
	static String workerDep;
	
	static SelenideElement revokedDoc;
	static SelenideElement regCard;
	
	static String docNum;
	static String regDocNum;
	static String regDocNumDateTime;
	
	static String registrationDate;
	
	public static void FindingRevokedDoc() {
		Reporter.log("Запоминаем имя и подразделение работника<br>");
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
 		worker = userInfo.getText().split("\n")[0];
 		workerDep = userInfo.getText().split("\n")[1];
		
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		docNum = System.getProperty("docNum");
		regDocNum = System.getProperty("registeredDocNumber");
		
		Reporter.log("Ищем отозванный документ<br>");
		if (docNum == null & regDocNum == null) {
			revokedDoc = IncomingMainPage.FindRegisteredDoc("", "Отозван");
		} else {
			revokedDoc = IncomingMainPage.FindRegisteredDoc(regDocNum, "");
		}
		
		if (!revokedDoc.exists()) {
			revokedDoc = IncomingMainPage.FindRegisteredDoc("", "Зарегистрирован");
			IncomingMainPage.RecallDocument();
		}
		
		IncomingMainPage.TransferDocumentToDepartment($(byClassName("x-userinfo")).getText().split("\n")[1]);
		
		docNum = IncomingMainPage.GetCaseNumber();
		regDocNumDateTime = IncomingMainPage.GetRegDocNumDateTime();
	}
	
	public static void RestoreDocument(HashMap<String, String> regCardFields) {
		String attentionMessage = "Регистрация данного документа уже производилась за номером " + regDocNumDateTime + "\r\n" + 
								  "При продолжении регистрации существующая регистрационная карточка будет восстановлена.";
		
		Reporter.log("Нажимаем клавишу 'Зарегистрировать документ'<br>");
		SelenideElement cardListPanel = $$(byClassName("docflow-list")).find(visible);
		cardListPanel.$(byText("")).click();
		
		SelenideElement cardLinkWindow = $$(byClassName("x-window")).exclude(hidden).find(not(cssClass("x-toast")));
		cardLinkWindow.$(byName("regNumber")).sendKeys(docNum);
		cardLinkWindow.$(byText("Ok (Enter)")).click();
		
		Reporter.log("Проверяем сообщение о повторной регистрации<br>");
		SelenideElement attentionWindow = $(byClassName("x-message-box"));
		attentionWindow.$(byClassName("x-window-text")).shouldHave(text(attentionMessage));
		
		Reporter.log("Нажимаем кнопку Продолжить<br>");
		attentionWindow.$(byText("Продолжить")).click();
		
		Reporter.log("Заполняем поля карточки новыми значениями<br>");
		regCard = IncomingMainPage.FillRequiredDocFields(regCardFields, true);
		
		Reporter.log("Добавляем Корреспондента<br>");
		CorrespondentPage.AddCorrespondent(regCardFields.get("Корреспондент"), true);
		
		Reporter.log("Нажимаем кнопку 'Сохранить'<br>");
		regCard.$(byText("Сохранить")).click();
		registrationDate = getNowLongDate();
		
		Common.WaitingForUpdateAllWindows();
	}

	public static void CheckRestoredDocument() {
		IncomingMainPage.CheckHistoryProcessing(worker, workerDep, registrationDate);
	}
}
