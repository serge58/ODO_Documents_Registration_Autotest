package tests.run.incoming.pages;

import static com.codeborne.selenide.Condition.*;

import static com.codeborne.selenide.Selectors.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import static common.utilities.Tools.*;

import static tests.commons.pageobjects.Common.CheckGridCheckbox;
import static tests.commons.pageobjects.Common.SelectFromDropDown;
import static tests.commons.pageobjects.Common.setCheckbox;
import static tests.commons.pageobjects.UPD.GetDepartmentShortName;
import static tests.commons.pageobjects.UPD.SelectDepartmentFromWindowTreeList;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Reporter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import common.utilities.Tools;

public class IncomingMainPage {
	static SelenideElement regCard;
	static ElementsCollection cardFields;
	static List<String> personData;
	
	static HashMap<String, String> regCardFieldsSaved;
	
	static SelenideElement cardsPanel = $$(byClassName("docflow-list")).find(visible);
	static ElementsCollection cardsList = cardsPanel.$$(byClassName("x-dataview-item"));

	static String reportedTo;
	
	public static void CheckPrefilledDocFields(SelenideElement regCard_, 
									  HashMap<String, String> docFields,
									  String lastCaseNum,
									  String worker,
									  String workerDep,
									  String receiver,
									  String receiverDep,
									  String depShortName,
									  String receiverDepShortName) {
		regCard = regCard_;
		cardFields = regCard.$$(byClassName("x-form-item")).exclude(hidden);
		
		Reporter.log("Проверяем дату регистрации<br>");
		
		cardFields.find(text("Дата регистрации:")).$(byName("regDate")).shouldHave(value(getNowLongDate()));
		
		Reporter.log("Проверяем Тип документа<br>");
		cardFields.find(text("Тип документа")).$(byName("docType")).shouldHave(value(docFields.get("Тип документа")));
		
		Reporter.log("Проверяем Индекс субъекта РФ<br>");
		cardFields.find(text("Индекс субъекта РФ")).$(byName("region")).shouldHave(value(docFields.get("Индекс субъекта РФ").split(" - ")[1]));
		
		Reporter.log("Проверяем Краткое содержание<br>");
		cardFields.find(text("Краткое содержание")).$(byName("subject")).shouldHave(value(docFields.get("Краткое содержание")));
		
		Reporter.log("Проверяем Листов/томов<br>");
		cardFields.find(text("Листов/томов")).$(byName("volNumber")).shouldHave(value(docFields.get("Листов/томов")));
		
		Reporter.log("Проверяем Приложение<br>");
		cardFields.find(text("Приложение")).$(byName("application")).shouldHave(value(docFields.get("Приложение")));
		
		Reporter.log("Проверяем Примечание<br>");
		cardFields.find(text("Примечание")).$(byName("comments")).shouldHave(value(docFields.get("Примечание")));
		
		Reporter.log("Проверяем общие данные внизу карточки<br>");
		SelenideElement summaryField = cardFields.find(text("Учётный номер"));
		summaryField.shouldHave(text(lastCaseNum));
		summaryField.shouldHave(text("от " + getNowLongDate()));
		
		summaryField.shouldHave(text("принял: " + worker + ", " + depShortName));
		
		summaryField = cardFields.find(text("Подразделение:"));
		summaryField.shouldHave(text(receiverDep));
		
		summaryField = cardFields.find(text("Регистратор документа:"));
		summaryField.shouldHave(text(receiver + ", " + receiverDepShortName));
	}
	
