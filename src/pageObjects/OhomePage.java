package pageObjects;

import org.openqa.selenium.WebDriver;

import accessories.SeleniumWebDriver;
import accessories.SeleniumWebDriver.Locators;

public class OhomePage {
	private SeleniumWebDriver selenium;
	private WebDriver driver;

	public OhomePage(SeleniumWebDriver selenium , String appURL) {
		this.selenium = selenium;
		this.driver=selenium.getDriver();
		driver.get(appURL);
		System.out.println("Go to www.odesk.com");

	}

	public SearchPage getAnotherApplicationPage(Locators locator,
			String pointerToElement) {
		try {
			selenium.click(locator, pointerToElement);
			selenium.wait(5);
			System.out.println("Click on 'Find Contractor'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SearchPage(selenium);
	}

}
