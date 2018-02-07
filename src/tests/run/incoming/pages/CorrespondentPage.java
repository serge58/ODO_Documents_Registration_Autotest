package tests.run.incoming.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

import static common.utilities.Tools.getRandomInt;
import static tests.commons.pageobjects.Common.WaitingForUpdateAllWindows;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import common.utilities.Tools;

import tests.run.incoming.pages.IncomingMainPage;

public class CorrespondentPage {
	
	static ElementsCollection cardInfo = IncomingMainPage.GetCardInfo();
	
	static List<String> personData;
	
	public static List<String> AddCorrespondent(HashMap<String, String> correspondentFields) {
		 SelenideElement regCard = $$(byClassName("x-window")).find(Condition.text("Регистрационная карточка входящего документа"));
		
		Reporter.log("Добавляем Корреспондента<br>");
		SelenideElement correspondentToolbar = regCard.$(byClassName("x-toolbar"));
		correspondentToolbar.$(byText("Добавить корреспондента")).click();
		SelenideElement addingPersonal = $$(byClassName("x-window")).find(text("Добавление персоналии"));
		addingPersonal.$(byName("name")).setValue(correspondentFields.get("Корреспондент")).pressEnter();
		
		WaitingForUpdateAllWindows();
		
		ElementsCollection personalList = addingPersonal.$(byClassName("x-grid")).$$(By.tagName("table"));
		
		SelenideElement person = personalList.get(getRandomInt(personalList.size()));
		
		person.click();

		personData = person.$$(By.tagName("td")).texts();

		addingPersonal.$(byText("Выбрать (Enter)")).click();
		
		SelenideElement corrSelectWindow = $$(byClassName("x-window")).find(Condition.text("Выбор корреспондента"));
		corrSelectWindow.$(byText("Ok (Enter)")).click();
		
		return personData;
	}
	
	public static List<String> AddCorrespondent(String correspondent, boolean edit) {
		SelenideElement regCard;
		
		if (edit)
			regCard = $$(byClassName("x-window")).find(Condition.text("Редактирование регистрационной карточки"));
		else
			regCard = $$(byClassName("x-window")).find(Condition.text("Регистрационная карточка входящего документа"));
		
		Reporter.log("Добавляем Корреспондента<br>");
		SelenideElement correspondentToolbar = regCard.$(byClassName("x-toolbar"));
		correspondentToolbar.$(byText("Добавить корреспондента")).click();
		SelenideElement addingPersonal = $$(byClassName("x-window")).find(Condition.text("Добавление персоналии"));
		addingPersonal.$(byName("name")).setValue(correspondent).pressEnter();
		
		WaitingForUpdateAllWindows();
		
		ElementsCollection personalList = addingPersonal.$(byClassName("x-grid")).$$(By.tagName("table"));
		SelenideElement person = personalList.get(getRandomInt(personalList.size()));
		personData = person.$$(By.tagName("td")).texts();
		person.click();
		
		addingPersonal.$(byText("Выбрать (Enter)")).click();
		
		WaitingForUpdateAllWindows();
		
		SelenideElement corrSelectWindow = $$(byClassName("x-window")).find(Condition.text("Выбор корреспондента"));
		corrSelectWindow.$(byText("Ok (Enter)")).click();
		
		return personData;
	}
	
	public static void CheckEditedCorrFields(HashMap<String, String> correspondentFieldsSaved) {
		SelenideElement cardInfoCorrespondent = cardInfo.find(text("Корреспонденты"));
		
		Reporter.log("Корреспонденты<br>");
		
		ElementsCollection correspondentRows = cardInfoCorrespondent.$$(By.tagName("table"));
		
		Reporter.log("Табличка с Корреспондентами должна содержать только одну строку. Противный случай означает, что что-то пошло не так!<br>");
		correspondentRows.shouldHaveSize(1);
		
		ElementsCollection corrColumns = correspondentRows.first().$$(By.tagName("td"));
		corrColumns.get(0).shouldHave(text(correspondentFieldsSaved.get("Корреспондент")));
		corrColumns.get(1).shouldHave(text(correspondentFieldsSaved.get("Исходящий номер")));
		corrColumns.get(2).shouldHave(text(correspondentFieldsSaved.get("Исходящая дата")));
	}
	
	public static void DeleteExtraCorrespondents(ElementsCollection correspondentGrid) {
		SelenideElement regCard = $$(byClassName("x-window")).find(Condition.text("Редактирование регистрационной карточки"));
		SelenideElement deleteCorrespondentButton = regCard.$(byClassName("x-toolbar")).$(byText("Удалить корреспондента"));
		
		int limit = correspondentGrid.size() - 1;
		
		for (int i = 0; i < limit; i++) {
			correspondentGrid.first().click();
			deleteCorrespondentButton.click();
		}
	}
	
	public static SelenideElement SelectRandomCorrespondent(ElementsCollection correspondentGrid) {
		SelenideElement selCorrespondent =  correspondentGrid.get(Tools.getRandomInt(correspondentGrid.size()));
		selCorrespondent.click();
		
		return selCorrespondent;
	}
}
