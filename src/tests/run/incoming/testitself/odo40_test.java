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
import static tests.commons.pageobjects.Common.SelectDepartmentFromWindowTreeList;
import static tests.commons.pageobjects.UPD.CreateEditDocument;
import static tests.commons.pageobjects.UPD.DepartmentSelect;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import tests.run.incoming.pages.IncomingMainPage;

public class odo40_test {
	
	static String caseNumAnatherDep;
	static String department;
	
	static SelenideElement cardLinkWindow = $$(byClassName("x-window")).exclude(hidden).find(not(cssClass("x-toast")));
	static SelenideElement regCardButton = $$(byClassName("docflow-list")).find(visible).$(byText(""));
	static SelenideElement errorWindow = $(byClassName("x-message-box"));
	
	public static void accountNewDocument(HashMap<String, String> docFields) {
		Reporter.log("Открываем вкладку УПД<br>");
		$(byText("УПД")).click();
		
		$(byClassName("x-panel")).$$(byClassName("x-item")).findBy(text("Учёт новых документов")).click();
		
		department = docFields.get("Департамент");
		
		Reporter.log("Выбираем подразделение<br>");
		DepartmentSelect(department);
		
		Reporter.log("Принимаем к учету документ<br>");
		CreateEditDocument(docFields, false);
 		
		Reporter.log("Запоминаем номер дела, для дальнейшей проверки карточки<br>");
		List<String> newListDoc = $(byClassName("case-list")).$(By.tagName("table")).$$(By.tagName("tr")).texts();
		caseNumAnatherDep = newListDoc.get(0).split(" ")[0];
	}
	
	public static void numDocAlreadyRegistered() {
		Reporter.log("Запоминаем имя и подразделение работника<br>");
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
 		String workerDep = userInfo.getText().split("\n")[1];

		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();

 		Reporter.log("Находим уже зарегистрированный документ<br>");
 		IncomingMainPage.FindRegisteredDoc("", "Зарегистрирован");
 		
		String cardNumber = IncomingMainPage.GetRegDocNumDate();
 		String lastCaseNum = IncomingMainPage.GetCaseNumber();
 		
		String errMessage = "Перед регистрацией документа необходимо, "
				+ "чтобы ранее зарегистрированный документ по данной учетной карточке был отозван с регистрации. "
				+ "На данный момент входящий документ зарегистрирован в подразделении: "
				+ workerDep + " за номером " + cardNumber;
		
		Reporter.log("Нажимаем клавишу 'Зарегистрировать документ'<br>");
		regCardButton.click();
		
		cardLinkWindow.$(byName("regNumber")).sendKeys(lastCaseNum);
		cardLinkWindow.$(byText("Ok (Enter)")).click();
		
		errorWindow.$(byClassName("x-window-text")).shouldHave(text(errMessage));
		
		Reporter.log("закрываем окно с ошибкой нажатием на клавишу 'Ок'<br>");
		errorWindow.$(byText("OK")).click();
		
		cardLinkWindow.$(byText("Закрыть (Esc)")).click();
	}
	
	public static void numDocInAnotherDepartment() {
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Нажимаем клавишу 'Зарегистрировать документ'<br>");
		regCardButton.click();
		
		cardLinkWindow.$(byName("regNumber")).sendKeys(caseNumAnatherDep);
		cardLinkWindow.$(byText("Ok (Enter)")).click();
		
		errorWindow.$(byClassName("x-window-text")).shouldHave(text("Документ в структурное подразделение не передавался."));
		
		Reporter.log("закрываем окно с ошибкой нажатием на клавишу 'Ок'<br>");
		errorWindow.$(byText("OK")).click();
		
		cardLinkWindow.$(byText("Закрыть (Esc)")).click();
	}

