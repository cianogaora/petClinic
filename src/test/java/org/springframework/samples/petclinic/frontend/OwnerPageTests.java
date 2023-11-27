package org.springframework.samples.petclinic.frontend;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerPageTests extends EndToEndTest {

	@LocalServerPort
	private int port;

	By tableBodyBy = By.tagName("tbody");

	By tableRowBy = By.tagName("tr");

	By tableDataColBy = By.tagName("td");

	By tableHeadColBy = By.tagName("th");

	String ownerPageLink = "http://localhost:%d/owners/find";

	String ownerPageLinkAlt = "http://localhost:%d/owners/find/";

	String newOwnerPageLink = "http://localhost:%d/owners/new";

	String errorPageLink = "http://localhost:%d/oups";

	@Test
	void whenEnterVetsPageThenPageDisplayCorrectly() {
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		ownerPageDisplayCorrectly();
	}

	@Test
	void whenClickOwnerButtonThenDirectToOwnerPage() {
		driver.get(String.format(errorPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement vetsTabLink = driver.findElementByXPath("//a[@title='find owners']");
		vetsTabLink.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.getCurrentUrl().equals(String.format(ownerPageLink, port))
				|| driver.getCurrentUrl().equals(String.format(ownerPageLinkAlt, port)));
		ownerPageDisplayCorrectly();
	}

	@Test
	void whenOpenNewOwnerPageAndInputValidInfoThenOwnerCreated() {
		Map<String, String> mockOwnerData = newUser();
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addOwnerButton = driver.findElementByXPath("//a[@href='/owners/new']");
		Assertions.assertTrue(addOwnerButton.isDisplayed());
		addOwnerButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "Owner");
		Assertions.assertTrue(driver.getCurrentUrl().equals(String.format(newOwnerPageLink, port)));
		createNewUser(mockOwnerData);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "Owner Information");
		// table displays
		WebElement table = driver.findElement(By.tagName("table"));
		Assertions.assertTrue(table.isDisplayed());
		// table has 4 rows. first and last row display correctly
		verifyOwnerInOwnerInfoPage(driver, mockOwnerData);
	}

	@Test
	void whenOpenNewOwnerPageAndInputEmptyFirstnameThenOwnerCreationFails() {
		Map<String, String> mockOwnerData = newUser();
		mockOwnerData.put("mockFirstName", "");
		inputInvalidOwnerInfoAndExpectError(mockOwnerData);
	}

	@Test
	void whenOpenNewOwnerPageAndInputEmptyLastnameThenOwnerCreationFails() {
		Map<String, String> mockOwnerData = newUser();
		mockOwnerData.put("mockLastName", "");
		inputInvalidOwnerInfoAndExpectError(mockOwnerData);
	}

	@Test
	void whenOpenNewOwnerPageAndInputEmptyAddressThenOwnerCreationFails() {
		Map<String, String> mockOwnerData = newUser();
		mockOwnerData.put("mockAddress", "");
		inputInvalidOwnerInfoAndExpectError(mockOwnerData);
	}

	@Test
	void whenOpenNewOwnerPageAndInputEmptyCityThenOwnerCreationFails() {
		Map<String, String> mockOwnerData = newUser();
		mockOwnerData.put("mockCity", "");
		inputInvalidOwnerInfoAndExpectError(mockOwnerData);
	}

	@Test
	void whenOpenNewOwnerPageAndInputEmptyTelephoneThenOwnerCreationFails() {
		Map<String, String> mockOwnerData = newUser();
		mockOwnerData.put("mockTelephone", "");
		inputInvalidOwnerInfoAndExpectError(mockOwnerData);
	}

	@Test
	void whenOpenNewOwnerPageAndInputInvalidTelephoneThenOwnerCreationFails() {
		Map<String, String> mockOwnerData = newUser();
		mockOwnerData.put("mockTelephone", "abc");
		inputInvalidOwnerTelephoneAndExpectError(mockOwnerData);
	}

	@Test
	void whenOpenNewPetPageAndInputValidInfoThenPetCreated() {
		Map<String, String> petInfo = newPet();
		Map<String, String> owner = newUser();
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton = driver.findElement(By.linkText("Add New Pet"));
		Assertions.assertTrue(addPetButton.isDisplayed());
		addPetButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		verifyOwnerInOwnerInfoPage(driver, owner);
		verifyPetInOwnerInfoPage(driver, petInfo);
	}

	@Test
	void whenOpenNewPetPageAndInputNoNameThenPetCreationFailed() {
		Map<String, String> petInfo = newPet();
		petInfo.put("mockName", "");
		Map<String, String> owner = newUser();
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton = driver.findElement(By.linkText("Add New Pet"));
		Assertions.assertTrue(addPetButton.isDisplayed());
		addPetButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.findElement(By.className("help-inline")).getText().contains("is required"));
	}

	@Test
	void whenOpenNewPetPageAndInputNoBirthDateThenPetCreationFailed() {
		Map<String, String> petInfo = newPet();
		Map<String, String> owner = newUser();
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton = driver.findElement(By.linkText("Add New Pet"));
		Assertions.assertTrue(addPetButton.isDisplayed());
		addPetButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton2 = driver.findElement(By.linkText("Add New Pet"));
		addPetButton2.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.findElement(By.className("help-inline")).getText().contains("is already in use"));
	}

	@Test
	void whenOpenNewPetPageAndInputNameThatIsIdenticalToAnotherPetOfThisOwnerThenPetCreationFailed() {
		Map<String, String> petInfo = newPet();
		petInfo.put("mockBirthDate", "");
		Map<String, String> owner = newUser();
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton = driver.findElement(By.linkText("Add New Pet"));
		Assertions.assertTrue(addPetButton.isDisplayed());
		addPetButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.findElement(By.className("help-inline")).getText().contains("is required"));
	}

	@Test
	void whenOpenSearchOwnerPageAndInputValidInfoThenFoundOwner() {
		Map<String, String> owner = newUser();
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement lastNameSearchBox = driver.findElementByXPath("//input[@id='lastName']");
		WebElement submitButton = driver.findElementByXPath("//button[@type='submit']");
		lastNameSearchBox.sendKeys(owner.get("mockLastName"));
		submitButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		verifyOwnerInOwnerInfoPage(driver, owner);
	}

	@Test
	void whenOpenSearchOwnerPageAndInputNonExistLastNameThenFoundNoOwner() {
		Map<String, String> owner = newUser();
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement lastNameSearchBox = driver.findElementByXPath("//input[@id='lastName']");
		WebElement submitButton = driver.findElementByXPath("//button[@type='submit']");
		lastNameSearchBox.sendKeys(owner.get("mockLastName"));
		submitButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertNotNull(driver.findElement(By.xpath(String.format("//p[text()='%s']", "has not been found"))));
	}

	@Test
	void whenOpenSearchOwnerPageAndInputEmptyLastThenThenFoundAllOwner() {
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement submitButton = driver.findElementByXPath("//button[@type='submit']");
		submitButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement tableBody = driver.findElement(tableBodyBy);
		List<WebElement> rows = tableBody.findElements(tableRowBy);
		Assertions.assertTrue(rows.size() > 5);
	}

	@Test
	void whenOpenVisitPageAndInputValidInfoThenVisitCreated() {
		Map<String, String> petInfo = newPet();
		Map<String, String> owner = newUser();
		Map<String, String> visitInfo = newVisit();
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton = driver.findElement(By.linkText("Add New Pet"));
		Assertions.assertTrue(addPetButton.isDisplayed());
		addPetButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		verifyOwnerInOwnerInfoPage(driver, owner);
		verifyPetInOwnerInfoPage(driver, petInfo);
		WebElement addVisitButton = driver.findElement(By.linkText("Add Visit"));
		addVisitButton.click();
		createNewVisit(visitInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		verifyVisitInOwnerInfoPage(driver, visitInfo);
	}

	@Test
	void whenOpenVisitPageAndInputEmptyDescriptionThenVisitCreated() {
		Map<String, String> petInfo = newPet();
		Map<String, String> owner = newUser();
		Map<String, String> visitInfo = newVisit();
		visitInfo.put("mockDescription", "");
		driver.get(String.format(newOwnerPageLink, port));
		createNewUser(owner);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addPetButton = driver.findElement(By.linkText("Add New Pet"));
		Assertions.assertTrue(addPetButton.isDisplayed());
		addPetButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewPet(petInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		verifyOwnerInOwnerInfoPage(driver, owner);
		verifyPetInOwnerInfoPage(driver, petInfo);
		WebElement addVisitButton = driver.findElement(By.linkText("Add Visit"));
		addVisitButton.click();
		createNewVisit(visitInfo);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.findElement(By.className("help-inline")).getText().contains("must not be empty"));
	}

	private void inputInvalidOwnerInfoAndExpectError(Map<String, String> mockOwnerData) {
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addOwnerButton = driver.findElementByXPath("//a[@href='/owners/new']");
		Assertions.assertTrue(addOwnerButton.isDisplayed());
		addOwnerButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewUser(mockOwnerData);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.findElement(By.className("help-inline")).getText().contains("must not be empty"));
	}

	private void inputInvalidOwnerTelephoneAndExpectError(Map<String, String> mockOwnerData) {
		driver.get(String.format(ownerPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement addOwnerButton = driver.findElementByXPath("//a[@href='/owners/new']");
		Assertions.assertTrue(addOwnerButton.isDisplayed());
		addOwnerButton.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		createNewUser(mockOwnerData);
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertEquals(driver.findElement(By.className("help-inline")).getText(),
				"numeric value out of bounds (<10 digits>.<0 digits> expected)");
	}

	private void createNewVisit(Map<String, String> visitInfo) {
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "New Visit");

		WebElement dateInput = driver.findElementById("date");
		WebElement descriptionInput = driver.findElementById("description");

		dateInput.clear();
		dateInput.sendKeys(visitInfo.get("mockDate"));
		descriptionInput.sendKeys(visitInfo.get("mockDescription"));

		WebElement submitButton = driver.findElementByXPath("//button[@type='submit']");
		submitButton.click();
	}

	private void createNewPet(Map<String, String> petInfo) {
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "New Pet");

		WebElement nameInput = driver.findElementById("name");
		WebElement birthDateInput = driver.findElementById("birthDate");
		Select typeOption = new Select(driver.findElementById("type"));

		nameInput.sendKeys(petInfo.get("mockName"));
		birthDateInput.sendKeys(petInfo.get("mockBirthDate"));
		typeOption.selectByVisibleText(petInfo.get("mockType"));

		WebElement submitButton = driver.findElementByXPath("//button[@type='submit']");
		submitButton.click();
	}

	private void createNewUser(Map<String, String> mockOwnerData) {
		WebElement firstNameInput = driver.findElementById("firstName");
		WebElement lastNameInput = driver.findElementById("lastName");
		WebElement addressInput = driver.findElementById("address");
		WebElement cityInput = driver.findElementById("city");
		WebElement telephoneInput = driver.findElementById("telephone");

		firstNameInput.sendKeys(mockOwnerData.get("mockFirstName"));
		lastNameInput.sendKeys(mockOwnerData.get("mockLastName"));
		addressInput.sendKeys(mockOwnerData.get("mockAddress"));
		cityInput.sendKeys(mockOwnerData.get("mockCity"));
		telephoneInput.sendKeys(mockOwnerData.get("mockTelephone"));

		WebElement submitButton = driver.findElementByXPath("//button[@type='submit']");
		submitButton.click();
	}

	private Map<String, String> newVisit() {
		Map<String, String> visitInfo = new HashMap<>();
		String mockDate = "2020-01-01";
		String mockDescription = "cat";
		visitInfo.put("mockDate", mockDate);
		visitInfo.put("mockDescription", mockDescription);
		return visitInfo;
	}

	private Map<String, String> newPet() {
		Map<String, String> petInfo = new HashMap<>();
		String mockName = RandomStringUtils.random(6, true, false);
		String mockBirthDate = "2020-01-01";
		String mockType = "cat";
		petInfo.put("mockName", mockName);
		petInfo.put("mockBirthDate", mockBirthDate);
		petInfo.put("mockType", mockType);
		return petInfo;
	}

	private Map<String, String> newUser() {
		String mockFirstName = RandomStringUtils.random(6, true, false);
		String mockLastName = RandomStringUtils.random(6, true, false);
		String mockAddress = RandomStringUtils.random(20, true, true);
		String mockCity = RandomStringUtils.random(7, true, false);
		String mockTelephone = RandomStringUtils.random(9, false, true);
		Map<String, String> mockOwnerData = new HashMap<>();
		mockOwnerData.put("mockFirstName", mockFirstName);
		mockOwnerData.put("mockLastName", mockLastName);
		mockOwnerData.put("mockAddress", mockAddress);
		mockOwnerData.put("mockCity", mockCity);
		mockOwnerData.put("mockTelephone", mockTelephone);
		return mockOwnerData;
	}

	private void verifyOwnerInOwnerInfoPage(ChromeDriver driver, Map<String, String> ownerInfo) {
		WebElement tableBody = driver.findElement(tableBodyBy);
		List<WebElement> rows = tableBody.findElements(tableRowBy);
		Assertions.assertEquals(rows.size(), 4);
		WebElement firstRow = rows.get(0);
		Assertions.assertEquals(firstRow.findElement(tableHeadColBy).getText(), "Name");
		Assertions.assertEquals(firstRow.findElement(tableDataColBy).getText(),
				ownerInfo.get("mockFirstName") + " " + ownerInfo.get("mockLastName"));
		WebElement secondRow = rows.get(1);
		Assertions.assertEquals(secondRow.findElement(tableHeadColBy).getText(), "Address");
		Assertions.assertEquals(secondRow.findElement(tableDataColBy).getText(), ownerInfo.get("mockAddress"));
		WebElement thirdRow = rows.get(2);
		Assertions.assertEquals(thirdRow.findElement(tableHeadColBy).getText(), "City");
		Assertions.assertEquals(thirdRow.findElement(tableDataColBy).getText(), ownerInfo.get("mockCity"));
		WebElement forthRow = rows.get(3);
		Assertions.assertEquals(forthRow.findElement(tableHeadColBy).getText(), "Telephone");
		Assertions.assertEquals(forthRow.findElement(tableDataColBy).getText(), ownerInfo.get("mockTelephone"));
	}

	private void verifyPetInOwnerInfoPage(ChromeDriver driver, Map<String, String> petInfoMap) {
		List<WebElement> petInfo = driver.findElements(By.tagName("dd"));
		Assertions.assertEquals(petInfo.get(0).getText(), petInfoMap.get("mockName"));
		Assertions.assertEquals(petInfo.get(1).getText(), petInfoMap.get("mockBirthDate"));
		Assertions.assertEquals(petInfo.get(2).getText(), petInfoMap.get("mockType"));
	}

	private void verifyVisitInOwnerInfoPage(ChromeDriver driver, Map<String, String> visitInfoMap) {
		Assertions.assertNotNull(
				driver.findElement(By.xpath(String.format("//td[text()='%s']", visitInfoMap.get("mockDate")))));
		Assertions.assertNotNull(
				driver.findElement(By.xpath(String.format("//td[text()='%s']", visitInfoMap.get("mockDescription")))));
	}

	private void ownerPageDisplayCorrectly() {
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "Find Owners");
		WebElement activeTab = driver.findElement(activeTabBy);
		WebElement tabLink = activeTab.findElement(tabLinkBy);
		Assertions.assertTrue(tabLink.isDisplayed());
	}

}
