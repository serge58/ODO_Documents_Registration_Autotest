package tests.run.incoming.testitself;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.HashMap;
import java.util.List;

import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import tests.run.incoming.pages.CorrespondentPage;
import tests.run.incoming.pages.IncomingMainPage;

public class odo47_test {
	
	static SelenideElement editButton = $$(byClassName("x-tree-panel")).find(visible).$(byAttribute("data-qtip", "Редактировать"));
	static SelenideElement registeredDoc;
	
	static HashMap<String, String> regCardFieldsSaved;
	static List<String> personData;
	
	public static void CheckRevokedDocNotEdited() {
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
		
		Reporter.log("Ищем Отозванный документ<br>");
		SelenideElement revokedDoc = IncomingMainPage.FindRegisteredDoc("", "Отозван");
		
		if (!revokedDoc.exists()) {
			revokedDoc = IncomingMainPage.FindRegisteredDoc("", "Зарегистрирован");
			IncomingMainPage.RecallDocument();
		}

		Reporter.log("Проверяем что кнопка 'Редактировать' недоступна<br>");
		editButton.shouldHave(cssClass("x-item-disabled"));
	}
	
	public static void FindingRegisteredDoc() {
		Reporter.log("Ищем Зарегистрированный документ<br>");
		registeredDoc = IncomingMainPage.FindRegisteredDoc("", "Зарегистрирован");
	}

	public static void EditDocument(HashMap<String, String> regCardFields) {
		regCardFieldsSaved = regCardFields;
		
		Reporter.log("Нажимаем кнопку 'Редактировать'<br>");
		editButton.click();
		
		SelenideElement regCard = $$(byClassName("x-window")).find(Condition.text("Редактирование регистрационной карточки"));;
		
		Reporter.log("Редактируем документ<br>");
		IncomingMainPage.FillDocFields(regCardFields, true);
		
		Reporter.log("Добавляем корреспондента<br>");
		personData = CorrespondentPage.AddCorrespondent(regCardFields.get("Корреспондент"), true);
		
		Reporter.log("Нажимаем кнопку 'Сохранить'<br>");
		regCard.$(byText("Сохранить")).click();
	}

	public static void CheckEditedFields() {
		IncomingMainPage.CheckEditedDocFields(regCardFieldsSaved, registeredDoc, personData);
	}
	
	public static void CheckEditedRegCard() {
		IncomingMainPage.CheckEditedRegCardFields(regCardFieldsSaved);
	}
}
