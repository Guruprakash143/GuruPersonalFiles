package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestBase {

	public static WebDriver driver;
	public static Properties prop;
	public static ExtentReports extent;
	public static ExtentSparkReporter spark;
	public static ExtentTest logger;
	public static String htmlFilePath="";
	
	public TestBase() {
		try {
			prop=new Properties();
			FileInputStream ip=new FileInputStream(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"configuration"+File.separator+"config.properties");
			prop.load(ip);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void generateHTMLReport(String className) {
		Date now=new Date();
		SimpleDateFormat dateFormate=new SimpleDateFormat("MM-dd-yyyy hh mm ss");
		String time=dateFormate.format(now);
		String resDir=System.getProperty("user.dir")+"\\ExecutionReports\\"+time;
		File resFolder=new File(resDir);
		resFolder.mkdir();
		
		final String filePath=resDir +"./"+className +".html";
		
		htmlFilePath=filePath;
		prop.setProperty("HTMLPath", htmlFilePath);
		spark=new ExtentSparkReporter(htmlFilePath);
		spark.config().setDocumentTitle("Automation Report");
		spark.config().setReportName("Automation Test Results");
		spark.config().setTheme(Theme.STANDARD);
		extent=new ExtentReports();
		extent.attachReporter(spark);
	}
	
	
	public void initialization(String env) {
		
		ChromeOptions option=new ChromeOptions();
		option.addArguments("--remote-allow-origins=*");
		option.addArguments("--window-size=1920,1800");
		option.addArguments("--start-maximized");
		DesiredCapabilities des=new DesiredCapabilities();
		des.setCapability(ChromeOptions.CAPABILITY, option);
		option.merge(des);
		driver=new ChromeDriver(option);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		if(env.equalsIgnoreCase("QA")) {
			driver.get(prop.getProperty("QA_URL"));
		}else if(env.equalsIgnoreCase("UAT")) {
			driver.get(prop.getProperty("UAT_URL"));
		}
		
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	
	
	//close the browser
	public void closeBrowser(ITestResult result) {
		if(result.getStatus()==ITestResult.SUCCESS) {
			String methodName=result.getMethod().getMethodName();
			String logText="Test Case: "+methodName+" Passed";
			Markup m=MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			logger.generateLog(Status.PASS, m);
		}else if(result.getStatus()==ITestResult.FAILURE){
			
			String methodName=result.getMethod().getMethodName();
			String logText="Test Case: "+methodName+" Faield";
			Markup m=MarkupHelper.createLabel(logText, ExtentColor.RED);
			logger.generateLog(Status.FAIL, m);
		}else if(result.getStatus()==ITestResult.SKIP){
			
			String methodName=result.getMethod().getMethodName();
			String logText="Test Case: "+methodName+" Skipped";
			Markup m=MarkupHelper.createLabel(logText, ExtentColor.INDIGO);
			logger.generateLog(Status.SKIP, m);
		}
		
		if(TestBase.driver !=null) {
			TestBase.driver.quit();
		}
	}
	
	//close the HTML Report
	public void closeHTML() {
		extent.flush();
	}
	
}
