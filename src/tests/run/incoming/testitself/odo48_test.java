package tests.run.incoming.testitself;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.cssClass;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byName;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import tests.commons.pageobjects.UPD;
import tests.run.incoming.pages.IncomingMainPage;

public class odo48_test {
	
	static SelenideElement editButton = $$(byClassName("x-tree-panel")).find(visible).$(byAttribute("data-qtip", "Редактировать"));
	static String reportedBy;
	static HashMap<String, String> resolutionFieldsSaved;
	static ElementsCollection treeViewItems = $$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table"));
	static SelenideElement toolBar = $$(byClassName("x-panel")).exclude(hidden).get(9).$(byClassName("x-toolbar"));
	
	public static void FindingRegisteredDoc() {
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Ищем Зарегистрированный документ<br>");
		IncomingMainPage.FindRegisteredDoc("", "Зарегистрирован").click();
		
		Reporter.log("Запоминаем поле 'Доложен' для дальнейшей проверки<br>");
		ElementsCollection cardInfo = IncomingMainPage.GetCardInfo();
		ElementsCollection cardInfoMainFields = cardInfo.find(text("Основная информация")).$$(By.tagName("tr"));
		reportedBy = cardInfoMainFields.find(text("Доложен")).$$(By.tagName("td")).get(1).getText();
	}

	public static void AddingResolution(HashMap<String, String> resolutionFields) {
		resolutionFieldsSaved = resolutionFields;
		
		Reporter.log("Центральная панель с деревом объектов<br>");
		SelenideElement treePanel = $$(byClassName("x-tree-panel")).find(visible);
		SelenideElement createResolution = treePanel.$(byClassName("x-toolbar-docked-top")).$(byAttribute("data-qtip", "Создать корневую резолюцию"));
		ElementsCollection treeView = treePanel.$$(By.tagName("table"));
		
		Reporter.log("Переходим в Резолюции<br>");
		SelenideElement resolutions = treeView.find(text("Резолюции"));
		resolutions.click();
		
		Reporter.log("Здесь проверяем не создана ли уже резолюция, если создана удаляем её<br>");
		SelenideElement resolution = treeViewItems.get(IncomingMainPage.GetTreeItemIndex(treeViewItems, "Резолюции") + 1).$(By.tagName("tr"));
		if (resolution.has(cssClass("x-grid-tree-node-leaf"))) {
			resolution.click();
			toolBar.$(byText("Удалить")).click();
			SelenideElement deleteResolutionWindow = $$(byClassName("x-window")).find(text("Удаление"));
			deleteResolutionWindow.$(byName("comment")).setValue("Удаление");
			deleteResolutionWindow.$(byText("OK (Ctrl+Enter)")).click();
			deleteResolutionWindow.$(byText("OK")).click();
		}
		
		Reporter.log("Нажимаем кнопку Создать резолюцию<br>");
		createResolution.click();
		
		Reporter.log("Проверяем что поле Автор предварительно заполнено: " + reportedBy + "<br>");
		IncomingMainPage.CheckResolutionAutor(reportedBy);
		
		Reporter.log("Заполняем поля резолюции<br>");
		IncomingMainPage.FillResolutionFields(resolutionFields);
	}
	
	public static void CheckResolutionFields() {
		Reporter.log("Запоминаем имя и подразделение работника<br>");
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
 		String worker = userInfo.getText().split("\n")[0];
 		String workerDep = userInfo.getText().split("\n")[1];
 		
 		Reporter.log("Получаем короткое наименование подразделения работника<br>");
 		String workerDepShortName = UPD.GetDepartmentShortName(workerDep);
 		
		IncomingMainPage.CheckResolutionFields(worker, workerDepShortName);
	}
}
