package org.springframework.samples.petclinic.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomePageTests extends EndToEndTest {

	@LocalServerPort
	private int port;

	String homePageLink = "http://localhost:%d";

	String homePageLinkAlt = "http://localhost:%d/";

	String errorPageLink = "http://localhost:%d/oups";

	@Test
	void whenEnterHomePageThenPageDisplayCorrectly() {
		driver.get(String.format(homePageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		homeDisplayCorrectly();
	}

	@Test
	void whenClickHomeButtonThenDirectToHomePage() {
		driver.get(String.format(errorPageLink, port));
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		WebElement homeTabLink = driver.findElementByXPath("//a[@title='home page']");
		homeTabLink.click();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Assertions.assertTrue(driver.getCurrentUrl().equals(String.format(homePageLink, port))
				|| driver.getCurrentUrl().equals(String.format(homePageLinkAlt, port)));
		homeDisplayCorrectly();
	}

	private void homeDisplayCorrectly() {
		Assertions.assertEquals(driver.findElement(titleBy).getText(), "Welcome");
		WebElement activeTab = driver.findElement(activeTabBy);
		WebElement tabLink = activeTab.findElement(tabLinkBy);
		Assertions.assertEquals(tabLink.getAttribute("title"), "home page");
		Assertions.assertTrue(tabLink.isDisplayed());
	}

}
