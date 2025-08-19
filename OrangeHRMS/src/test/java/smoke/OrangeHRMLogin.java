package smoke;

import org.testng.annotations.Test;

public class OrangeHRMLogin extends smokeParentTest{

	@Test
	public void orangeHRM_verifyTheLoginFunctionality() throws InterruptedException {
		String testCaseId="OrangeHRM_001";
		
		int rowNumber=xlObject.populateRowNums("TestCaseSelectionConfiguaration", testCaseId);
		String executionFlag=xlObject.getCellData("TestCaseSelectionConfiguaration", rowNumber, "ExecutionFlag");
		String env=xlObject.getCellData("TestCaseSelectionConfiguaration", rowNumber, "Environment");
		prop.setProperty("type", "SmokeTest");
		prop.setProperty("env", env);
		if(executionFlag.equalsIgnoreCase("Y")) {
			
			/******** Main Script Starts **********/
			initialization(env);
			objLogin.enterUserName();
			objLogin.enterPassword();
			objLogin.clickOnLoginButton();
/********** This is Simple Test case ***************/
			
			/******** Main Script ends ***********/
		}else {
			logger.skip("Please update the ExecutionFlag as <b>'Y'</b> for <b>'"+testCaseId+"'</b> test script in <b> TestCaseSelectionConfiguaration</b> sheet under <b>TestData.xlsx</b>");
		}
	}
}
