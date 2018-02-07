package tests.run.incoming.testitself;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static common.utilities.Tools.getRandomInt;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import common.utilities.Tools;
import tests.run.incoming.pages.CorrespondentPage;
import tests.run.incoming.pages.IncomingMainPage;

public class odo00_test {
	
	static SelenideElement editButton = $$(byClassName("x-tree-panel")).find(visible).$(byAttribute("data-qtip", "Редактировать"));
	static SelenideElement registeredDoc;
	
	static HashMap<String, String> correspondentFieldsSaved = new HashMap<String, String>();;
	
	public static void FindingRegisteredDoc() {
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Ищем Зарегистрированный документ<br>"); 
		registeredDoc = IncomingMainPage.FindRegisteredDoc("", "Зарегистрирован");
	}

	public static void EditCorrespondent() {
		Reporter.log("Нажимаем кнопку 'Изменить корреспондента'<br>");
		editButton.click();
		
		SelenideElement regCard = $$(byClassName("x-window")).find(Condition.text("Редактирование регистрационной карточки"));
		SelenideElement changeCorrespondentButton = regCard.$(byClassName("x-toolbar")).$(byText("Изменить корреспондента"));
		ElementsCollection correspondentGrid = regCard.$(byClassName("x-grid-view")).$$(By.tagName("table"));
		
		Reporter.log("<b>Проверяем список корреспондентов, если он пустой добавляем одного</b><br>");
		if (correspondentGrid.size() == 0)
			CorrespondentPage.AddCorrespondent("тестовский", true);
		else if (correspondentGrid.size() > 1)
			CorrespondentPage.DeleteExtraCorrespondents(correspondentGrid);
		
		Reporter.log("<b>Редактируем Корреспондента</b><br>");
		Reporter.log("Выбираем первого корреспондента в таблице<br>");
		SelenideElement correspondent = correspondentGrid.first();
		correspondent.click();
		//SelenideElement correspondent = CorrespondentPage.SelectRandomCorrespondent(correspondentGrid);

		correspondentFieldsSaved.put("Корреспондент", correspondent.$$(By.tagName("td")).get(0).getText());
		
		Reporter.log("Нажимаем кнопку 'Изменить корреспондента'<br>");
		changeCorrespondentButton.click();
		
		SelenideElement editCorrespondentWindow = $$(byClassName("x-window")).find(text("Выбор корреспондента"));
		
		//Reporter.log("Нажимаем кнопку 'Поиск корреспондента'<br>");
		//editCorrespondentWindow.$$(byClassName("x-form-trigger")).find(visible).click();
		
		//Reporter.log("Выбираем нового случайного корресподента из списка<br>");
		//SelenideElement addingPersonal = $$(byClassName("x-window")).exclude(hidden).find(text("Добавление персоналии"));
		//addingPersonal.$(byName("name")).setValue("Тестовский").pressEnter();
		
		//WaitingForUpdateAllWindows();
		
		//ElementsCollection personalList = addingPersonal.$(byClassName("x-grid-item-container")).$$(byClassName("x-grid-row"));
		//SelenideElement person = personalList.get(getRandomInt(personalList.size()));
		//person.click();
		
		//ElementsCollection personFields = person.$$(By.tagName("td"));
		//correspondentFieldsSaved.put("Корреспондент", personFields.get(0).getText().trim());
		//correspondentFieldsSaved.put("Тип лица", personFields.get(1).getText());
		//correspondentFieldsSaved.put("ИНН", personFields.get(2).getText());

		//addingPersonal.$(byText("Выбрать (Enter)")).click();
		
		//Reporter.log("Проверяем что поле Корреспондент заполнилось новым значением<br>");
		//editCorrespondentWindow.$(byName("corr")).shouldHave(value(correspondentFieldsSaved.get("Корреспондент")));
		
		Reporter.log("Редактируем Исходящий номер<br>");
		
		String tmp = Integer.toString(getRandomInt(999999999));
		editCorrespondentWindow.$(byName("outNumber")).setValue(tmp);
		correspondentFieldsSaved.put("Исходящий номер", tmp);
		
		Reporter.log("Редактируем Исходящую дату<br>");
		tmp = Tools.getRandomLongDate("", "");
		editCorrespondentWindow.$(byName("outDate")).setValue(tmp);
		correspondentFieldsSaved.put("Исходящая дата", tmp);
		
		editCorrespondentWindow.$(byText("Ok (Enter)")).click();
		
		Reporter.log("Проверяем отредактированные поля Корреспондента<br>");
		ElementsCollection correspondentFields = correspondent.$$(By.tagName("td"));
		correspondentFields.get(0).shouldHave(text(correspondentFieldsSaved.get("Корреспондент")));
		correspondentFields.get(1).shouldHave(text(correspondentFieldsSaved.get("Исходящий номер")));
		correspondentFields.get(2).shouldHave(text(correspondentFieldsSaved.get("Исходящая дата")));
		
		Reporter.log("Нажимаем кнопку 'Сохранить'<br>");
		regCard.$(byText("Сохранить")).click();
		
		SelenideElement errorWindow = $$(byClassName("x-message-box")).find(visible);
		
		Reporter.log("Проверяем выбрана ли основной корреспондент, если нет выбираем его<br>");
		if (errorWindow.exists()) {
			errorWindow.shouldHave(text("Выберите основную запись!"));
			errorWindow.$(byText("OK")).click();
			correspondentFields.get(3).click();
			Reporter.log("Нажимаем кнопку 'Сохранить' ещё раз<br>");
			regCard.$(byText("Сохранить")).click();
		}
	}

	public static void CheckEditedCorrespondent() {
		CorrespondentPage.CheckEditedCorrFields(correspondentFieldsSaved);
	}
}
