package org.springframework.samples.petclinic.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

public class EndToEndTest {

	By titleBy = By.tagName("h2");

	By activeTabBy = By.className("active");

	By tabLinkBy = By.tagName("a");

	ChromeDriver driver;

	@BeforeAll
	static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	void setupTest() {
		driver = new ChromeDriver();
	}

	@AfterEach
	void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

}