	public static void FillDocFields(HashMap<String, String> regCardFields, boolean edit) {
		regCardFieldsSaved = regCardFields;
		
		if (edit)
			regCard = $$(byClassName("x-window")).find(Condition.text("Редактирование регистрационной карточки"));
		else
			regCard = $$(byClassName("x-window")).find(Condition.text("Регистрационная карточка входящего документа"));
		
		cardFields = regCard.$$(byClassName("x-field"));
		
		boolean setDSP = regCardFields.get("ДСП").contentEquals("Да") ? true : false;
		
		/*if (edit) {
			Reporter.log("Проверяем флажок ДСП, если редактирование он должен быть недоступен<br>");
			SelenideElement dsp = $$(byClassName("x-form-type-checkbox")).first();
			dsp.shouldHave(cssClass("x-item-disabled"));
		} else {
			Reporter.log("Устанавливаем флажок ДСП<br>");
			setCheckbox(cardFields.find(text("ДСП")), setDSP);
			
			Reporter.log("Проверяем что если флажок 'ДСП' установлен, 'Конфиденциальный' должен быть установлен тоже<br>");
			if (setDSP)
				cardFields.find(text("Конфиденциальный")).shouldHave(cssClass("x-form-cb-checked"));
		}*/
		
		Reporter.log("Устанавливаем флажок ДСП<br>");
		setCheckbox(cardFields.find(text("ДСП")), setDSP);
		
		Reporter.log("Проверяем что если флажок 'ДСП' установлен, 'Конфиденциальный' должен быть установлен тоже<br>");
		if (setDSP)
			cardFields.find(text("Конфиденциальный")).shouldHave(cssClass("x-form-cb-checked"));
		
		Reporter.log("Устанавливаем флажок Конфиденциальный<br>");
		setCheckbox(cardFields.find(text("Конфиденциальный")), regCardFields.get("Конфиденциальный").contentEquals("Да") ? true : false);
		
		Reporter.log("Выбираем Тип документа<br>");
		SelectFromDropDown(cardFields.find(text("Тип документа")), regCardFields.get("Тип документа"));
		
		Reporter.log("Вводим Индекс субъекта РФ<br>");
		SelectFromDropDown(cardFields.find(text("Индекс субъекта РФ")), regCardFields.get("Индекс субъекта РФ"));
		
		Reporter.log("Вводим Краткое содержание<br>");
		cardFields.find(text("Краткое содержание")).$(byName("subject")).setValue(regCardFields.get("Краткое содержание"));
		
		Reporter.log("Вводим Листов/томов<br>");
		cardFields.find(text("Листов/томов")).$(byName("volNumber")).setValue(regCardFields.get("Листов/томов"));
		
		Reporter.log("Вводим Приложение<br>");
		cardFields.find(text("Приложение")).$(byName("application")).setValue(regCardFields.get("Приложение"));
		
		if (!edit) {
			Reporter.log("Вводим Номер Наряда<br>");
			SelectFromDropDown(cardFields.find(text("Номер наряда")), regCardFields.get("Номер наряда"));
		}
		
		Reporter.log("Вводим Том наряда<br>");
		cardFields.find(text("Том наряда")).$(byName("ordervolNum")).setValue(regCardFields.get("Том наряда"));
		
		Reporter.log("Вводим Страница тома<br>");
		cardFields.find(text("Страница тома")).$(byName("volpageNum")).setValue(regCardFields.get("Страница тома"));
		
		Reporter.log("Вводим Примечание<br>");
		cardFields.find(text("Примечание")).$(byName("comments")).setValue(regCardFields.get("Примечание"));

		Reporter.log("Выбираем Доложен<br>");
		String worker = SelectWorker(cardFields, "Доложен", regCardFields.get("Доложен"));
		regCardFieldsSaved.replace("Доложен", worker);
		
		reportedTo = cardFields.find(text("Доложен")).$(byName("reportedTo")).getValue();
		
		Reporter.log("Вводим Дата доклада<br>");
		cardFields.find(text("Дата доклада")).$(byName("reportDate")).setValue(regCardFields.get("Дата доклада"));
	}
	
	public static SelenideElement FillRequiredDocFields(HashMap<String, String> regCardFields, boolean edit) {
		regCardFieldsSaved = regCardFields;
		
		if (edit)
			regCard = $$(byClassName("x-window")).find(Condition.text("Редактирование регистрационной карточки"));
		else
			regCard = $$(byClassName("x-window")).find(Condition.text("Регистрационная карточка входящего документа"));
		
		cardFields = regCard.$$(byClassName("x-field"));
		
		Reporter.log("Выбираем Тип документа<br>");
		SelectFromDropDown(cardFields.find(text("Тип документа")), regCardFields.get("Тип документа"));
		
		Reporter.log("Вводим Краткое содержание<br>");
		cardFields.find(text("Краткое содержание")).$(byName("subject")).setValue(regCardFields.get("Краткое содержание"));

		return regCard;
	}
	
