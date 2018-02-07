package tests.run.outgoing.pages;

import static com.codeborne.selenide.Condition.*;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static common.utilities.Tools.generateRandomSymbolsRU;
import static common.utilities.Tools.getNowLongDate;
import static common.utilities.Tools.getRandomInt;
import static tests.commons.pageobjects.Common.SelectFromDropDown;
import static tests.commons.pageobjects.Common.WaitingForUpdateAllWindows;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import common.utilities.Tools;
import tests.commons.pageobjects.UPD;

public class OutgoingMainPage {
	SelenideElement addDocsButton;
	SelenideElement createDocDraftButton;
	
	SelenideElement documentButton;
	SelenideElement registerDocButton;
	SelenideElement recallDocButton;
	
	SelenideElement editDocDraftButton;
	SelenideElement deleteDocDraftButton;
	
	SelenideElement filterToolbar;
	SelenideElement searchToolbar;
	
	SelenideElement cardsTypeButton;
	ElementsCollection cardTypesList;
	
	SelenideElement docListPanel;
	ElementsCollection docList;
	
	SelenideElement docCardPanel;
	
	HashMap<String, String> docDraftFields;
	
	public OutgoingMainPage() {
		docListPanel = $$(byClassName("x-panel")).exclude(hidden).get(4);
		filterToolbar = docListPanel.$$(byClassName("x-toolbar")).get(1);
		searchToolbar = docListPanel.$$(byClassName("x-toolbar")).get(0);
		
		docList = docListPanel.$$(byClassName("x-dataview-item"));
		
		addDocsButton = searchToolbar.$$(byClassName("x-btn")).get(0);
		createDocDraftButton = $$(byClassName("x-menu")).find(visible).$$(byClassName("x-menu-item")).get(0);
		
		documentButton = $$(byClassName("x-toolbar")).exclude(hidden).get(5).$(byText("Документ"));
		registerDocButton = $$(byClassName("x-menu")).find(visible).$$(byClassName("x-menu-item")).get(0);
		recallDocButton = $$(byClassName("x-menu")).find(visible).$$(byClassName("x-menu-item")).get(1);
		
		editDocDraftButton = $$(byClassName("x-toolbar")).exclude(hidden).get(4).$$(byClassName("x-toolbar-item")).get(1);
		deleteDocDraftButton = $$(byClassName("x-toolbar")).exclude(hidden).get(4).$$(byClassName("x-toolbar-item")).get(2);
		
		cardsTypeButton = filterToolbar.$$(byClassName("x-btn")).get(0);
		cardTypesList = $$(byClassName("x-menu")).find(visible).$$(byClassName("x-menu-item"));
		
		docCardPanel = $$(byClassName("x-panel")).exclude(hidden).get(8);
	}
	
	public HashMap<String, String> CreateEditDocDraft(boolean edit) {
		if (edit) 
			editDocDraftButton.click();
		else {
			addDocsButton.click();
			createDocDraftButton.click();
		}
		
		SelenideElement docDraftCard = $$(byClassName("x-window")).find(text("Карточка проекта исходящего документа"));
		ElementsCollection cardFields = docDraftCard.$$(byClassName("x-container")).exclude(hidden);
		
		Reporter.log("Выбираем тип документа<br>");
		SelectFromDropDown(cardFields.get(0).$(byClassName("x-form-item-body")), docDraftFields.get("Тип документа"));
		
		Reporter.log("Удаляем всех адресатов<br>");
		DeleteExtraAddressee();
		
		Reporter.log("Добавляем адресата<br>");
		cardFields.get(1).$(byText("Добавить адресата")).click();
		HashMap<String, String> personData = AddAddressee(docDraftFields.get("Адресат"), false);
		
		Reporter.log("Проверяем что адресат добавился<br>");
		cardFields.get(1).$(By.tagName("table")).shouldHave(text(personData.get("Адресат") + ", " + personData.get("Тип лица") + " " + personData.get("Куда")));
		
		Reporter.log("Добавляем исполнителя<br>");
		cardFields.get(2).$(byText("Добавить исполнителя")).click();
		String worker = SelectWorker(docDraftFields.get("Исполнитель"));
		docDraftFields.replace("Исполнитель", worker);
		
		Reporter.log("Проверяем что исполнитель добавился<br>");
		cardFields.get(2).$$(By.tagName("table")).find(text(worker)).should(exist);
		
		Reporter.log("Добавляем подписанта<br>");
		cardFields.get(3).$(byText("Добавить подписанта")).click();
		worker = SelectWorker(docDraftFields.get("Подписант"));
		docDraftFields.replace("Подписант", worker);
		
		Reporter.log("Проверяем что подписант добавился<br>");
		cardFields.get(3).$$(By.tagName("table")).find(text(worker)).should(exist);
		
		Reporter.log("Сохраняем карточку проекта<br>");
		docDraftCard.$(byText("Сохранить")).click();
		
		//docDraftCard.waitWhile(visible, 10000);
		
		personData.put("Учетный номер", docList.first().$(byClassName("number")).getText().substring(0, 10));
		personData.put("Дата создания", docList.first().$(byClassName("number")).getText().substring(3));
		
 		SelenideElement userInfo = $(byClassName("x-userinfo"));
		personData.put("Автор", userInfo.getText().split("\n")[0]);
		personData.put("Подразделение", userInfo.getText().split("\n")[1]);
		personData.put("Короткое название подразделения", UPD.GetDepartmentShortName(userInfo.getText().split("\n")[1]));
		
		return personData;
	}
	
