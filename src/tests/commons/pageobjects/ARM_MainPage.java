package tests.commons.pageobjects;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.testng.Reporter;

public class ARM_MainPage {
	String worker;
	String workerDep;
	String workerDepShortName;
	
	public ARM_MainPage() {
		worker = $(byClassName("x-userinfo")).getText().split("\n")[0];
		workerDep = $(byClassName("x-userinfo")).getText().split("\n")[1];
		workerDepShortName = UPD.GetDepartmentShortName(workerDep);
	}
	
	public String GetWorkerFullName() {
		return worker;
	}
	
	public String GetWorkerDep() {
		return workerDep;
	}
	
	public String GetWorkerShortName() {
		return workerDepShortName;
	}
	
	public void OpenUPD() {
		Reporter.log("Открываем вкладку УПД<br>");
		$(byText("УПД")).click();
	}
	
	public void OpenODO() {
		Reporter.log("Открываем вкладку ОДО<br>");
		$(byText("ОДО")).click();
	}
	
	public void OpenUPR() {
		Reporter.log("Открываем вкладку УПР<br>");
		$(byText("УПР")).click();
	}
	
	public void OpenSDP() {
		Reporter.log("Открываем вкладку СДП<br>");
		$(byText("СДП")).click();
	}
	
	public void OpenERP() {
		Reporter.log("Открываем вкладку ЕРП<br>");
		$(byText("ЕРП")).click();
	}
	
	public void OpenSettings() {
		Reporter.log("Открываем вкладку Настройки<br>");
		$(byText("Настройки")).click();
	}
}
