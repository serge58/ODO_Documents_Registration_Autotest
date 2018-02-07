package tests.run.folders.pages;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

import static common.utilities.Tools.*;

import org.testng.Reporter;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import tests.commons.pageobjects.ARM_MainPage;

public class FoldersMainPage {
	static SelenideElement createFolderButton;
	static SelenideElement editFolderButton;
	static SelenideElement deleteFolderButton;
	
	static ElementsCollection foldersList;
	static SelenideElement newFolder;
	
	static String folderName;
	
	static String worker;
	static String workerDep;
	static String workerDepShortName;
	
	public FoldersMainPage() {
		createFolderButton = $$(byAttribute("data-qtip", "Добавить")).find(visible);
		editFolderButton = $$(byAttribute("data-qtip", "Редактировать")).find(visible);
		deleteFolderButton = $$(byAttribute("data-qtip", "Удалить")).find(visible);
		
		foldersList = $$(byClassName("docflow-list")).find(visible).$$(byClassName("x-dataview-item"));
		
		ARM_MainPage armMainPage = new ARM_MainPage();
		worker = armMainPage.GetWorkerFullName();
		workerDep = armMainPage.GetWorkerDep();
		workerDepShortName = armMainPage.GetWorkerShortName();
	}
	
	public SelenideElement CreateFolder() {
		Reporter.log("Нажимаем кнопку Добавить папку<br>"); 
		createFolderButton.click();
		
		Reporter.log("Указываем название папки<br>");
		SelenideElement createFolderWindow = $$(byClassName("x-window")).findBy(text("Создание папки документов"));
		folderName = "Автотестовая папка - " + generateRandomSymbolsRU(10);
		createFolderWindow.$(byName("title")).setValue(folderName);
		
		Reporter.log("Нажимаем 'Сохранить'<br>");
		createFolderWindow.$(byText("Сохранить")).click();
		
		newFolder = foldersList.first();
		return newFolder;
	}
	
	public void CheckFolder(SelenideElement folder) {
		Reporter.log("Проверяем название и дату вновь созданной папки<br>");
		folder.shouldHave(text(folderName + " от " + getNowLongDate()));

		Reporter.log("Проверяем дерево, в нём должна быть одна запись с названием папки<br>");
		SelenideElement folderTree = $$(byClassName("x-tree-view")).find(visible);
		folderTree.shouldHave(text(folderName));
	}
	
	public void EditFolder() {
		Reporter.log("Нажимаем кнопку Редактировать папку<br>");
		editFolderButton.click();
		
		Reporter.log("Генерируем новое название Папки<br>");
		folderName = "Автотестовая папка - " + generateRandomSymbolsRU(10);
		
		Reporter.log("Указываем название папки<br>");
		SelenideElement editFolderWindow = $$(byClassName("x-window")).findBy(text("Редактирование папки документов"));
		editFolderWindow.$(byName("title")).setValue(folderName);
		
		Reporter.log("Проверяем строку: Папка создана, Дата, Имя работника, Краткое название депертамента<br>");
		SelenideElement statusBar = editFolderWindow.$(byClassName("x-component"));
		String whoCreated = worker + ", " + workerDepShortName;
		String creationDate = getNowLongDate();
		statusBar.shouldHave(text("Кем создана: " + whoCreated));
		statusBar.shouldHave(text("Дата создания: " + creationDate));
	
		Reporter.log("Нажимаем 'Сохранить'<br>");
		editFolderWindow.$(byText("Сохранить")).click();
	}
	
	public void DeleteFolder(SelenideElement folder) {
		Reporter.log("Нажимаем кнопку Удалить папку<br>");
		deleteFolderButton.click();
		
		Reporter.log("Проверяем окно предупреждения об удалении папки<br>");
		SelenideElement deleteFolderWindow = $$(byClassName("x-message-box")).findBy(text("Внимание!"));
		
		deleteFolderWindow.shouldHave(text("Вы уверены, что хотите удалить папку документа и все ее связи с документами?"));
		
		deleteFolderWindow.$(byText("Да")).click();
		
		Reporter.log("Проверяем окно с сообщением об успешном удалении папки<br>");
		SelenideElement info =  $$(byClassName("x-message-box")).findBy(text("Информация"));
		info.shouldHave(text("Папка успешно удалена"));
		info.$(byText("OK")).click();
		
		Reporter.log("Проверяем что Папки нет в списке<br>");
		foldersList.findBy(text(folderName)).shouldNot(exist);
	}
	
	public SelenideElement SelectFolder(String folder) {
		return foldersList.findBy(text(folder));
	}
	
	public String GetNewFolderName() {
		return folderName;
	}
}