	private HashMap<String, String> AddAddressee(String addressee, boolean edit) {
		HashMap<String, String> personData = new HashMap<String, String>();
		SelenideElement addresseeWindow;
		
		if (!edit)
			addresseeWindow = $$(byClassName("x-window")).findBy(text("Добавление адресата"));
		else
			addresseeWindow = $$(byClassName("x-window")).findBy(text("Редактирование адресата"));
		
		SelenideElement addingPersonal = $$(byClassName("x-window")).find(text("Добавление персоналии"));
		SelenideElement withRepresentativeOffices = addingPersonal.$(byText("С учётом представительств"));
		//SelenideElement legalEntities = addingPersonal.$(byText("Юр. лица"));
		SelenideElement physicalPersons = addingPersonal.$(byText("Физ. лица"));
		
		withRepresentativeOffices.click();
		physicalPersons.click();
		
		addingPersonal.$(byName("name")).setValue(addressee).pressEnter();
		
		WaitingForUpdateAllWindows();
		
		ElementsCollection personalList = addingPersonal.$$(By.tagName("table"));
		SelenideElement person = personalList.get(getRandomInt(personalList.size()));
		
		person.click();

		personData.put("ФИО", person.$$(By.tagName("td")).get(0).getText());
		personData.put("Тип лица", "гражданин"); //person.$$(By.tagName("td")).get(1).getText());
		
		addingPersonal.$(byText("Выбрать (Enter)")).click();
		
		WaitingForUpdateAllWindows();
		
		personData.put("Адресат", addresseeWindow.$(byName("personFirst")).getValue());
		personData.put("Адрес", addresseeWindow.$(byName("addrItem")).getValue());
		personData.put("Кому", addresseeWindow.$(byName("recipient")).getValue());
		personData.put("Куда", addresseeWindow.$(byName("address")).getValue());
		
		addresseeWindow.$(byText("Сохранить")).click();
		
		WaitingForUpdateAllWindows();
		
		return personData;
	}
	
	public void CheckDocDraft(HashMap<String, String> personData) {
		Reporter.log("<b>Проверяем секцию 'Основная информация'</b><br>");
		SelenideElement mainInfo = docCardPanel.$$(byClassName("x-panel-default-framed")).find(text("Основная информация"));
		
		Reporter.log("Проверяем Учётный номер<br>");
		ElementsCollection fields = mainInfo.$$(By.tagName("tr")).get(0).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Учетный номер"));
		fields.get(1).shouldHave(text(personData.get("Учетный номер")));
		
		Reporter.log("Проверяем Дату создания<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(1).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Дата создания"));
		fields.get(1).shouldHave(text(personData.get("Дата создания")));
		
