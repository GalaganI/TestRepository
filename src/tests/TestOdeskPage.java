package tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import pageObjects.ContractorPage;
import pageObjects.OhomePage;
import pageObjects.ResultsPage;
import pageObjects.SearchPage;
import accessories.Person;
import accessories.SeleniumWebDriver;
import accessories.SeleniumWebDriver.Browsers;
import accessories.SeleniumWebDriver.Locators;

public class TestOdeskPage {

	private SeleniumWebDriver selenium;
	private WebDriver driver;
	public static List<Person> results;
	private static final String baseURL = "http://odesk.com";//introduce base URL
	private static final String keyword = "tester";//introduce keyword for search

	@Before
	public void init() {
		selenium = new SeleniumWebDriver(Browsers.Chrome);//set specific driver type  
		driver = selenium.getDriver();
		driver.manage().window().maximize();
		selenium.deleteCookies();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void TestPage() throws Exception {
		OhomePage homepage = new OhomePage(selenium, baseURL);
		SearchPage searchpage = homepage.getAnotherApplicationPage(
				Locators.linktext, "Hire Freelancers");
		searchpage.typeKeyword(Locators.xpath, ".//input[@name='q']", keyword);
		ResultsPage resultpage = searchpage.search(Locators.xpath,
				".//input[@type='submit']");
		System.out
				.println("Parse 1st page with search results: store info given on the 1st page of search results");
		 results = resultpage.parseAllResults();
		for (Person per : results) {
			per.print();
			assertTrue("Contractor" + per.getName()
					+ " doesn't contain required keyword",
					per.contain(keyword));
			System.out
					.println("At least one attribute (title, objective, skills, etc) of the current item  contains word "
							+ keyword);
		}

		ContractorPage contractorpage = resultpage.getToRandomContractorsPage();
		Person randomContractor = contractorpage.saveContractordetails();
		for (Person ex : results) {
			if (ex.getName().equalsIgnoreCase(randomContractor.getName())) {
				randomContractor.print();
				assertTrue("Some attributes didn't match",
						ex.equals(randomContractor));
				System.out
						.println("\n Each attribute value is equal to one of those stored previously");

			}
		}
		assertTrue("Contractor" + randomContractor.getName()
				+ "doesn't contain required keyword",
				randomContractor.contain(keyword));
		System.out.println("Contractor " +randomContractor.getName()+" contains required keyword in his attributes");
	}

}
