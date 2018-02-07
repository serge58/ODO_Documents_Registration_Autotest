package tests.commons.pageobjects;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static common.httpclient.utilities.Utilities.GetDepartmentID;
import static common.httpclient.utilities.Utilities.GetDeptShortName;
import static tests.commons.pageobjects.Common.WaitingForUpdateAllWindows;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import common.httpclient.HttpClient;
import common.httpclient.Services;

public class UPD {

	static HttpClient httpClient;
	
	public static Number GetDepartmentId(String departmentName, String baseURL, String login, String password) {
		String result = "";
		Number depID = 0;
		
		httpClient = new HttpClient(baseURL, login, password);
		
		try {
			result = httpClient.ExecuteGetRequest(Services.EMPLOYEES_DEPARTMENT_TREE + "?activeOnly=true&node=root");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		if (!result.contains("\"success\":false")) 
			depID = GetDepartmentID(result, departmentName);
		
		return depID;
	}
	
	public static String GetDepartmentShortName(String departmentName) {
		String result = "";
		Number departmentId = 0;
		
		URL url;
		String baseURL = "";
				
		try {
			url = new URL(WebDriverRunner.url());
			baseURL = url.getProtocol() + "://" + url.getHost() + url.getPath();
 		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		httpClient = new HttpClient(baseURL, "kozlov", "zzzzzzzz");
		
		// Получаем ID подразделения
		try {
			result = httpClient.ExecuteGetRequest(Services.EMPLOYEES_DEPARTMENT_TREE + "?activeOnly=true&node=root");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		if (!result.contains("\"success\":false")) 
			departmentId = GetDepartmentID(result, departmentName);
		
		// Получаем короткое имя подразделения
		try {
			result = httpClient.ExecuteGetRequest(Services.EMPLOYEES_DEPARTMENT + "/" + departmentId);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		return GetDeptShortName(result);
	}

	public static SelenideElement DepartmentSelect(String department) {
		WaitingForUpdateAllWindows();
		
		ElementsCollection departs = $$(byClassName("x-grid-body")).exclude(hidden).first().$$(byClassName("x-grid-item"));
		
		SelenideElement retDepart = departs.find(text(department));
		retDepart.scrollIntoView(true).click();
		
		WaitingForUpdateAllWindows();
		return retDepart;
	}
	
	public static void SelectDepartmentFromWindowTreeList(ElementsCollection depsTreeList, String depName) {
		depsTreeList.find(text(depName)).scrollIntoView(true).click();
	}
	
	public static SelenideElement FindingDocumentByNumber(String docNumber) {
		return $$(byClassName("case-list")).find(visible).$(By.tagName("table")).$$(By.tagName("tr")).find(text(docNumber));
	}
		
	public static void CreateEditDocument(HashMap<String, String> docFields, boolean allFields) {
		SelenideElement editCardField;
		
		((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("window.print=function(){};");
		Reporter.log("Нажимаем кнопку Принять к учёту<br>");
		$$(byClassName("x-toolbar")).findBy(text("Принять к учёту (Пробел)")).$(byText("Принять к учёту (Пробел)")).click();
		
		//******************* Заполняем форму *********************
		Reporter.log("Тип документа<br>");
		
		SelenideElement accountNewDoc = $$(byClassName("x-window")).exclude(hidden).find(not(cssClass("x-toast")));
		
		Selenide.actions().dragAndDropBy(accountNewDoc.$(byClassName("x-window-header")), 0, -20).perform();
		
 		SelenideElement mainInfo = accountNewDoc.$$(byClassName("x-fieldset")).find(text("Основная информация"));
		
		editCardField = mainInfo.$$(byClassName("x-field")).find(text("Тип документа"));	// Находим строку с типом
		editCardField.$(byClassName("x-form-arrow-trigger")).click();						// Проверяем лейбл и жмем стрелочку
		
		editCardField = $$(byClassName("x-boundlist-list-ct")).find(Condition.visible);
		editCardField.$$(By.tagName("li")).filterBy(text(docFields.get("Тип документа"))).first().click();	// В открывшемся списке ищем нужный тип 
		
		if (allFields) {
			Reporter.log("Автор (подписант)<br>");
			editCardField = mainInfo.$$(byClassName("x-form-item")).find(text("Автор (подписант):"));
			
			String[] items = docFields.get("Автор (подписант)").split(",");
			
			for (String item: items) {
				editCardField.$(byAttribute("data-qtip", "Добавить")).click();
				editCardField.$(byAttribute("type", "text")).setValue(item);
			}
			
			Reporter.log("В отношении кого<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("В отношении кого:"));
			
			items = docFields.get("В отношении кого").split(",");
			
			for (String item: items) {
				editCardField.$(byAttribute("data-qtip", "Добавить")).click();
				editCardField.$(byAttribute("type", "text")).setValue(item);
			}

			Reporter.log("Откуда поступил<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Откуда поступил:"));
			$(byName("whereFrom")).setValue(docFields.get("Откуда поступил"));	
			
			Reporter.log("Индекс субъекта РФ<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Индекс субъекта РФ:"));	// Находим строку с индексом
			editCardField.$(byClassName("x-form-arrow-trigger")).click();							// Проверяем лейбл и жмем стрелочку
			$$(byClassName("x-boundlist-list-ct")).find(Condition.visible).$$(byClassName("x-boundlist-item")).find(text(docFields.get("Индекс субъекта РФ"))).click();	// В открывшемся списке ищем нужный индекс
			
			Reporter.log("Исходящий номер<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Исходящий номер:"));
			editCardField.$(byName("outNumber")).setValue(docFields.get("Исходящий номер"));
			
			Reporter.log("Исходящая дата<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Исходящая дата:"));
			editCardField.$(byName("outDate")).setValue(docFields.get("Исходящая дата"));
			
			Reporter.log("Примечание<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Примечание:"));
			editCardField.$(byName("comments")).setValue(docFields.get("Примечание"));
			
			Reporter.log("Приложение<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Приложение:"));
			editCardField.$(byName("app")).setValue(docFields.get("Приложение"));
		
			Reporter.log("ШПИ отправления<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("ШПИ отправления:"));
			editCardField.$(byName("postNumber")).setValue(docFields.get("ШПИ отправления"));

			Reporter.log("Листов/томов<br>");
			editCardField = mainInfo.$$(byClassName("x-field")).find(text("Листов/Томов:"));
			editCardField.$(byName("volNumber")).setValue(docFields.get("Листов/томов"));

			//******************************************************************************************************************************************
			
			Reporter.log("Находим набор полей 'Дополнительная информация'<br>");
			SelenideElement additionInfo = $$(byClassName("x-fieldset")).find(text("Дополнительная информация"));
			
			Reporter.log("Открываем набор полей с Дополнительной информацией<br>");
			if (additionInfo.has(cssClass("x-fieldset-collapsed"))) 
				additionInfo.$(By.tagName("legend")).click();
			
			Reporter.log("Рег. номер в общем ДО<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Рег. номер в общем ДО:"));
			editCardField.$(byName("regNum")).setValue(docFields.get("Рег. номер в общем ДО"));
			
			Reporter.log("Номер жалобного производства<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Номер жалобного производства:"));
			editCardField.$(byName("complaintNum")).setValue(docFields.get("Номер жалобного производства"));
			
			Reporter.log("Номер дела в ВС РФ<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Номер дела в ВС РФ:"));
			editCardField.$(byName("caseNumber")).setValue(docFields.get("Номер дела в ВС РФ"));
			
			Reporter.log("Краткое содержание<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Краткое содержание:"));
			editCardField.$(byName("desc")).setValue(docFields.get("Краткое содержание"));
			
