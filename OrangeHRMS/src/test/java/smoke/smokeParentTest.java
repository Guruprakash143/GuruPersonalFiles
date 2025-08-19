package smoke;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import pageEvents.LoginPageEvents;
import testBase.TestBase;
import utils.ExcelOperations;
import utils.TestUtility;

public class smokeParentTest extends TestBase{

	
	ExcelOperations xlObject;
	LoginPageEvents objLogin;
	
	TestUtility util;
	
	public smokeParentTest() {
		super();
	}
	
	@BeforeTest
	public void beforeTestMethod() {
		generateHTMLReport("OrangeHRM");
	}
	
	@AfterTest
	public void afterTestMethod() {
		extent.setSystemInfo("Executed Environment", prop.getProperty("env"));
		closeHTML();
	}
	
	@BeforeMethod
	public void beforeMethodMethod(Method testMethod) {
		
		objLogin=new LoginPageEvents();
		
		xlObject=new ExcelOperations();
		util=new TestUtility();
		logger=extent.createTest(testMethod.getName());
	}
	
	public void afterMethodMethod(ITestResult result) {
		closeBrowser(result);
	}
	
}
