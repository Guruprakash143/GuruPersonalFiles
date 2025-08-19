package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.yandex.qatools.ashot.Screenshot;

import com.aventstack.extentreports.MediaEntityBuilder;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import testBase.TestBase;

public class GenericUtil extends TestBase{

	public static String captureScreenshot() {
		TakesScreenshot takeScreenshot=(TakesScreenshot)TestBase.driver;
		String snapShot=takeScreenshot.getScreenshotAs(OutputType.BASE64);
		return snapShot;
	}
	
	
	public static String getScreenshot(String screenShotName) {
		
		File sourceFile=((TakesScreenshot)TestBase.driver).getScreenshotAs(OutputType.FILE);
		String imagePath="./ExecutionReport/Screenshots/"+screenShotName+TestUtility.returnDateTime()+".png";
		File path=new File(imagePath);
		try {
			FileUtils.copyFile(sourceFile, path);
			
		}catch(Exception e) {
			
		}
			return imagePath;
	}
	
	public static synchronized void failScreenshot(String logDetails) {
		String screenshotName=GenericUtil.captureScreenshot();
		FailScreenShotCapture(logDetails, screenshotName);
	}
	
	
	public static synchronized void passScreenshot(String logDetails) {
		String screenshotName=GenericUtil.captureScreenshot();
		PassScreenShotCapture(logDetails, screenshotName);
	}
	
	
	public static Object FailScreenShotCapture(String logDetails,String imagePath) {
		logger.fail(logDetails,MediaEntityBuilder.createScreenCaptureFromBase64String(imagePath).build());
		return logger;
	}
	
	
	public static Object PassScreenShotCapture(String logDetails,String imagePath) {
		logger.fail(logDetails,MediaEntityBuilder.createScreenCaptureFromBase64String(imagePath).build());
		return logger;
	}
	
	//capture Fill page screenshot with Ashot and Base64
	public static String captureFullPage() throws IOException {
		Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ImageIO.write(screenshot.getImage(), "PNG", out);
		byte[] bytes=out.toByteArray();
		String imagebase64=org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
		return imagebase64;
	}
	
	public static synchronized void passFullScreenshot(String logDetails) throws IOException {
		String screenshot=GenericUtil.captureFullPage();
		PassScreenShotCapture(logDetails, screenshot);
	}
}