		Reporter.log("Проверяем Тип документа<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(2).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Тип документа"));
		fields.get(1).shouldHave(text(docDraftFields.get("Тип документа")));
		
		// ------------ Не реализовано --------------
		Reporter.log("Проверяем Тип телеграммы<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(3).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Тип телеграммы"));
		
		Reporter.log("Проверяем Срок доставки телеграммы<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(4).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Срок доставки телеграммы"));
		// ------------------------------------------
		
		Reporter.log("Проверяем Подразделение<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(5).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Подразделение"));
		fields.get(1).shouldHave(text(personData.get("Подразделение")));
		
		Reporter.log("Проверяем Кем создан документ<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(6).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Кем создан документ"));
		fields.get(1).shouldHave(text(personData.get("Автор") + ", " + personData.get("Короткое название подразделения")));
		
		Reporter.log("Проверяем Номер производства<br>");
		fields = mainInfo.$$(By.tagName("tr")).get(7).$$(By.tagName("td"));
		fields.get(0).shouldHave(text("Номер производства"));
		//********************************************************************************************
		
		Reporter.log("Проверяем секцию 'Адресаты'<br>");
		SelenideElement addressees = docCardPanel.$$(byClassName("x-panel-default-framed")).find(text("Адресаты"));
		addressees.shouldHave(text(personData.get("ФИО")));
		addressees.shouldHave(text(personData.get("Адрес")));
		
		Reporter.log("Проверяем секцию 'Исполнители документа'<br>");
		SelenideElement docExecutant = docCardPanel.$$(byClassName("x-panel-default-framed")).find(text("Исполнители документа"));
		docExecutant.shouldHave(text(docDraftFields.get("Исполнитель")));
		
		Reporter.log("Проверяем секцию 'Подписанты'<br>");
		SelenideElement subscribers = docCardPanel.$$(byClassName("x-panel-default-framed")).find(text("Подписанты"));
		subscribers.shouldHave(text(docDraftFields.get("Подписант")));
		
		Reporter.log("Проверяем секцию 'Дополнительная информация'br>"); //*******************************
		SelenideElement additionalInfo = docCardPanel.$$(byClassName("x-panel-default-framed")).find(text("Дополнительная информация"));
		
		Reporter.log("Добавляем новое свойство<br>");
		String PROPERTY_NAME = generateRandomSymbolsRU(10);
		String PROPERTY_VALUE = generateRandomSymbolsRU(30);
		
		SelenideElement cardInfoToolbar = $$(byClassName("x-toolbar-docked-top")).exclude(hidden).last();
		cardInfoToolbar.$(byText("Свойства")).click();
		SelenideElement properties = $$(byClassName("x-box-target")).exclude(hidden).last();
		properties.$(byText("Добавить свойство")).click();
		SelenideElement propertiesWindow = $$(byClassName("x-window")).find(text("Дополнительное свойство"));
		SelenideElement field = propertiesWindow.$$(byClassName("x-field")).find(text("Название *:"));
		field.$(byName("title")).setValue(PROPERTY_NAME);
		
		field = propertiesWindow.$$(byClassName("x-field")).find(text("Значение *:"));
		field.$(byName("valString")).setValue(PROPERTY_VALUE);
		
		propertiesWindow.$(byText("Сохранить")).click();
		
		Reporter.log("Проверяем что новое свойство добавилось<br>");
		ElementsCollection rows = additionalInfo.$$(By.tagName("table")).find(text(PROPERTY_NAME)).$$(By.tagName("td")); //last().$$(By.tagName("td"));
		rows.get(0).shouldHave(text(PROPERTY_NAME));
		rows.get(1).shouldHave(text(PROPERTY_VALUE));
		rows.get(2).shouldHave(text(getNowLongDate()));
		rows.get(3).shouldHave(text(personData.get("Автор") + "," + personData.get("Короткое название подразделения")));
		//************************************************************************************************
		
		Reporter.log("Проверяем секцию 'История обработки'<br>");
		SelenideElement procHistory = docCardPanel.$$(byClassName("x-panel-default-framed")).find(text("История обработки"));
		rows = procHistory.$$(By.tagName("table")).first().$$(By.tagName("td"));
		rows.get(0).shouldHave(text(getNowLongDate()));
		rows.get(1).shouldHave(text("Проект"));
		rows.get(2).shouldHave(text(personData.get("Автор") + ", " + personData.get("Короткое название подразделения")));
	}
	
	public void RecallDocDraft() {
		documentButton.click();
		recallDocButton.click();
		SelenideElement recallDocWindow = $$(byClassName("x-window")).exclude(hidden).find(text("Отзыв проекта документа"));
		recallDocWindow.shouldHave(text("Отзыв проекта документа\r\n" + 
				"Вы действительно хотите отозвать проект документа?\r\n" + 
				"После отзыва документа его изменение будет невозможно, все почтовые отправления по документу будут удалены."));
		recallDocWindow.$(byName("comment")).setValue("Проект документа отозван " + generateRandomSymbolsRU(30));
		recallDocWindow.$(byText("Отозвать")).click();
	}
	
	public void CheckDocDraftRecalled(SelenideElement docDraftRecalled) {
		Reporter.log("Проверяем что документ отозван<br>");
		docDraftRecalled.$(byClassName("state")).shouldHave(text("Отозван"));
		
		Reporter.log("Проверяем что кнопка 'Отозвать' неактивно<br>");
		documentButton.click();
		recallDocButton.shouldHave(cssClass("x-menu-item-disabled"));
	}
	
	public void DeleteDocDraft() {
		deleteDocDraftButton.click();
		SelenideElement recallDocWindow = $$(byClassName("x-window")).exclude(hidden).find(text("Удаление проекта документа"));
		recallDocWindow.shouldHave(text("Вы действительно хотите удалить проект документа?"));
		recallDocWindow.$(byText("Удалить")).click();
	}

	public void CheckDocDraftDeleted(String docNumber) {
		Reporter.log("Проверяем что документ удалён<br>");
		ElementsCollection docCardList = $$(byClassName("docflow-list")).findBy(visible).$$(byClassName("x-item"));
		docCardList.find(text(docNumber)).shouldNot(exist);
	}
	
	public void RegisterDocDraft() {
		documentButton.click();
		registerDocButton.click();
		
		SelenideElement OutgoingDocRegCard = $$(byClassName("x-window")).find(text("Регистрационная карточка исходящего документа"));
		ElementsCollection cardFields = OutgoingDocRegCard.$$(byClassName("x-container")).exclude(hidden);
	}
	
	public void CheckRegisteredDocDraft() {
		ElementsCollection docCardList = $$(byClassName("docflow-list")).findBy(visible).$$(byClassName("x-item"));
	}
	
	public SelenideElement FindDocument(String docStatus) {
		ElementsCollection docCardList = $$(byClassName("docflow-list")).findBy(visible).$$(byClassName("x-item"));
		SelenideElement docCard = null;
		
		for (SelenideElement docCardTemp : docCardList) {
			if (docCardTemp.$(byClassName("state")).has(text(docStatus))) {
				docCard = docCardTemp;
				docCard.click();
				break;
			}
		}
		
		return docCard;
	}
	
	public String GetDocNumber(SelenideElement doc) {
		return doc.$(byClassName("number")).getText().split(" ")[0];
	}
	
	public void SetDocDraftFields(HashMap<String, String> docDraftFields) {
		this.docDraftFields = docDraftFields;
	}
	
	public void SelectCardType(String cardType) {
		cardsTypeButton.click();
		cardTypesList.find(text(cardType)).click();
	}
	
	private static String SelectWorker(String workerName) {
		SelenideElement selectControllerWindow = $$(byClassName("x-window")).find(text("Выбор работников"));
		SelenideElement worker;
		String returnName;
		
		ElementsCollection workers = selectControllerWindow.$$(By.tagName("table"));
		
		if (workerName.isEmpty()) {
			worker = workers.get(Tools.getRandomInt(workers.size()));
		} else {
			worker = workers.find(text(workerName));
		}
		
		worker.click();
		String dep = worker.$(By.tagName("span")).text();
		returnName = worker.getText().replace(dep, "");
		
		selectControllerWindow.$(byText("Выбрать")).click();
		return returnName;
	}
	
	private static void DeleteExtraAddressee() {
		SelenideElement draftCard = $$(byClassName("x-window")).find(text("Карточка проекта исходящего документа"));
		SelenideElement deleteAddresseeButton = draftCard.$(byClassName("x-toolbar")).$(byText("Удалить адресата"));
		ElementsCollection addresseeGrid = draftCard.$(byClassName("x-grid-view")).$$(By.tagName("table"));
		
		int limit = addresseeGrid.size();
		
		for (int i = 0; i < limit; i++) {
			addresseeGrid.first().click();
			deleteAddresseeButton.click();
		}
	}
}