	public static void CheckFinalDocFields(List<String> oldCardParams, String lastCaseNum, 
			String registrationDateTime, List<String> accauntCardInfo, String receiver, String receiverDep, String receiverDepShortName, List<String> personData) {
		
		String PROPERTY_NAME = generateRandomSymbolsRU(10);
		String PROPERTY_VALUE = generateRandomSymbolsRU(30);
		
		Reporter.log("<b>Подготавливаем все необходимые элементы для проверки</b><br>");
		
		Reporter.log("Панель со списком карточек<br>");
		//SelenideElement cardsSearch = cardsPanel.$$(byClassName("x-toolbar-docked-top")).first();
		//SelenideElement cardsFilter = cardsPanel.$$(byClassName("x-toolbar-docked-top")).last();
		//SelenideElement cardsPaging = cardsPanel.$(byClassName("x-toolbar-docked-bottom"));
		
		int prevCardNumber = IncomingMainPage.GetPrevCardNumber(oldCardParams.get(0));
		
		String newCardNumber = regCardFieldsSaved.get("ДСП").contentEquals("Да") ? "ДСП-" : "";
		newCardNumber += prevCardNumber + 1;
		newCardNumber += "/" + getShortYear() + "-";
		newCardNumber += regCardFieldsSaved.get("Номер наряда");
		
		SelenideElement lastCard = cardsList.get(0);
		
		SelenideElement cardData = lastCard.$(byClassName("state"));
		cardData.shouldHave(text("Зарегистрирован"));
		
		cardData = lastCard.$(byClassName("number"));
		
		String cardNumberWithDate = newCardNumber + " от " + getNowLongDate();
		cardData.shouldHave(text(cardNumberWithDate));
		
		System.setProperty("cardNumber", cardNumberWithDate);
		
		cardData = lastCard.$(byClassName("type"));
		cardData.shouldHave(text(regCardFieldsSaved.get("Тип документа")));
		
		//cardData = cardsList.get(0).$(byClassName("description"));
		//cardData.shouldHave(text(regCardFieldsSaved.get("Краткое содержание").replace("\n", "")));
		
		Reporter.log("Центральная панель с деревом объектов<br>");
		SelenideElement treePanel = $$(byClassName("x-tree-panel")).find(visible);
		//SelenideElement treeViewToolbar = treePanel.$(byClassName("x-toolbar-docked-top"));
		ElementsCollection treeView = treePanel.$$(By.tagName("table"));
		treeView.get(0).shouldHave(text(newCardNumber));
		treeView.get(GetTreeItemIndex(treeView, "Учётные карточки") + 1).shouldHave(text(lastCaseNum));
		
		Reporter.log("Сам документ с верхней панелью инструментов<br>");
		SelenideElement cardInfoToolbar = $$(byClassName("x-toolbar-docked-top")).exclude(hidden).last();
		ElementsCollection cardInfo = GetCardInfo();
		
		ElementsCollection cardInfoMainFields = cardInfo.find(text("Основная информация")).$$(By.tagName("tr"));
		SelenideElement cardInfoCorrespondent = cardInfo.find(text("Корреспонденты"));
		SelenideElement cardInfoAdditional = cardInfo.find(text("Дополнительная информация"));
		SelenideElement cardInfoHistory = cardInfo.find(text("История обработки"));
		
		Reporter.log("Проверяем поля документа<br>");
		Reporter.log("Основная информация<br>");
		
		SelenideElement field = cardInfoMainFields.get(0);
		field.shouldHave(text("Регистрационный номер"));
		field.shouldHave(text(newCardNumber));
		
		field = cardInfoMainFields.get(1);
		field.shouldHave(text("Дата регистрации"));
		field.shouldHave(text(Tools.getLongDate(registrationDateTime)));
		
		field = cardInfoMainFields.get(2);
		field.shouldHave(text("Для служебного пользования"));
		field.shouldHave(text(regCardFieldsSaved.get("ДСП")));
		
		field = cardInfoMainFields.get(3);
		field.shouldHave(text("Конфиденциальный"));
		field.shouldHave(text(regCardFieldsSaved.get("Конфиденциальный")));
		
		field = cardInfoMainFields.get(4);
		field.shouldHave(text("Тип документа"));
		field.shouldHave(text(regCardFieldsSaved.get("Тип документа")));
		
		field = cardInfoMainFields.get(5);
		field.shouldHave(text("Индекс субъекта РФ"));
		field.shouldHave(text(regCardFieldsSaved.get("Индекс субъекта РФ")));
		
		field = cardInfoMainFields.get(6);
		field.shouldHave(text("Краткое содержание"));
		field.shouldHave(text(regCardFieldsSaved.get("Краткое содержание")));
		
		field = cardInfoMainFields.get(7);
		field.shouldHave(text("Листов/томов"));
		field.shouldHave(text(regCardFieldsSaved.get("Листов/томов")));
		
		field = cardInfoMainFields.get(8);
		field.shouldHave(text("Приложение"));
		field.shouldHave(text(regCardFieldsSaved.get("Приложение")));
		
		field = cardInfoMainFields.get(9);
		field.shouldHave(text("Номер наряда"));
		field.shouldHave(text(regCardFieldsSaved.get("Номер наряда")));
		
		field = cardInfoMainFields.get(10);
		field.shouldHave(text("Том наряда"));
		field.shouldHave(text(regCardFieldsSaved.get("Том наряда")));
		
		field = cardInfoMainFields.get(11);
		field.shouldHave(text("Страница тома"));
		field.shouldHave(text(regCardFieldsSaved.get("Страница тома")));
		
		field = cardInfoMainFields.get(12);
		field.shouldHave(text("Примечание"));
		field.shouldHave(text(regCardFieldsSaved.get("Примечание")));
		
		field = cardInfoMainFields.get(13);
		field.shouldHave(text("Доложен"));
		field.shouldHave(text(reportedTo.replace(" (", ", ").replace(")", "")));
		
		field = cardInfoMainFields.get(14);
		field.shouldHave(text("Дата доклада"));
		field.shouldHave(text(regCardFieldsSaved.get("Дата доклада")));
		
		field = cardInfoMainFields.get(15);
		field.shouldHave(text("Учётная карточка"));
		String accountCard = lastCaseNum + " от " + accauntCardInfo.get(2) + ". Принял: " + accauntCardInfo.get(3) + ", " + accauntCardInfo.get(0);
		field.shouldHave(text(accountCard));
		
		field = cardInfoMainFields.get(16);
		field.shouldHave(text("Подразделение"));
		field.shouldHave(text(receiverDep));
		
		field = cardInfoMainFields.get(17);
		field.shouldHave(text("Регистратор документа"));
		field.shouldHave(text(receiver + ", " + receiverDepShortName));
		
		Reporter.log("Корреспонденты<br>");
		ElementsCollection columnHeaders = cardInfoCorrespondent.$$(byClassName("x-column-header"));
		columnHeaders.get(0).shouldHave(text("Корреспондент"));
		//columnHeaders.get(1).shouldHave(text("Должность (представительство)"));
		//columnHeaders.get(2).shouldHave(text("Учреждение"));
		columnHeaders.get(1).shouldHave(text("Исх. номер"));
		columnHeaders.get(2).shouldHave(text("Исх. дата"));
		columnHeaders.get(3).shouldHave(text("Осн."));
		
		ElementsCollection rows = cardInfoCorrespondent.$$(By.tagName("table"));
		ElementsCollection corrColumns = rows.first().$$(By.tagName("td"));
		corrColumns.get(0).shouldHave(text(personData.get(0)));
		
		Reporter.log("Дополнительная информация<br>");
		Reporter.log("Добавляем новое свойство<br>");
		cardInfoToolbar.$(byText("Свойства")).click();
		SelenideElement properties = $$(byClassName("x-box-target")).exclude(hidden).last();
		properties.$(byText("Добавить свойство")).click();
		SelenideElement propertiesWindow = $$(byClassName("x-window")).find(text("Дополнительное свойство"));
		field = propertiesWindow.$$(byClassName("x-field")).find(text("Название *:"));
		field.$(byName("title")).setValue(PROPERTY_NAME);
		
		field = propertiesWindow.$$(byClassName("x-field")).find(text("Значение *:"));
		field.$(byName("valString")).setValue(PROPERTY_VALUE);
		
		propertiesWindow.$(byText("Сохранить")).click();
		
		Reporter.log("Проверяем что новое свойство добавилось<br>");
		rows = cardInfoAdditional.$$(By.tagName("table")).first().$$(By.tagName("td"));
		rows.get(0).shouldHave(text(PROPERTY_NAME));
		rows.get(1).shouldHave(text(PROPERTY_VALUE));
		rows.get(2).shouldHave(text(getNowLongDate()));
		rows.get(3).shouldHave(text(receiver));
		
		Reporter.log("История обработки<br>");
		rows = cardInfoHistory.$$(By.tagName("table")).first().$$(By.tagName("td"));
		rows.get(0).shouldHave(text(getNowLongDate()));
		rows.get(1).shouldHave(text("Зарегистрирован"));
		rows.get(2).shouldHave(text(receiver + ", " + receiverDepShortName));
	}
	
