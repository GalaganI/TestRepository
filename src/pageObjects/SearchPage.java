package pageObjects;

import accessories.SeleniumWebDriver;
import accessories.SeleniumWebDriver.Locators;

public class SearchPage {
	private SeleniumWebDriver selenium;// instance of SeleniumWebdriver

//	Constructor 
	public SearchPage(SeleniumWebDriver selenium) {
		this.selenium = selenium;

	}
// initiating search 
	public ResultsPage search(Locators locator, String locatorIdentifier) throws Exception {
		selenium.click(selenium.getWebElement(locator, locatorIdentifier));
		System.out.println("submit form");
		return new ResultsPage(selenium);
	}
//type keyword in a search field 
	public void typeKeyword(Locators locator, String locatorIdentifier,
			String keyword) throws Exception{
		selenium.type(locator,locatorIdentifier,keyword);
		System.out.println("Enter <keyword>="+keyword);
	}
}
