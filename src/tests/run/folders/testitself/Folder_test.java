package tests.run.folders.testitself;

import com.codeborne.selenide.SelenideElement;

import tests.commons.pageobjects.ARM_MainPage;
import tests.commons.pageobjects.ODO_MainPage;
import tests.run.folders.pages.FoldersMainPage;

public class Folder_test {
	static ARM_MainPage armMainPage = new ARM_MainPage();
	static ODO_MainPage odoMainPage = new ODO_MainPage();
	static FoldersMainPage foldersMainPage = new FoldersMainPage();
	static SelenideElement workFolder;
	
	public static void CreateFolder() {
		armMainPage.OpenODO();
		
		odoMainPage.OpenFolders();
		
		workFolder = foldersMainPage.CreateFolder();
		
		foldersMainPage.CheckFolder(workFolder);
	}
	
	public static void EditFolder() {
		foldersMainPage.EditFolder();
		
		foldersMainPage.CheckFolder(workFolder);
	}
	
	public static void DeleteFolder() {
		foldersMainPage.DeleteFolder(workFolder);
	}
}
