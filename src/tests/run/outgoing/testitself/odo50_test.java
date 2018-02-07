package tests.run.outgoing.testitself;

import tests.commons.pageobjects.ARM_MainPage;
import tests.commons.pageobjects.ODO_MainPage;
import tests.run.outgoing.pages.OutgoingMainPage;

import java.util.HashMap;

import tests.commons.constants.CardTypes;

public class odo50_test {
	static ARM_MainPage armMainPage = new ARM_MainPage();
	static ODO_MainPage odoMainPage = new ODO_MainPage();
	static OutgoingMainPage outgoingMainPage = new OutgoingMainPage();
	
	static HashMap<String, String> personData;
	
	public static void CreateDocDraft(HashMap<String, String> docDraftFields) {
		armMainPage.OpenODO();
		odoMainPage.OpenOutgoing();
		
		outgoingMainPage.SelectCardType(CardTypes.PROJECTS_DOC);
		
		outgoingMainPage.SetDocDraftFields(docDraftFields);
		
		personData = outgoingMainPage.CreateEditDocDraft(false);
	}
	
	public static void CheckDocDraft() {
		outgoingMainPage.CheckDocDraft(personData);
	}
}