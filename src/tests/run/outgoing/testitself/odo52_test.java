package tests.run.outgoing.testitself;

import com.codeborne.selenide.SelenideElement;

import tests.commons.constants.CardStatus;
import tests.commons.constants.CardTypes;
import tests.commons.pageobjects.ARM_MainPage;
import tests.commons.pageobjects.ODO_MainPage;
import tests.run.outgoing.pages.OutgoingMainPage;

public class odo52_test {
	static ARM_MainPage armMainPage = new ARM_MainPage();
	static ODO_MainPage odoMainPage = new ODO_MainPage();
	static OutgoingMainPage outgoingMainPage = new OutgoingMainPage();
	
	static SelenideElement docDraft;
	
	public static void RecallDocDraft() {
		armMainPage.OpenODO();
		odoMainPage.OpenOutgoing();

		outgoingMainPage.SelectCardType(CardTypes.PROJECTS_DOC);
		docDraft = outgoingMainPage.FindDocument(CardStatus.PROJECT);
		
		outgoingMainPage.RecallDocDraft();
	}
	
	public static void CheckDocDraftRecalled() {
		outgoingMainPage.CheckDocDraftRecalled(docDraft);
	}
}
