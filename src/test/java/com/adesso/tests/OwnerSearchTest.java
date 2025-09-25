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
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OwnerSearchTest {

	WebDriver driver;

	WebDriverWait wait;

	@BeforeEach
	void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		driver.get("http://localhost:8080");

	}

	@Test
	void searchOwners_emptyLastName_shouldListAllOwners() {
		wait.until(ExpectedConditions.elementToBeClickable(Variables.Find_Owner)).click();
		driver.findElement(Variables.Find_Owner_button).click();

		WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("owners")));

		assertTrue(table.isDisplayed(), "All are listed");

	}

	@Test
	void searchOwners_byLastname_shouldShowMatches() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(Variables.Find_Owner)).click();
		WebElement lastname = wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.lastname));
		lastname.clear();
		lastname.sendKeys("Braun");
		driver.findElement(By.xpath("//*[@id=\"search-owner-form\"]/div[2]/div/button")).click();

		// Results: Owner Information pages
		boolean ownerInfoVisible = driver.findElements(By.xpath("/html/body/div/div/h2[1]")).size() > 0;

		boolean tableRowVisible = driver
			.findElements(By.xpath("//table[@id='owners']//td/a[contains(.,'Search Owner')]"))
			.size() > 0;

		assertTrue(ownerInfoVisible || tableRowVisible, "Owner search result should appear");

		Thread.sleep(2000);
	}

	@AfterEach
	void tearDown() {
		if (driver != null)
			driver.quit();
	}

}
