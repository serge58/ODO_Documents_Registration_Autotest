package common.httpclient.models;
import java.util.List;

public class Document {
	Number type;			// Тип документа
	String fromWhere;		// Откуда поступил
	Number region;			// Индекс субъекта РФ
	String outNumber;		// Исходящий номер
	String outDate;			// Исходящая дата
	Authors authors;		// Авторы
	Number volNumber;		// Листов/томов
	String app;				// Приложение
	String postNumber;		// ШПИ отправления
	String comments;		// Примечание
	String regNum;			// Рег. номер в общем ДО
	String complaintNum;	// Номер жалобного производства
	String caseNumber;		// Номер дела в ВС РФ
	Number step;			// Этап судопроизводства
	CoreFaces coreFaces;	// В отношении кого
	String desc;			// Краткое содержание
	String judgeDate;		// Дата передачи судье
	Number storingType;		// Подшит/Отправлен
	String storingDate;		// Дата
	String sentAddress;		// Адрес отправки
	Number isCase;			// 0 - документ; 1 - судебное дело

	public Number getType() {
		return type;
	}

	public void setType(Number type) {
		this.type = type;
	}

	public String getFromWhere() {
		return fromWhere;
	}

	public void setFromWhere(String fromWhere) {
		this.fromWhere = fromWhere;
	}

	public Number getRegion() {
		return region;
	}

	public void setRegion(Number region) {
		this.region = region;
	}

	public String getOutNumber() {
		return outNumber;
	}

	public void setOutNumber(String outNumber) {
		this.outNumber = outNumber;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public Authors getAuthors() {
		return authors;
	}

	public void setAuthors(Authors authors) {
		this.authors = authors;
	}

	public Number getVolNumber() {
		return volNumber;
	}

	public void setVolNumber(Number volNumber) {
		this.volNumber = volNumber;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(String postNumber) {
		this.postNumber = postNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRegNum() {
		return regNum;
	}

	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	public String getComplaintNum() {
		return complaintNum;
	}

	public void setComplaintNum(String complaintNum) {
		this.complaintNum = complaintNum;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Number getStep() {
		return step;
	}

	public void setStep(Number step) {
		this.step = step;
	}

	public CoreFaces getCoreFaces() {
		return coreFaces;
	}

	public void setCoreFaces(CoreFaces coreFaces) {
		this.coreFaces = coreFaces;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getJudgeDate() {
		return judgeDate;
	}

	public void setJudgeDate(String judgeDate) {
		this.judgeDate = judgeDate;
	}

	public Number getStoringType() {
		return storingType;
	}

	public void setStoringType(Number storingType) {
		this.storingType = storingType;
	}

	public String getStoringDate() {
		return storingDate;
	}

	public void setStoringDate(String storingDate) {
		this.storingDate = storingDate;
	}

	public String getSentAddress() {
		return sentAddress;
	}

	public void setSentAddress(String sentAddress) {
		this.sentAddress = sentAddress;
	}

	public Document(Number type, String fromWhere, Number region, String outNumber, String outDate, Authors authors,
			Number volNumber, String app, String postNumber, String comments, String regNum, String complaintNum,
			String caseNumber, Number step, CoreFaces coreFaces, String desc, String judgeDate, Number storingType,
			String storingDate, String sentAddress, Number isCase) {
		super();
		this.type = type;
		this.fromWhere = fromWhere;
		this.region = region;
		this.outNumber = outNumber;
		this.outDate = outDate;
		this.authors = authors;
		this.volNumber = volNumber;
		this.app = app;
		this.postNumber = postNumber;
		this.comments = comments;
		this.regNum = regNum;
		this.complaintNum = complaintNum;
		this.caseNumber = caseNumber;
		this.step = step;
		this.coreFaces = coreFaces;
		this.desc = desc;
		this.judgeDate = judgeDate;
		this.storingType = storingType;
		this.storingDate = storingDate;
		this.sentAddress = sentAddress;
		this.isCase = isCase;
	}

	public Document() {}
	
	public Number getIsCase() {
		return isCase;
	}

	public void setIsCase(Number isCase) {
		this.isCase = isCase;
	}
}

class Authors {
	public List<String> getinsert() {
		return insert;
	}

	public void setInsert(List<String> insert) {
		this.insert = insert;
	}

	public Authors(List<String> insert) {
		super();
		this.insert = insert;
	}

	private List<String> insert;
}

class CoreFaces {
	public List<String> getCoreFaces() {
		return coreFaces;
	}

	public void setCoreFaces(List<String> coreFaces) {
		this.coreFaces = coreFaces;
	}

	public CoreFaces(List<String> coreFaces) {
		super();
		this.coreFaces = coreFaces;
	}

	private List<String> coreFaces;
}