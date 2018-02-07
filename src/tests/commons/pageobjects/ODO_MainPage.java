package tests.commons.pageobjects;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.$$;

import org.testng.Reporter;

import com.codeborne.selenide.ElementsCollection;

public class ODO_MainPage {
	ElementsCollection verticalTab;
	
	public ODO_MainPage() {
		//verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
	}
	
	public void OpenIncoming() {
		verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
		Reporter.log("Открываем вкладку Входящие<br>");
		verticalTab.find(text("Входящие")).click();
	}
	
	public void OpenOutgoing() {
		verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
		Reporter.log("Открываем вкладку Исходящие<br>");
		verticalTab.find(text("Исходящие")).click();
	}

	public void OpenInternal() {
		verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
		Reporter.log("Открываем вкладку Внутренние<br>");
		verticalTab.find(text("Внутренние")).click();
	}

	public void OpenFolders() {
		verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
		Reporter.log("Открываем вкладку Папки<br>");
		verticalTab.find(text("Папки")).click();
	}

	public void OpenSearch() {
		verticalTab = $$(byClassName("vertical-tabs")).exclude(hidden).first().$$(byClassName("x-item"));
		Reporter.log("Открываем вкладку Поиск<br>");
		verticalTab.find(text("Поиск")).click();
	}
}
