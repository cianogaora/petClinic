package org.springframework.samples.petclinic.frontend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetPageTests extends EndToEndTest {

	@LocalServerPort
	private int port;

	By activeTabBy = By.className("active");

	By tabLinkBy = By.tagName("a");

	By tableIdBy = By.id("vets");

	By tableBodyBy = By.tagName("tbody");

	By tableRowBy = By.tagName("tr");

	By tableColBy = By.tagName("td");

	String vetsPageLink = "http://localhost:%d/vets.html";

	String vetsPageLinkAlt = "http://localhost:%d/vets.html/";

	String errorPageLink = "http://localhost:%d/oups";

	@Test
	void whenEnterVetsPageThenPageDisplayCorrectly() {
		driver.get(String.format(vetsPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		vetsPageDisplayCorrectly();
	}

	@Test
	void whenClickVetsButtonThenDirectToVetsPage() {
		driver.get(String.format(errorPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement vetsTabLink = driver.findElementByXPath("//a[@title='veterinarians']");
		vetsTabLink.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.getCurrentUrl().equals(String.format(vetsPageLink, port))
				|| driver.getCurrentUrl().equals(String.format(vetsPageLinkAlt, port)));
		vetsPageDisplayCorrectly();
	}

	private void vetsPageDisplayCorrectly() {
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "Veterinarians");
		WebElement activeTab = driver.findElement(activeTabBy);
		WebElement tabLink = activeTab.findElement(tabLinkBy);

		Assertions.assertTrue(tabLink.isDisplayed());

		// title shows up correctly
		String title = tabLink.getAttribute("title");
		Assertions.assertEquals(title, "veterinarians");

		// table displays
		WebElement table = driver.findElement(tableIdBy);
		Assertions.assertTrue(table.isDisplayed());

		// table has 6 rows. first and last row display correctly
		WebElement tableBody = driver.findElement(tableBodyBy);
		List<WebElement> rows = tableBody.findElements(tableRowBy);
		Assertions.assertEquals(rows.size(), 6);
		List<WebElement> firstRow = rows.get(0).findElements(tableColBy);
		Assertions.assertEquals(firstRow.get(0).getText(), "James Carter");
		Assertions.assertEquals(firstRow.get(1).getText(), "none");
		List<WebElement> lastRow = rows.get(5).findElements(tableColBy);
		Assertions.assertEquals(lastRow.get(0).getText(), "Sharon Jenkins");
		Assertions.assertEquals(lastRow.get(1).getText(), "none");
	}

}