	public static void CheckEditedDocFields(HashMap<String, String> editCardFields, SelenideElement registeredDoc, List<String> personData) {
		Reporter.log("Панель со списком карточек<br>");

		SelenideElement cardData = registeredDoc.$(byClassName("state"));
		cardData.shouldHave(text("Зарегистрирован"));
		
		//cardData = registeredDoc.$(byClassName("description"));
		//cardData.shouldHave(text(editCardFields.get("Краткое содержание")));
		
		Reporter.log("Сам документ с верхней панелью инструментов<br>");
		ElementsCollection cardInfo = GetCardInfo();
		ElementsCollection cardInfoMainFields = cardInfo.find(text("Основная информация")).$$(By.tagName("tr"));
		SelenideElement cardInfoCorrespondent = cardInfo.find(text("Корреспонденты"));
		
		Reporter.log("Проверяем поля документа<br>");
		Reporter.log("Основная информация<br>");
		
		SelenideElement field = cardInfoMainFields.find(text("Конфиденциальный"));
		field.shouldHave(text(editCardFields.get("Конфиденциальный")));
		
		field = cardInfoMainFields.find(text("Тип документа"));
		field.shouldHave(text(editCardFields.get("Тип документа")));
		
		field = cardInfoMainFields.find(text("Индекс субъекта РФ"));
		field.shouldHave(text(editCardFields.get("Индекс субъекта РФ")));
		
		field = cardInfoMainFields.find(text("Краткое содержание"));
		field.shouldHave(text(editCardFields.get("Краткое содержание")));
		
		field = cardInfoMainFields.find(text("Листов/томов"));
		field.shouldHave(text(editCardFields.get("Листов/томов")));
		
		field = cardInfoMainFields.find(text("Приложение"));
		field.shouldHave(text(editCardFields.get("Приложение")));
		
		field = cardInfoMainFields.find(text("Том наряда"));
		field.shouldHave(text(editCardFields.get("Том наряда")));
		
		field = cardInfoMainFields.find(text("Страница тома"));
		field.shouldHave(text(editCardFields.get("Страница тома")));
		
		field = cardInfoMainFields.find(text("Примечание"));
		field.shouldHave(text(editCardFields.get("Примечание")));
		
		field = cardInfoMainFields.find(text("Доложен"));
		field.shouldHave(text(reportedTo.replace(" (", ", ").replace(")", "")));
		
		field = cardInfoMainFields.find(text("Дата доклада"));
		field.shouldHave(text(regCardFieldsSaved.get("Дата доклада")));
		
		Reporter.log("Проверяем что добавленный корреспондент есть в списке<br>");
		ElementsCollection rows = cardInfoCorrespondent.$$(By.tagName("table"));
		SelenideElement correspondent = rows.find(text(personData.get(0).trim()));
		correspondent.should(exist);
	}
	
