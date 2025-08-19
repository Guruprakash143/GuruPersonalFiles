package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import testBase.TestBase;

public class TestUtility {

	public String iden;
	
	public static String returnDateTime() {
		LocalDateTime myDateObj=LocalDateTime.now();
		DateTimeFormatter myFormateObj=DateTimeFormatter.ofPattern("ddMMyyyy_HH mm ss");
		String formattedDate=myDateObj.format(myFormateObj);
		return formattedDate;
	}
	
	
	//Fluent Wait
	public WebElement waitforElement(String identifier) {
		
		Wait<WebDriver> fw=new FluentWait<WebDriver>(TestBase.driver)
				.withTimeout(Duration.ofSeconds(200))
				.pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(ElementClickInterceptedException.class);
		
		iden=identifier;
		WebElement foo=fw.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return TestBase.driver.findElement(By.xpath(iden));
			}
		});
		return foo;
	}
	
	
	//Wait for element to be visible 
	public static WebElement waitForElementToBeVisible(String identification,int sec) {
		WebDriverWait wait=new WebDriverWait(TestBase.driver, Duration.ofSeconds(sec));
		WebElement ele=wait.until(ExpectedConditions.visibilityOf(TestBase.driver.findElement(By.xpath(identification))));
		
		return ele;
	}
	
	
	//Wait for element to be Clickable 
		public static WebElement waitForElementToBeClickable(String identification,int sec) {
			WebDriverWait wait=new WebDriverWait(TestBase.driver, Duration.ofSeconds(sec));
			WebElement ele=wait.until(ExpectedConditions.visibilityOf(TestBase.driver.findElement(By.xpath(identification))));
			
			return ele;
		}
		
		//Fetch the WebElement
		public WebElement getWebElement(String identifierType,String identifierValue) {
			if(identifierType.toLowerCase()=="id") {
				return TestBase.driver.findElement(By.id(identifierValue));
			}else if(identifierType.toLowerCase()=="name") {
				return TestBase.driver.findElement(By.name(identifierValue));
			}else if(identifierType.toLowerCase()=="xpath") {
				return TestBase.driver.findElement(By.xpath(identifierValue));
			}else if(identifierType.toLowerCase()=="tagname") {
				return TestBase.driver.findElement(By.tagName(identifierValue));
			}else {
				return null;
			}
		}
		
		
		public void validateWebElement(String identifier,String failureMessage) {
			WebElement fo=null;
			
			try {
				Wait<WebDriver> fw=new FluentWait<WebDriver>(TestBase.driver)
						.withTimeout(Duration.ofSeconds(200))
						.pollingEvery(Duration.ofSeconds(1))
						.ignoring(NoSuchElementException.class)
						.ignoring(ElementClickInterceptedException.class)
						.ignoring(ElementNotInteractableException.class)
						.ignoring(StaleElementReferenceException.class);
				
				iden=identifier;
				WebElement foo=fw.until(new Function<WebDriver,WebElement>() {
					public WebElement apply(WebDriver driver) {
						return TestBase.driver.findElement(By.xpath(iden));
					}
				});
				fo=foo;
			}catch(Exception e) {
				GenericUtil.failScreenshot(failureMessage);
				Assert.assertTrue(fo.isDisplayed(), failureMessage);
			}
		}
		
		public void clickOnWebElement(String identifier,String identifierValue,String filed) throws InterruptedException {
			validateWebElement(identifierValue, filed);
			WebElement object=getWebElement(identifier, identifierValue);
			JavascriptExecutor jse=(JavascriptExecutor)TestBase.driver;
			jse.executeScript("arguments[0].scrollIntoView();", object);
			Thread.sleep(1000);
			object.click();
			TestBase.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(200));
			TestBase.logger.pass(filed+"exist and clicked on it");
		}
		
		//Wait until the Element is Clickable
		public void waitToClick(String identifier,String field) {
			JavascriptExecutor js=(JavascriptExecutor)TestBase.driver;
			WebElement element=(new WebDriverWait(TestBase.driver, Duration.ofSeconds(20)))
					.until(ExpectedConditions.elementToBeClickable(By.xpath(identifier)));
			if(element.isDisplayed()) {
				js.executeScript("arguments[0].click();", element);
				TestBase.logger.pass(field+" exits and clciked on it");
			}else {
				TestBase.logger.fail(field+" doesnt match");
			}
		}
		
		public void scrollByVisibleElement(WebElement object) {
			JavascriptExecutor js=(JavascriptExecutor)TestBase.driver;
			js.executeScript("arguments[0].scrollIntoView();", object);
		}
		
		public void enterData(String identifier,String identifierValue,String field,String value) throws InterruptedException {
			scrollByVisibleElement(getWebElement(identifier, identifierValue));
			validateWebElement(identifierValue, value);
			WebElement object=getWebElement(identifier, identifierValue);
			JavascriptExecutor jse=(JavascriptExecutor)TestBase.driver;
			jse.executeScript("arguments[0].scrollIntoView();", object);
			Thread.sleep(1000);
			jse.executeScript("arguments[0].setAttribute('style','border:2px solid red; background:yellow')", object);
			String expectedText=value.trim();
			Actions action=new Actions(TestBase.driver);
			action.doubleClick(object).perform();
			object.clear();
			object.sendKeys("");
			object.sendKeys(expectedText);
			object.sendKeys(Keys.TAB);
			String actualText=object.getAttribute("value").trim();
			if(actualText.equals(expectedText)) {
				TestBase.logger.pass(field+" exist and entered "+value);
			}else {
				GenericUtil.failScreenshot(field+" Expected:"+expectedText+" and Actual: "+actualText);
				
			}
		}
		
			public void enterDataUsingJS(String identifier,String identifierValue,String field,String value) throws InterruptedException {
				scrollByVisibleElement(getWebElement(identifier, identifierValue));
				validateWebElement(identifierValue, value);
				WebElement object=getWebElement(identifier, identifierValue);
				JavascriptExecutor jse=(JavascriptExecutor)TestBase.driver;
				jse.executeScript("arguments[0].scrollIntoView();", object);
				Thread.sleep(1000);
				jse.executeScript("arguments[0].setAttribute('style','border:2px solid red; background:yellow')", object);
				String expectedText=value.trim();
				jse.executeScript("arguments[0].value='" +expectedText +"';", object);
				Actions action=new Actions(TestBase.driver);
				action.doubleClick(object).perform();
				action.sendKeys(Keys.TAB);
				String actualText=object.getAttribute("value").trim();
				if(actualText.equals(expectedText)) {
					TestBase.logger.pass(field+" exist and entered "+value);
				}else {
					GenericUtil.failScreenshot(field+" Expected:"+expectedText+" and Actual: "+actualText);
					
				}
			}
			
			
}
