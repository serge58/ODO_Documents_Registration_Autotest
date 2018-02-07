package tests.run.outgoing.testitself;

import tests.commons.constants.CardStatus;
import tests.commons.constants.CardTypes;
import tests.commons.pageobjects.ARM_MainPage;
import tests.commons.pageobjects.ODO_MainPage;
import tests.run.outgoing.pages.OutgoingMainPage;

public class odo53_test {
	static ARM_MainPage armMainPage = new ARM_MainPage();
	static ODO_MainPage odoMainPage = new ODO_MainPage();
	static OutgoingMainPage outgoingMainPage = new OutgoingMainPage();
	
	static String docNumber;
	
	public static void DeleteDocDraft() {
		armMainPage.OpenODO();
		odoMainPage.OpenOutgoing();

		outgoingMainPage.SelectCardType(CardTypes.PROJECTS_DOC);
		
		docNumber = outgoingMainPage.GetDocNumber(outgoingMainPage.FindDocument(CardStatus.PROJECT));
		
		outgoingMainPage.DeleteDocDraft();
	}
	
	public static void CheckDocDraftDeleted() {
		outgoingMainPage.CheckDocDraftDeleted(docNumber);
	}
}