	public static void CheckEditedRegCardFields(HashMap<String, String> editCardFields) {
		Reporter.log("Находим учётную карточку и открываем её<br>");
		
		ElementsCollection treeView = $$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table"));
		treeView.get(GetTreeItemIndex(treeView, "Учётные карточки") + 1).click();
		
		Reporter.log("Начинаем проверять карточку<br>");
		ElementsCollection card = $$(byClassName("x-autocontainer-innerCt")).find(visible).$$(byClassName("x-panel")).filter(visible);
		
		Reporter.log("Проверяем основную информацию<br>");
		ElementsCollection mainInfo = card.findBy(text("ОСНОВНАЯ ИНФОРМАЦИЯ")).$$(By.tagName("tr"));
		mainInfo.get(0).$$(By.tagName("td")).get(0).shouldHave(text("Тип документа"));
		mainInfo.get(0).$$(By.tagName("td")).get(1).shouldHave(text(editCardFields.get("Тип документа")));
		
		mainInfo.get(4).$$(By.tagName("td")).get(0).shouldHave(text("Индекс субъекта РФ"));
		mainInfo.get(4).$$(By.tagName("td")).get(1).shouldHave(text(editCardFields.get("Индекс субъекта РФ")));
		
		mainInfo.get(7).$$(By.tagName("td")).get(0).shouldHave(text("Примечание")); 
		mainInfo.get(7).$$(By.tagName("td")).get(1).shouldHave(text(editCardFields.get("Примечание")));
		
		mainInfo.get(8).$$(By.tagName("td")).get(0).shouldHave(text("Приложение"));
		mainInfo.get(8).$$(By.tagName("td")).get(1).shouldHave(text(editCardFields.get("Приложение")));
		
		mainInfo.get(10).$$(By.tagName("td")).get(0).shouldHave(text("Листов/томов"));
		mainInfo.get(10).$$(By.tagName("td")).get(1).shouldHave(text(editCardFields.get("Листов/томов")));
		
		Reporter.log("Проверяем дополнительную информацию<br>");
		ElementsCollection additionInfo = card.findBy(text("ДОПОЛНИТЕЛЬНАЯ ИНФОРМАЦИЯ")).$$(By.tagName("tr"));
		
		additionInfo.get(5).$$(By.tagName("td")).get(0).shouldHave(text("Краткое содержание"));
		additionInfo.get(5).$$(By.tagName("td")).get(1).shouldHave(text(editCardFields.get("Краткое содержание")));
	}
	
	public static void CheckHistoryProcessing(String worker, String workerDep, String registrationDateTime) {
		String workerDepShortName = GetDepartmentShortName(workerDep);
		
		ElementsCollection cardInfo = GetCardInfo();
		
		Reporter.log("Проверяем Историю обработки<br>");
		ElementsCollection rows = cardInfo.find(text("История обработки")).$$(By.tagName("table")).last().$$(By.tagName("td"));
		rows.get(0).shouldHave(text(registrationDateTime));
		rows.get(1).shouldHave(text("Зарегистрирован"));
		rows.get(2).shouldHave(text(worker + ", " + workerDepShortName));
	}
	
	static SelenideElement resolutionWindow = $$(byClassName("x-window")).find(text("Резолюция"));
	static ElementsCollection mainFields = resolutionWindow.$$(byClassName("x-field")).exclude(hidden);
	static ElementsCollection treeViewItems = $$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table"));
	
	public static void CheckResolutionAutor(String resolutionAutor) {
		mainFields.find(text("Автор")).$(byName("profileId")).shouldHave(value(resolutionAutor));
	}
	
	private static String autor;
	private static String controller;
	private static String executor;
	
	private static String createdDate;
	
	private static HashMap<String, String> resolutionFieldsSaved;
	