			Reporter.log("Дата передачи судье<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Дата передачи судье:"));
			editCardField.$(byName("judgeDate")).setValue(docFields.get("Дата передачи судье"));
			
			Reporter.log("Этап судопроизводства<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Этап судопроизводства:"));
			editCardField.$(byClassName("x-form-arrow-trigger")).click();
			$$(byClassName("x-boundlist-list-ct")).find(Condition.visible).$$(byClassName("x-boundlist-item")).find(text(docFields.get("Этап судопроизводства"))).click();
			
			Reporter.log("Подшит/Отправлен<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Подшит/Отправлен:"));
			editCardField.$(byClassName("x-form-arrow-trigger")).click();
			$$(byClassName("x-boundlist-list-ct")).find(Condition.visible).$$(byClassName("x-boundlist-item")).find(text(docFields.get("Подшит/отправлен"))).click();
			
			Reporter.log("Дата подшивки/отправки<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Дата подшивки/отправки:"));
			editCardField.$(byName("storingDate")).setValue(docFields.get("Дата отправки"));
			
			Reporter.log("Адрес отправки<br>");
			editCardField = additionInfo.$$(byClassName("x-field")).find(text("Адрес отправки:"));
			editCardField.$(byName("sentAddress")).setValue(docFields.get("Адрес отправки"));
			
			Reporter.log("Закрываем набор полей с Дополнительной информацией<br>");
			if (!additionInfo.has(cssClass("x-fieldset-collapsed"))) 
				additionInfo.$(By.tagName("legend")).click();
		}
		
		((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("window.print=function(){};");
		
		Reporter.log("Сохраняем документ<br>");
		accountNewDoc.$(byText("Сохранить (F10)")).click();
		
		WaitingForUpdateAllWindows();
	}
}
