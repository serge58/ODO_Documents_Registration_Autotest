package tests.commons.pageobjects;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import org.testng.Reporter;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class Common {
	
	public static void WaitingForUpdateAllWindows() {
		SelenideElement waitWindow = $$(byClassName("x-mask-msg-text")).find(text("Загрузка..."));
		
		if (waitWindow.isDisplayed())
			waitWindow.waitWhile(visible, 30000);
		
		Selenide.sleep(1000);
		
		if (waitWindow.isDisplayed())
			waitWindow.waitWhile(visible, 30000);
	}
	
	public static void WaitForDocumentLoading() {
		SelenideElement waitWindow = $$(byClassName("x-mask-msg-text")).find(text("Загрузка документа..."));
		
		if (waitWindow.isDisplayed())
			waitWindow.waitWhile(visible, 30000);
		
		Selenide.sleep(1000);
	}
	
	public static void WaitingForManualEntryWindow() {
		$$(byClassName("x-window")).exclude(hidden).find(text("Ручной ввод")).waitUntil(visible, 5000);
	}
	
	public static void SelectProfile(String profile) {
		SelenideElement userInfo = $(byClassName("x-userinfo")); 
		String currentProfile = userInfo.getText().split("\n")[1];
		
		Reporter.log("Проверяем профиль пользователя, если не тот, выбираем нужный<br>");
		if (!currentProfile.equals(profile)) {
			userInfo.click();
			
			SelenideElement changeProfileMenu = $$(byClassName("x-box-inner")).find(text("Сменить профиль Завершить сеанс"));
			changeProfileMenu.$(byText("Сменить профиль")).click();
			
			SelenideElement changeProfile = $$(byClassName("x-window")).filter(visible).find(text("Выбор профиля"));
			ElementsCollection profiles = changeProfile.$$(byClassName("x-dataview-item"));
			
			profiles.findBy(text(profile)).click();
			changeProfile.$(byText("Выбрать (Enter)")).click();
		}
	}
	
	public static void Relogin(String login, String password, String userName) {
		SelenideElement userInfo = $(byClassName("x-userinfo"));
		String currentUserName = userInfo.getText().split("\n")[0];
		
		Reporter.log("Проверяем логин пользователя, если не тот, завершаем сеанс и перелогиниваемся под нужным<br>");
		if (!currentUserName.equals(userName)) {
			userInfo.click();
			SelenideElement changeProfileMenu = $$(byClassName("x-box-inner")).find(text("Сменить профиль Завершить сеанс"));
			changeProfileMenu.$(byText("Завершить сеанс")).click();
			
			Reporter.log("Логинимся пользователем, который будет передавать документ<br>");

			Login(login, password);
		}
	}
	
	public static void Login (String login, String password) {
		/*int waitTime = 20000;
		int i = 0;
		
		while ($(byText("Войти")).is(not(visible)) && i <= waitTime) {
		    pressESC();
		    Selenide.sleep(1000);
		    i++;
		}*/
		
		Reporter.log("Заполняем поле 'Пользователь'<br>");
 		SelenideElement loginPanel = $(byClassName("x-panel")).waitUntil(visible, 30000);
 		
 		if (loginPanel.isDisplayed()) {
 			Selenide.sleep(2000);
 	 		loginPanel.$(byAttribute("type", "text")).waitUntil(visible, 10000).setValue(login);
 	 		
 	 		Reporter.log("Заполняем поле 'Пароль'<br>");
 	 		loginPanel.$(byAttribute("type", "password")).waitUntil(visible, 10000).setValue(password);
 	 		
 	 		Reporter.log("Нажимаем кнопку 'Войти'<br>");
 	 		$(byText("Войти")).click();
 		}
	}
	
	public static void SelectFromDropDown(SelenideElement dropDown, String value) {
		dropDown.$(byClassName("x-form-arrow-trigger")).click();
		$$(byClassName("x-boundlist-list-ct")).find(visible).$$(byClassName("x-boundlist-item")).find(text(value)).click();
	}
	
	public static void setCheckbox(SelenideElement checkbox, boolean set) {
		if (set) {
			if (!checkbox.has(cssClass("x-form-cb-checked")))
				checkbox.$(byClassName("x-form-cb-label")).click();
		} else {
			if (checkbox.has(cssClass("x-form-cb-checked")))
				checkbox.$(byClassName("x-form-cb-label")).click();
		}
	}
	
	public static void CheckSelected(SelenideElement checkBox, boolean shouldBeChecked) {
		if (shouldBeChecked)
			checkBox.shouldHave(cssClass("x-form-cb-checked"));
		else 
			checkBox.shouldNotHave(cssClass("x-form-cb-checked"));
	}
	
	public static void CheckGridCheckbox(SelenideElement gridCheckBox, boolean shouldBeChecked) {
		if (shouldBeChecked)
			gridCheckBox.shouldHave(cssClass("x-grid-checkcolumn-checked"));
		else 
			gridCheckBox.shouldNotHave(cssClass("x-grid-checkcolumn-checked"));
	}
	
	public static void SelectDepartmentFromWindowTreeList(ElementsCollection depsTreeList, String depName) {
		
		SelenideElement retDepart = depsTreeList.find(text(depName));
		
		for (int i = 0; i < 10; i++) {
			if (!retDepart.exists())
				depsTreeList.last().click();
			else break;
		}
		
		retDepart.click();
	}
	
	public static void CheckEnabled(SelenideElement element, boolean enabled) {
		if (enabled)
			element.shouldNotHave(cssClass("x-item-disabled"));
		else 
			element.shouldHave(cssClass("x-item-disabled"));
	}
}