	public static void FillResolutionFields(HashMap<String, String> resolutionFields) {
		resolutionFieldsSaved = resolutionFields;
		
		Reporter.log("<b>Заполняем основные поля</b><br>");
		
		Reporter.log("Заполняем поле Название<br>");
		mainFields.find(text("Название")).$(byName("content")).setValue(resolutionFields.get("Название"));
		
		Reporter.log("Заполняем поле Автор<br>");
		
		SelectWorker(mainFields, "Автор", resolutionFields.get("Автор"));
		autor = mainFields.find(text("Автор")).$(byName("profileId")).getValue();
		
		Reporter.log("Заполняем поле Дата Создания<br>");
		SelenideElement date = mainFields.find(text("Дата создания")).$(byName("objDate"));
		
		//Reporter.log("Пооверяем что поле Дата заполнено по умолчанию сегодняшним числом<br>");
		//date.shouldHave(value(getNowLongDate()));
		
		Reporter.log("Устанавливаем дату создания<br>");
		date.setValue(resolutionFields.get("Дата создания"));
		
		Reporter.log("Заполняем поля Пункта 1<br>");
		
		Reporter.log("Выбираем На исполнение/На ознакомление<br>");
		SelenideElement forExecution = $(byClassName("x-form-checkboxgroup"));
		SetRadioButton(forExecution, resolutionFields.get("Куда"));
		
		Reporter.log("Заполняем поля Контроль исполнения<br>");
		boolean executionControl = resolutionFields.get("Контроль исполнения").contains("Да") ? true : false;
		if (executionControl) {
			SelenideElement exeControlChk = resolutionWindow.$(byClassName("x-form-type-checkbox"));
			setCheckbox(exeControlChk, executionControl);
			
			Reporter.log("Заполняем поле Контролёр<br>");
			SelectWorker(mainFields, "Контролёр", resolutionFields.get("Контролёр"));
			controller = mainFields.find(text("Контролёр")).$(byName("controller")).getValue();
			
			Reporter.log("Заполняем поле Дата контроля<br>");
			mainFields.find(text("Дата контроля")).$(byName("dateControl")).setValue(resolutionFields.get("Дата контроля"));
		}
				
		Reporter.log("<b>Создаем поручение</b><br>");
		SelenideElement delegateGrid = resolutionWindow.$(byClassName("x-grid"));
		SelenideElement createDelegateButton = delegateGrid.$(byText("Создать поручение"));
		createDelegateButton.click();
		
		SelenideElement addDelegateWindow = $$(byClassName("x-window")).find(text("Поручение ответственного исполнителя пункта резолюции"));
		ElementsCollection delegateFields = addDelegateWindow.$$(byClassName("x-field")).exclude(hidden);
		
		Reporter.log("Заполняем поле Исполнитель<br>");
		SelectWorker(delegateFields, "Исполнитель", resolutionFields.get("Исполнитель"));
		executor = delegateFields.find(text("Исполнитель")).$(byName("executor")).getValue();
		
		Reporter.log("Заполняем поле Срок исполнения<br>");
		delegateFields.find(text("Срок исполнения")).$(byName("objDate")).setValue(resolutionFields.get("Срок исполнения"));
		
		Reporter.log("Устанавливаем чекбокс 'Поручение ответственного исполнителя пункта резолюции'<br>");
		setCheckbox(addDelegateWindow.$(byClassName("x-form-type-checkbox")), resolutionFields.get("Поручение исполнителя").contains("Да") ? true : false);
		
		Reporter.log("Заполняем поле Текст поручения<br>");
		delegateFields.find(text("Текст поручения")).$(byName("content")).setValue(resolutionFields.get("Текст поручения"));
		
		Reporter.log("Сохраняем поручение<br>");
		addDelegateWindow.$(byText("Сохранить")).click();
		
		Reporter.log("Проверяем таблицу с новым поручением<br>");
		ElementsCollection LastGridRow = delegateGrid.$$(By.tagName("table")).first().$$(By.tagName("td"));
		
		Reporter.log("Исполнитель<br>");
		LastGridRow.get(0).shouldHave(text(resolutionFields.get("Исполнитель")));
		
		Reporter.log("Срок исполнения<br>");
		LastGridRow.get(1).shouldHave(text(resolutionFields.get("Срок исполнения")));
		
		Reporter.log("Отв.<br>");
		SelenideElement checkColumn = LastGridRow.get(2).$(byClassName("x-grid-checkcolumn"));
		CheckGridCheckbox(checkColumn, resolutionFields.get("Поручение исполнителя").contains("Да") ? true : false);
		
		Reporter.log("Текст поручения<br>");
		LastGridRow.get(3).shouldHave(text(resolutionFields.get("Текст поручения")));
		
		Reporter.log("Сохраняем резолюцию<br>");
		resolutionWindow.$(byText("Сохранить")).click();
		createdDate = Tools.getNowLongDate();
	}
	
