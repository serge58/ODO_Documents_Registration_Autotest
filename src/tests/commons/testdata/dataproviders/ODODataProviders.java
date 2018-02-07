package tests.commons.testdata.dataproviders;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import static common.utilities.Tools.generateRandomSymbolsRU;
import static common.utilities.Tools.getRandomInt;
import static common.utilities.Tools.getRandomLongDate;

public class ODODataProviders {
	@DataProvider(name = "document")
	public static Object[][] getDocumentData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object[] params = null;
		
		int depNumber = 1;
		JSONObject depart;

		String projPath;
		String filePath = "";
		String testName = method.getName();
		
		HashMap<String, String> allParams = new HashMap<String, String>();

		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("document_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray departList = (JSONArray) jsonObject.get("documents");
		
		if (testName.contentEquals("EditDocument") | testName.contentEquals("CheckEditDocResults")) {
			depNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("editDocument").trim()) - 1;
		} else 				
			depNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("document").trim()) - 1;
			
		depart = (JSONObject) departList.get(depNumber);
		
		if (testName.contentEquals("CheckResults")) {
			allParams.put("Департамент", (String) depart.get("Департамент"));
		} else {
			allParams.put("Департамент", (String) depart.get("Департамент"));
			allParams.put("Тип документа", (String) depart.get("Тип документа"));
			allParams.put("Откуда поступил", (String) depart.get("Откуда поступил"));				
			allParams.put("Индекс субъекта РФ", (String) depart.get("Индекс субъекта РФ"));	
			allParams.put("Исходящий номер", (String) depart.get("Исходящий номер"));		
			allParams.put("Исходящая дата", (String) depart.get("Исходящая дата"));		
			allParams.put("Автор (подписант)", (String) depart.get("Автор (подписант)"));
			allParams.put("Листов/томов", (String) depart.get("Листов/томов"));
			allParams.put("Приложение", (String) depart.get("Приложение"));
			allParams.put("ШПИ отправления", (String) depart.get("ШПИ отправления"));
			allParams.put("Примечание", (String) depart.get("Примечание"));
			allParams.put("Номер жалобного производства", (String) depart.get("Номер жалобного производства"));
			allParams.put("Номер дела в ВС РФ", (String) depart.get("Номер дела в ВС РФ"));
			allParams.put("В отношении кого", (String) depart.get("В отношении кого"));
			allParams.put("Краткое содержание", (String) depart.get("Краткое содержание") + " \n" + generateRandomSymbolsRU(10));
			allParams.put("Дата передачи судье", (String) depart.get("Дата передачи судье"));
			allParams.put("Этап судопроизводства", (String) depart.get("Этап судопроизводства"));
			allParams.put("Подшит/отправлен", (String) depart.get("Подшит/отправлен"));
			allParams.put("Дата отправки", (String) depart.get("Дата отправки"));
			allParams.put("Адрес отправки", (String) depart.get("Адрес отправки"));
			allParams.put("Рег. номер в общем ДО", (String) depart.get("Рег. номер в общем ДО"));
		}
		
		paramsList = new Object[1][];
		
		params = new Object[1];
		params[0] = allParams;
		
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "regCard")
	public static Object[][] getRegCardData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		
		int regCardNumber;

		String projPath;
		String filePath = "";
		
		HashMap<String, String> allParams = new HashMap<String, String>();

		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("regCard_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray regCardList = (JSONArray) jsonObject.get("reg_cards");

		regCardNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("regCard").trim()) - 1;
		
		JSONObject regCard = (JSONObject) regCardList.get(regCardNumber);

		allParams.put("ДСП", (String) regCard.get("ДСП"));
		allParams.put("Конфиденциальный", (String) regCard.get("Конфиденциальный"));
		allParams.put("Тип документа", (String) regCard.get("Тип документа"));
		allParams.put("Индекс субъекта РФ", (String) regCard.get("Индекс субъекта РФ"));
		allParams.put("Корреспондент", (String) regCard.get("Корреспондент"));
		allParams.put("Краткое содержание", (String) regCard.get("Краткое содержание") + " \n" + generateRandomSymbolsRU(10));
		
		String temp = (String) regCard.get("Листов/томов");
		if (temp.isEmpty()) temp = Integer.toString(getRandomInt(999999));
		allParams.put("Листов/томов", temp);
		
		allParams.put("Приложение", (String) regCard.get("Приложение")  + generateRandomSymbolsRU(10));
		allParams.put("Номер наряда", (String) regCard.get("Номер наряда"));
		
		temp = (String) regCard.get("Том наряда");
		if (temp.isEmpty()) temp = Integer.toString(getRandomInt(9999));
		allParams.put("Том наряда", temp);
		
		temp = (String) regCard.get("Страница тома");
		if (temp.isEmpty()) temp = Integer.toString(getRandomInt(999999999));
		allParams.put("Страница тома", temp);
		
		allParams.put("Примечание", (String) regCard.get("Примечание") + generateRandomSymbolsRU(10));
		allParams.put("Доложен", (String) regCard.get("Доложен"));
		
		temp = (String) regCard.get("Дата доклада");
		if (temp.isEmpty()) temp = getRandomLongDate();
		allParams.put("Дата доклада", temp);
		
		paramsList = new Object[1][];
		
		params = new Object[1];
		params[0] = allParams;
		
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "resolutionFields")
	public static Object[][] getResolutionData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		
		int resolutionNumber;

		String projPath;
		String filePath = "";
		
		HashMap<String, String> allParams = new HashMap<String, String>();

		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("resolution_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray regCardList = (JSONArray) jsonObject.get("Резолюции");

		resolutionNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("resolution").trim()) - 1;
		
		JSONObject resolution = (JSONObject) regCardList.get(resolutionNumber);
		
		allParams.put("Название", (String) resolution.get("Название"));
		allParams.put("Автор", (String) resolution.get("Автор"));
		allParams.put("Дата создания", (String) resolution.get("Дата создания"));
		allParams.put("Куда", (String) resolution.get("Куда"));
		allParams.put("Контроль исполнения", (String) resolution.get("Контроль исполнения"));
		allParams.put("Контролёр", (String) resolution.get("Контролёр"));
		allParams.put("Дата контроля", (String) resolution.get("Дата контроля"));
		allParams.put("Исполнитель", (String) resolution.get("Исполнитель"));
		allParams.put("Срок исполнения", (String) resolution.get("Срок исполнения"));
		allParams.put("Поручение исполнителя", (String) resolution.get("Поручение исполнителя"));
		allParams.put("Текст поручения", (String) resolution.get("Текст поручения"));
		
		paramsList = new Object[1][];
		
		params = new Object[1];
		params[0] = allParams;
		
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "correspondentFields")
	public static Object[][] getCorrespondentData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		
		int regCardNumber;

		String projPath;
		String filePath = "";
		
		HashMap<String, String> allParams = new HashMap<String, String>();

		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("correspondent_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray corrList = (JSONArray) jsonObject.get("correspondent");

		regCardNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("correspondent").trim()) - 1;
		
		JSONObject regCard = (JSONObject) corrList.get(regCardNumber);

		allParams.put("Корреспондент", (String) regCard.get("Корреспондент"));
		
		paramsList = new Object[1][];
		
		params = new Object[1];
		params[0] = allParams;
		
		paramsList[0] = params;
		return paramsList;
	}
	
	@DataProvider(name = "outgoingDocDraft")
	public static Object[][] getDocDraftData(ITestContext context, Method method) throws IOException, ParseException {
		Object paramsList[][];
		Object params[] = null;
		
		int docDraftNumber;

		String projPath;
		String filePath = "";
		
		HashMap<String, String> allParams = new HashMap<String, String>();

		projPath = System.getProperty("user.dir"); // Определяем домашнюю папку теста
		
		filePath = projPath + context.getCurrentXmlTest().getParameter("docDraft_DataFile");

		// Work with json_simple
		FileReader fr = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);
		JSONArray docDraftList = (JSONArray) jsonObject.get("Проекты документов");

		docDraftNumber = Integer.parseInt(context.getCurrentXmlTest().getParameter("docDraftNumber").trim()) - 1;
		
		JSONObject resolution = (JSONObject) docDraftList.get(docDraftNumber);
		
		allParams.put("Тип документа", (String) resolution.get("Тип документа"));
		allParams.put("Адресат", (String) resolution.get("Адресат"));
		allParams.put("Исполнитель", (String) resolution.get("Исполнитель"));
		allParams.put("Подписант", (String) resolution.get("Подписант"));
		
		paramsList = new Object[1][];
		
		params = new Object[1];
		params[0] = allParams;
		
		paramsList[0] = params;
		return paramsList;
	}
}
