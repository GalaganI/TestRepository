package pageObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import accessories.Person;
import accessories.SeleniumWebDriver;
import accessories.SeleniumWebDriver.Locators;

public class ResultsPage {

	// ResultsPage fields
	private SeleniumWebDriver selenium; // instantiating SeleniumWebdriver
	private List<WebElement> name;// list of web elements that
									// identify each found
									// contractors name
	private List<WebElement> jobTitles;// //list of web elements that identify
										// each found contractors jobTitle
	private List<WebElement> skills;// list of web elements that
									// identify every found
									// contractor's list of skills
	private List<WebElement> description;// list of web elements
											// that identify each
											// found contractor's
											// description
	private Locators locator = Locators.xpath;
	private String nameIdentifier = ".//h2/a[@class='oLoadContractor']";
	private String skillsIdentifier = ".//ul[@class='oSkillsList oInlineList']";
	private String descriptionIdentifier = ".//section[@class='oContractorDescription']/p";
	private String jobtitleIdentifier = ".//h3[@class='oAutoWrap oContractorTagline notranslate']";

	// ResultsPage constructor
	public ResultsPage(SeleniumWebDriver selenium) throws Exception {

		this.selenium = selenium;
		name = getSResults(locator, nameIdentifier);
		skills = getSResults(locator, skillsIdentifier);
		description = getSResults(locator, descriptionIdentifier);
		jobTitles = getSResults(locator, jobtitleIdentifier);

	}

	// grabs all search result's headers from the page and returns them as a
	// List
	public List<WebElement> getSResults(Locators locator,
			String elementIdentifier) throws Exception {
		return selenium.getWebElements(locator, elementIdentifier);

	}

	// method generates random numbers
	public int generateRandomNumber() {
		Random randomNumbers = new Random();// generates random numbers
		int number = 0 + randomNumbers.nextInt(9);
		System.out.println("Random number = " + number);
		return number;

	}

	// method closes drop down bar
	public void closeDropDownBar() throws Exception {
		if (selenium.getWebElement(Locators.xpath, "/html/body/div/section")
				.isDisplayed()) {
			JavascriptExecutor js = (JavascriptExecutor) selenium.getDriver();
			js.executeScript("document.getElementByXPath('/html/body/div/section').setAttribute('style', 'display: none;')");
		}
	}

	// getting to a random contractors page

	public ContractorPage getToRandomContractorsPage() throws Exception {
		int index = generateRandomNumber();
		if (name.get(index).isDisplayed()) {
			selenium.click(name.get(index));
			selenium.wait(5);
			System.out.println("Click on random contractors title");
			return new ContractorPage(selenium);
		} else {
			closeDropDownBar();
			selenium.click(name.get(index));
			System.out.println("Click on random contractors title");
			selenium.wait(5);
			return new ContractorPage(selenium);
		}

	}

	public Person parseResult(int index) {
		Person person = new Person();
		person.setName(name.get(index).getText());
		person.setJobTitle(jobTitles.get(index).getText());
		person.setDescription(description.get(index).getText());
		person.setSkills(skills.get(index).getText());
		return person;
	}

	public List<Person> parseAllResults() {
		List<Person> allpersons = new ArrayList<Person>();
		for (int i = 0; i < name.size(); i++)
			allpersons.add(this.parseResult(i));
		return allpersons;
	}
}