	public static void CheckResolutionFields(String worker, String workerDepShortName) {
		SelenideElement treePanel = $$(byClassName("x-panel")).exclude(hidden).get(10);
				
		Reporter.log("Проверяем Автора и Название резолюции в дереве объектов на центральной панели<br>");
		SelenideElement resolution = treeViewItems.get(GetTreeItemIndex(treeViewItems, "Резолюции") + 1);
		resolution.click();
		
		autor = autor.substring(0, autor.indexOf("(") - 1) + ", " + autor.substring(autor.indexOf("(")).replaceAll("[()]", "");
		executor = executor.substring(0, executor.indexOf("(") - 1) + ", " + executor.substring(executor.indexOf("(")).replaceAll("[()]", "");
		
		String autorTitle = autor + ": " + resolutionFieldsSaved.get("Название");
		
		resolution.shouldHave(text(autorTitle));
		
		Reporter.log("<b>Проверяем правильность заполнения всех полей резолюции</b><br>");
		
		Reporter.log("Проверяем заголовки<br>");
		ElementsCollection resolutionHeaders = treePanel.$$(byClassName("x-column-header"));
		resolutionHeaders.get(0).shouldHave(text("Название"));
		resolutionHeaders.get(1).shouldHave(text("Поручение"));
		resolutionHeaders.get(2).shouldHave(text("Исполнитель"));
		resolutionHeaders.get(3).shouldHave(text("Срок"));
		resolutionHeaders.get(4).shouldHave(text("Состояние"));
		
		Reporter.log("Проверяем поля резолюции<br>");
		ElementsCollection resolutionRows = treePanel.$$(By.tagName("table"));
		
		Reporter.log("Проверяем первую строку с автором и названием резолюции<br>");
		ElementsCollection resolutionColumn = resolutionRows.get(0).$$(By.tagName("td"));
		resolutionColumn.get(0).shouldHave(text(autorTitle));
		resolutionColumn.get(1).shouldHave(text(""));
		resolutionColumn.get(2).shouldHave(text(autor));
		resolutionColumn.get(3).shouldHave(text(resolutionFieldsSaved.get("Дата создания")));
		resolutionColumn.get(4).shouldHave(text("Не начата"));
		
		Reporter.log("Проверяем вторую строку 'Пункт 1'<br>");
		resolutionColumn = resolutionRows.get(1).$$(By.tagName("td"));
		resolutionColumn.get(0).shouldHave(text("Пункт 1"));
		resolutionColumn.get(1).shouldHave(text(resolutionFieldsSaved.get("Куда").contains("На исполнение") ? "Исполнение": "Ознакомление"));
		
		boolean executionControl = resolutionFieldsSaved.get("Контроль исполнения").contains("Да") ? true : false;
		if (executionControl) {
			controller = controller.substring(0, controller.indexOf("(") - 1) + ", " + controller.substring(controller.indexOf("(")).replaceAll("[()]", "");
			resolutionColumn.get(2).shouldHave(text(controller));
		}
		
		resolutionColumn.get(3).shouldHave(text(resolutionFieldsSaved.get("Дата контроля")));
		resolutionColumn.get(4).shouldHave(text("Не начат"));
		
		Reporter.log("Проверяем третью строку 'Поручение'<br>");
		resolutionColumn = resolutionRows.get(2).$$(By.tagName("td"));
		resolutionColumn.get(0).shouldHave(text(resolutionFieldsSaved.get("Текст поручения")));
		resolutionColumn.get(1).shouldHave(text(""));
		resolutionColumn.get(2).shouldHave(text(executor));
		resolutionColumn.get(3).shouldHave(text(resolutionFieldsSaved.get("Срок исполнения")));
		resolutionColumn.get(4).shouldHave(text("Не начато"));
		
		Reporter.log("Проверяем 'Ход исполнения'<br>");
		ElementsCollection executionCourse = $$(byClassName("x-panel")).exclude(hidden).get(11).$$(By.tagName("table"));
		ElementsCollection executionColumns = executionCourse.first().$$(By.tagName("td"));
		executionColumns.get(0).shouldHave(text(createdDate));
		executionColumns.get(1).shouldHave(text(worker + ", " + workerDepShortName));
		executionColumns.get(2).shouldHave(text("Не начата"));
	}
	
	public static void TransferDocumentToDepartment(String workerDep) {
		String workerDepShortName = GetDepartmentShortName(workerDep);
		
		ElementsCollection treeView = $$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table"));
		treeView.get(GetTreeItemIndex(treeView, "Учётные карточки") + 1).click();
		
		Reporter.log("Проверяем журнал передач<br>");
		ElementsCollection card = $$(byClassName("x-autocontainer-innerCt")).find(visible).$$(byClassName("x-panel")).filter(visible);
		SelenideElement transmissionJournal = card.findBy(text("ЖУРНАЛ ПЕРЕДАЧ"));
		
		ElementsCollection transmissionJournalTable = transmissionJournal.$$(By.tagName("tr"));
		
		if (transmissionJournal.exists()) {
			ElementsCollection transmission = transmissionJournalTable.get(0).$$(By.tagName("td"));
			if (!transmission.get(4).text().isEmpty() & !transmission.get(4).has(text(workerDepShortName))) {
				RedirectDocument(workerDep);
			} else if (transmission.get(4).text().isEmpty()) {
				TransferDocument(workerDep);
			}
		} else {
			TransferDocument(workerDep);
		}
	}
	
	private static void TransferDocument(String workerDep) {
		$(byText("Прием и передача")).click();
		$$(byClassName("x-menu")).find(visible).$(byText("Передать")).click();
		
		SelenideElement redirectDocumentWindow = $$(byClassName("x-window")).filter(visible).find(text("Передать документ"));
		SelectDepartmentFromWindowTreeList(redirectDocumentWindow.$$(By.tagName("table")), workerDep);
		
		redirectDocumentWindow.$(byText("Передать")).click();
	}
	
	private static void RedirectDocument(String workerDep) {
		$(byText("Прием и передача")).click();
		$$(byClassName("x-menu")).find(visible).$(byText("Перенаправить")).click();
		
		SelenideElement redirectDocumentWindow = $$(byClassName("x-window")).filter(visible).find(text("Перенаправить документ"));
		SelectDepartmentFromWindowTreeList(redirectDocumentWindow.$$(By.tagName("table")), workerDep);
		
		redirectDocumentWindow.$(byText("Перенаправить")).click();
	}
	
