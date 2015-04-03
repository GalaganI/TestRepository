package pageObjects;

import org.openqa.selenium.WebElement;

import accessories.Person;
import accessories.SeleniumWebDriver;
import accessories.SeleniumWebDriver.Locators;



public class ContractorPage {

	// Class fields
	private SeleniumWebDriver selenium;
	private WebElement name;
	private WebElement jobTitle;
	private WebElement skills;
	private WebElement description;
	private String nameIdentifier = ".//hgroup/h1[@class='oH1Huge']";
	private String jobTitleIdentifier = ".//hgroup/h1[@class='oH2High']";
	private String skillsIdentifier = ".//section[contains(@class,'oExpandableOneLine oSkills')]";
	private String descriptionIdentifier = ".//p[@class='notranslate']";
	private Locators locator = Locators.xpath;

	// Constructor
	public ContractorPage(SeleniumWebDriver selenium) throws Exception {
		this.selenium = selenium;
		name = selenium.getWebElement(locator, nameIdentifier);
		jobTitle = selenium.getWebElement(locator, jobTitleIdentifier);
		skills = selenium.getWebElement(locator, skillsIdentifier);
		description = selenium.getWebElement(locator, descriptionIdentifier);

	}

	public String getNameText() {
		return name.getText();
	}

	public String getJobtitleText() {
		return jobTitle.getText();
	}

	public String getSkillsText() throws Exception {
		if ((skills.getAttribute("class"))
				.equals("oExpandableOneLine oSkills oCollapsed")) {
			selenium.click(Locators.xpath, ".//a[@class='oMore']");
			return skills.getText();
		} else
			return skills.getText();
	}

	public String getDescriptionText() {
		return description.getText();
	}

	// Wrapping all Contractors details in a Person object.
	public Person saveContractordetails() throws Exception {
		Person randContractor = new Person();
		randContractor.setName(getNameText());
		randContractor.setJobTitle(getJobtitleText());
		randContractor.setSkills(getSkillsText());
		randContractor.setDescription(getDescriptionText());
		System.out.println("Get  contractor's " + randContractor.getName()
				+ " details");
		return randContractor;
	}

}
