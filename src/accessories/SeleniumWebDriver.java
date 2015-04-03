package accessories;

import static com.thoughtworks.selenium.SeleneseTestBase.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;

public class SeleniumWebDriver {
	// what browsers are we interested in implementing
	public enum Browsers {
		Firefox, Chrome, InternetExplorer, Safari
	};

	// what locator actions are available in webdriver
	public enum Locators {
		xpath, id, name, classname, paritallinktext, linktext, tagname,cssSelector
	};

	// this is our driver that will be used for all selenium actions
	private WebDriver driver;

	// our constructor, determining which browser to start with
	public SeleniumWebDriver(Browsers browser) {
		switch (browser) { // check our browser
		case Firefox: {
			driver = new FirefoxDriver();
			break;
		}
		case Chrome: {
			driver = getChromedriver();
			break;
		}
		case InternetExplorer: {
			driver = getIEdriver();
			break;
		}
		case Safari: {
			driver = new SafariDriver();
			break;
		}
		// if our browser is not listed, throw an error
		default: {
			System.out.println("Required browser is not found");
		}
		}
		System.out.println("Run browser");
	}

	public SeleniumWebDriver(Browsers browser, String appURL) {
		this(browser);
		// open a new driver instance to our application URL
		driver.get(appURL);

	}

	// for running test in IE
	public WebDriver getIEdriver() {
		File file = new File("libs\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		return new InternetExplorerDriver();
	}
	//for running test in Chrome
	public WebDriver getChromedriver() {
		File file = new File("libs\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		return new ChromeDriver();
	}

	// a method to delete cookies
	public void deleteCookies() {
		getDriver().manage().deleteAllCookies();
		System.out.println("Clear browser's cookies");
	}

	// a method to allow retrieving our driver instance
	public WebDriver getDriver() {
		return driver;
	}

	// ///////////////////////////////////////
	// waiting functionality
	// ///////////////////////////////////////

	// a method for allowing selenium to pause for a set amount of time
	public void wait(int seconds) throws InterruptedException {
		Thread.sleep(seconds * 1000);
	}

	public void wait(double seconds) throws InterruptedException {
		Thread.sleep(Double.doubleToLongBits(seconds * 1000));
	}

	// a method for waiting until an element is displayed
	public void waitForElementDisplayed(Locators locator, String element)
			throws Exception {
		waitForElementDisplayed(getWebElement(locator, element), 5);
	}

	public void waitForElementDisplayed(Locators locator, String element,
			int seconds) throws Exception {
		waitForElementDisplayed(getWebElement(locator, element), seconds);
	}

	public void waitForElementDisplayed(WebElement element) throws Exception {
		waitForElementDisplayed(element, 5);
	}

	public void waitForElementDisplayed(WebElement element, int seconds)
			throws Exception {
		// wait for up to XX seconds for our error message
		long end = System.currentTimeMillis() + (seconds * 1000);
		while (System.currentTimeMillis() < end) {
			// If results have been returned, the results are displayed in a
			// drop down.
			if (element.isDisplayed()) {
				break;
			}
		}
	}

	// ////////////////////////////////////
	// checking element functionality
	// ////////////////////////////////////

	// a method for checking id an element is displayed
	public void checkElementDisplayed(Locators locator, String element)
			throws Exception {
		checkElementDisplayed(getWebElement(locator, element));
	}

	public void checkElementDisplayed(WebElement element) throws Exception {
		assertTrue(element.isDisplayed());
	}

	// ///////////////////////////////////
	// selenium actions functionality
	// ///////////////////////////////////

	// our generic selenium click functionality implemented
	public void click(Locators locator, String element) throws Exception {
		click(getWebElement(locator, element));
	}

	public void click(WebElement element) {
		Actions selAction = new Actions(driver);
		selAction.click(element).perform();
	}

	// a method to simulate the mouse hovering over an element
	public void hover(Locators locator, String element) throws Exception {
		hover(getWebElement(locator, element));
	}

	public void hover(WebElement element) throws Exception {
		Actions selAction = new Actions(driver);
		selAction.moveToElement(element).perform();
	}

	// our generic selenium type functionality
	public void type(Locators locator, String element, String text)
			throws Exception {
		type(getWebElement(locator, element), text);
	}

	public void type(WebElement element, String text) {
		Actions selAction = new Actions(driver);
		selAction.sendKeys(element, text).perform();
	}

	// //////////////////////////////////
	// extra base selenium functionality
	// //////////////////////////////////

	// a method to grab the web element using selenium webdriver
	public WebElement getWebElement(Locators locator, String element)
			throws Exception {
		By byElement;
		switch (locator) { // determine which locator item we are interested in
		case xpath: {
			byElement = By.xpath(element);
			break;
		}
		case id: {
			byElement = By.id(element);
			break;
		}
		case name: {
			byElement = By.name(element);
			break;
		}
		case classname: {
			byElement = By.className(element);
			break;
		}
		case linktext: {
			byElement = By.linkText(element);
			break;
		}
		case paritallinktext: {
			byElement = By.partialLinkText(element);
			break;
		}
		case tagname: {
			byElement = By.tagName(element);
			break;
		}
		default: {
			throw new Exception();
		}
		}
		WebElement query = driver.findElement(byElement); // grab our element
															// based on the
															// locator
		return query; // return our query
	}

	// method to grab list of web elements from the page
	public List<WebElement> getWebElements(Locators locator, String element)
			throws Exception {
		By byElement;
		switch (locator) { // determine which locator item we are interested in
		case xpath: {
			byElement = By.xpath(element);
			break;
		}
		case id: {
			byElement = By.id(element);
			break;
		}
		case name: {
			byElement = By.name(element);
			break;
		}
		case classname: {
			byElement = By.className(element);
			break;
		}
		case linktext: {
			byElement = By.linkText(element);
			break;
		}
		case paritallinktext: {
			byElement = By.partialLinkText(element);
			break;
		}
		case tagname: {
			byElement = By.tagName(element);
			break;
		}
		default: {
			throw new Exception();
		}
		}
		List<WebElement> query = driver.findElements(byElement); // grab our
																	// elements
		// based on the
		// locator
		return query; // return our query
	}

	// a method to obtain screenshots
	public void takeScreenshot(String action) throws IOException {
		// make our screenshot name friendly
		action = action.replaceAll("[^a-zA-Z0-9]", "");

		// take a screenshot
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		FileUtils.copyFile(scrFile,
				new File("target/" + System.currentTimeMillis() + action
						+ ".png"));
	}

}
