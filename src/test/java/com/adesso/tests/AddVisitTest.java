package com.adesso.tests;

import com.adesso.main.Variables;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class AddVisitTest extends Variables {

	WebDriver driver;

	WebDriverWait wait;

	@BeforeEach
	void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		driver.get("http://localhost:8080");

	}

	@Test
	void addVisit_forExistingPet() {
		// Owner "Burak Test" suchen und öffnen
		wait.until(ExpectedConditions.elementToBeClickable(Variables.Find_Owner)).click();
		WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.lastname));
		lastName.clear();
		lastName.sendKeys("Souza");
		driver.findElement(Variables.submit_button).click();
		if (driver.findElements(Variables.Owner_Information).isEmpty()) {
			WebElement ownerInfo = wait
				.until(ExpectedConditions.visibilityOfElementLocated(Variables.Owner_Information));
			assertTrue(ownerInfo.isDisplayed());
		}

		// Falls noch kein Pet existiert: schnell eines anlegen
		if (driver.findElements(By.linkText("Add Visit")).isEmpty()) {
			wait.until(ExpectedConditions.elementToBeClickable(Variables.Add_new_pet)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Pet_name)).sendKeys("VisitPet");
			WebElement birth = driver.findElement(Variables.Pet_birthday);
			((JavascriptExecutor) driver).executeScript("arguments[0].value='2023-05-05';", birth);
			new Select(driver.findElement(By.id("type"))).selectByVisibleText("dog");
			driver.findElement(Variables.submit_button).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Owner_Information));
		}

		// Visit hinzufügen (letztes Pet)
		WebElement visit1 = wait.until(ExpectedConditions.elementToBeClickable(lastVisit));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", visit1);
		visit1.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.description))
			.sendKeys("Routineuntersuchung");
		driver.findElement(Variables.submit_button).click();

		// Prüfung
		wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Owner_Information));
		assertTrue(driver.getPageSource().contains("Routineuntersuchung"), "Visit sollte gelistet sein.");

	}

	@AfterEach
	void tearDown() {
		if (driver != null)
			driver.quit();
	}

}
