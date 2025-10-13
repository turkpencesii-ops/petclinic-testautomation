package com.adesso.tests;

import com.adesso.main.Variables;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AddOwnerTest extends Variables {

	WebDriver driver;

	WebDriverWait wait;

	@BeforeEach
	void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		driver.get("" + "");
	}

	@Test
	void addOwner_shouldShowOwnerInformation() {

		wait.until(ExpectedConditions.elementToBeClickable(Variables.Find_Owner)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Variables.Add_Owner)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.firstname)).sendKeys("Mihael");
		driver.findElement(Variables.lastname).sendKeys("Daniel");
		driver.findElement(Variables.address).sendKeys("BaumStrasse 19");
		driver.findElement(Variables.city).sendKeys("Nürnberg");
		driver.findElement(Variables.telefon).sendKeys("65323312232");

		wait.until(ExpectedConditions.elementToBeClickable(Variables.submit_button)).click();

		WebElement ownerInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Owner_Information));
		assertTrue(ownerInfo.isDisplayed());

	}

	@AfterEach
	void tearDown() {
		if (driver != null)
			driver.quit();
	}

}
