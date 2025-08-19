package pageEvents;

import pageObjects.LoginPageObjects;
import testBase.TestBase;
import utils.TestUtility;

public class LoginPageEvents extends TestBase{
	
	public LoginPageEvents() {
		super();
	}

	TestUtility utils=new TestUtility();
	
	public void enterUserName() throws InterruptedException {
		utils.enterData("xpath", LoginPageObjects.username, "UserName", prop.getProperty("username"));
	}
	
	public void enterPassword() throws InterruptedException {
		utils.enterData("xpath", LoginPageObjects.password, "Password", prop.getProperty("password"));
	}
	
	public void clickOnLoginButton() throws InterruptedException {
		utils.clickOnWebElement("xpath", LoginPageObjects.loginBtn, "Login");
	}
}