	public static void numDocCopy() {
		String errMessage = "Регистрация документа на основе переданной в подразделение копии учетной карточки невозможна.";
		
		Reporter.log("Запоминаем название подразделения работника<br>");
 		String userDepart = $(byClassName("x-userinfo")).getText().split("\n")[1];
		
		Reporter.log("Открываем вкладку УПД<br>");
		$(byText("УПД")).click();
		
		$(byClassName("x-panel")).$$(byClassName("x-item")).findBy(text("Учёт новых документов")).click();
		
		Reporter.log("Выбираем подразделение<br>");
		DepartmentSelect(department);
		
		Reporter.log("Открываем последний созданный документ<br>");
		$(byClassName("case-list")).$(By.tagName("table")).$$(By.tagName("tr")).first().doubleClick();
		
		Reporter.log("<b>Делаем копию</b><br>");
		Reporter.log("Нажимаем кнопку 'Создать копию'<br>");
		ElementsCollection buttons = $$(byClassName("x-toolbar")).find(text("Создать копию")).$$(byClassName("x-btn"));
		SelenideElement copyButton = buttons.find(text("Создать копию"));
		copyButton.click();
		
		SelenideElement copyDocumentWindow = $$(byClassName("x-window")).filter(visible).find(text("Создание копии документа"));
		
		Reporter.log("Выбираем подразделение<br>");
		SelectDepartmentFromWindowTreeList(copyDocumentWindow.$$(By.tagName("table")), userDepart);
		
		copyDocumentWindow.$(byText("Вперёд >>")).click();
		
		Reporter.log("В окне с ошибкой нажимаем 'Продолжить'<br>");
		errorWindow.$(byText("Продолжить (Enter)")).click();
		
		Reporter.log("Выбираем тип документа<br>");
		copyDocumentWindow = $$(byClassName("x-window")).exclude(hidden).find(not(cssClass("x-toast")));
		SelenideElement docType = copyDocumentWindow.$$(byClassName("x-field")).find(text("Тип документа"));
		docType.$(byClassName("x-form-arrow-trigger")).click();
		
		docType = $$(byClassName("x-boundlist-list-ct")).find(Condition.visible);
		docType.$$(By.tagName("li")).first().click();
		
		Reporter.log("Нажимаем клавишу 'Сохранить (F10)'<br>");
		copyDocumentWindow.$(byText("Сохранить (F10)")).click();
		
		Reporter.log("Нажимаем клавишу 'ОК' в окне с информацией о созданной копии<br>");
		$(byClassName("x-message-box")).$(byText("OK")).click();
		  
		Reporter.log("Передаём копию в наше подразделение<br>");
		$$(byClassName("x-toolbar")).find(text("Создать копию")).$$(byClassName("x-btn")).find(text("Прием и передача")).click();;
		$$(byClassName("x-menu")).find(visible).$$(byClassName("x-menu-item")).find(text("Передать")).click(); 
		
		Reporter.log("Выбираем подразделение<br>");
		copyDocumentWindow = $$(byClassName("x-window")).filter(visible).find(text("Передать документ"));
		SelectDepartmentFromWindowTreeList(copyDocumentWindow.$$(By.tagName("table")), userDepart);
		
		copyDocumentWindow.$(byText("Передать")).click();
		
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Нажимаем клавишу 'Зарегистрировать документ'<br>");
		regCardButton.click();
		
		cardLinkWindow.$(byName("regNumber")).sendKeys(caseNumAnatherDep);
		cardLinkWindow.$(byText("Ok (Enter)")).click();
		
		errorWindow.$(byClassName("x-window-text")).shouldHave(text(errMessage));
		
		Reporter.log("закрываем окно с ошибкой нажатием на клавишу 'Ок'<br>");
		errorWindow.$(byText("OK")).click();
		
		cardLinkWindow.$(byText("Закрыть (Esc)")).click();
	}
	
	public static void numDocNonexistent() {
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Нажимаем клавишу 'Зарегистрировать документ'<br>");
		regCardButton.click();
		
		cardLinkWindow.$(byName("regNumber")).sendKeys("000000/00");
		cardLinkWindow.$(byText("Ok (Enter)")).click();
		
		errorWindow.$(byClassName("x-window-text")).shouldHave(text("Документ с указанным регистрационным номером не обнаружен. Повторите попытку ввода"));
		
		Reporter.log("закрываем окно с ошибкой нажатием на клавишу 'Ок'<br>");
		errorWindow.$(byText("OK")).click();
		
		cardLinkWindow.$(byText("Закрыть (Esc)")).click();
	}
}