	public static String RecallDocument() {
		String recallDateTime;
		
		String COMMENT = "Комментарий 12312уйвычсыые4564";
		
		Reporter.log("Кликаем по кнопке 'Документ'<br>");
		$(byText("Документ")).click();
		
		Reporter.log("Кликаем по кнопке 'Отозвать'<br>");
		$(byText("Отозвать")).click();
		
		Reporter.log("Находим окно 'Отзыв документа'<br>");
		SelenideElement recallDocWindow = $$(byClassName("x-window")).find(text("Отзыв документа")); 
		
		Reporter.log("Проверяем текст лейбла<br>");
		SelenideElement label = recallDocWindow.$(By.tagName("label"));
		label.shouldHave(text("Вы действительно хотите отозвать документ с регистрации?\r\n" + 
				"После отзыва документа его изменение будет невозможно, все поручения по документу будут также отозваны."));
		
		Reporter.log("Заполняем поле комментария<br>");
		SelenideElement field = recallDocWindow.$$(byClassName("x-field")).find(text("Комментарий *:"));
		field.$(byName("comment")).setValue(COMMENT);
		
		Reporter.log("Нажимаем кнопку 'Отозвать'<br>");
		recallDateTime = getNowLongDate();
		recallDocWindow.$(byText("Отозвать")).click();
		
		return recallDateTime;
	}
	
	public static SelenideElement FindRegisteredDoc(String docNum, String status) {
		SelenideElement registeredDoc;
		
		if (docNum.isEmpty() & status.isEmpty())
			registeredDoc = cardsList.find(text("Зарегистрирован"));
		else if (!docNum.isEmpty())
			registeredDoc = cardsList.find(text(docNum));
		else 
			registeredDoc = cardsList.find(text(status));
		
		if (registeredDoc.exists())
			registeredDoc.click();
		
		return registeredDoc;
	}
	
	private static int GetPrevCardNumber(String prevCardData) {
		int ret = 0;
		String regData = prevCardData.split("\n")[1].split("/")[0];
		
		if (regData.contains("-"))
			ret = Integer.parseInt(regData.substring(regData.indexOf("-") + 1));
		else 
			ret = Integer.parseInt(regData);
		
		return ret;
	}
	
	public static int GetTreeItemIndex(ElementsCollection tree, String nodeName) {
		int index = 0;
		
		for (int i = 0; i < tree.size(); i++) {
			if (tree.get(i).has(text(nodeName))) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public static String GetCaseNumber() {
		SelenideElement treePanel = $$(byClassName("x-tree-panel")).find(visible);
		ElementsCollection treeView = treePanel.$$(By.tagName("table"));
		String docNum = treeView.get(GetTreeItemIndex(treeView, "Учётные карточки") + 1).getText().substring(1);
		
		return docNum;
	}
	
	public static String GetRegNumber() {
		String regDocNumber;
		
		$$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table")).first().click();
		
		ElementsCollection cardInfo = GetCardInfo();
		ElementsCollection cardInfoMainFields = cardInfo.find(text("Основная информация")).$$(By.tagName("tr"));
		
		regDocNumber = cardInfoMainFields.find(text("Регистрационный номер")).$$(By.tagName("td")).get(1).getText();
		
		return regDocNumber;
	}
	
	public static String GetRegDocNumDate() {
		String regDocNumDateTime;
		
		$$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table")).first().click();
		
		ElementsCollection cardInfo = GetCardInfo();
		ElementsCollection cardInfoMainFields = cardInfo.find(text("Основная информация")).$$(By.tagName("tr"));
		
		regDocNumDateTime = cardInfoMainFields.find(text("Регистрационный номер")).$$(By.tagName("td")).get(1).getText() + " от ";
		regDocNumDateTime += cardInfoMainFields.find(text("Дата регистрации")).$$(By.tagName("td")).get(1).getText().split(" ")[0];
		
		return regDocNumDateTime;	
	}
	
	public static String GetRegDocNumDateTime() {
		String regDocNumDateTime;
		
		$$(byClassName("x-tree-panel")).find(visible).$$(By.tagName("table")).first().click();
		
		ElementsCollection cardInfo = GetCardInfo();
		ElementsCollection cardInfoMainFields = cardInfo.find(text("Основная информация")).$$(By.tagName("tr"));
		
		regDocNumDateTime = cardInfoMainFields.find(text("Регистрационный номер")).$$(By.tagName("td")).get(1).getText() + " от ";
		regDocNumDateTime += cardInfoMainFields.find(text("Дата регистрации")).$$(By.tagName("td")).get(1).getText();
		
		return regDocNumDateTime;	
	}
	
	private static void SetRadioButton(SelenideElement forExecution, String radio) {
		forExecution.$(byText(radio)).click();
	}
	
	private static String SelectWorker(ElementsCollection fieldsList, String fieldName, String workerName) {
		fieldsList.find(text(fieldName)).$(byClassName("x-form-search-trigger")).click();
		SelenideElement selectControllerWindow = $$(byClassName("x-window")).find(Condition.text("Выбор работников"));
		SelenideElement worker;
		String returnName;
		
		ElementsCollection workers = selectControllerWindow.$$(By.tagName("table"));
		
		if (workerName.isEmpty()) {
			worker = workers.get(Tools.getRandomInt(workers.size()));
		}
		else {
			worker = workers.find(text(workerName));
		}
		
		worker.click();
		returnName = worker.getValue();

		selectControllerWindow.$(byText("Выбрать")).click();
		return returnName;
	}
	
	public static ElementsCollection GetCardInfo() {
		return $$(byClassName("x-panel")).exclude(hidden).get(8).$$(byClassName("x-panel-default-framed")).filter(visible);
	}
}
