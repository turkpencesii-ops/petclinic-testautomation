package com.adesso.tests;

import com.adesso.main.Variables;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class OwnerAddAndEditPetForFelixTest extends Variables {

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
	void findOwner_addPet_thenEditPet() throws InterruptedException {
		// ---------- Owner suchen ----------
		wait.until(ExpectedConditions.elementToBeClickable(Variables.Find_Owner)).click();
		WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.lastname));
		lastName.clear();
		lastName.sendKeys("Souza");
		driver.findElement(Variables.submit_button).click();

		// Owner-Detailseite sichtbar
		wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Owner_Information));
		assertTrue(driver.getPageSource().contains("Alex Souza"));

		// ---------- Neues Pet hinzufügen ----------
		wait.until(ExpectedConditions.elementToBeClickable(Variables.Add_new_pet)).click();
		wait.until(ExpectedConditions.urlMatches(".*/owners/\\d+/pets/new$"));

		Random rand = new Random();
		int n = rand.nextInt(90) + 10;

		String petName = "Buddy" + n;
		wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Pet_name)).sendKeys(petName);

		WebElement birth = driver.findElement(Variables.Pet_birthday);
		((JavascriptExecutor) driver).executeScript("arguments[0].value='2024-06-10';", birth);

		new Select(driver.findElement(By.id("type"))).selectByVisibleText("dog");

		WebElement saveBtn = wait
			.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", saveBtn);
		saveBtn.click();

		// Pet muss auf Owner-Seite erscheinen
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Owner Information']")));
		assertTrue(driver.getPageSource().contains(petName), "Neues Pet sollte sichtbar sein.");

		// ---------- Letztes Pet bearbeiten ----------
		By lastEdit = By.xpath("(//a[normalize-space()='Edit Pet'])[last()]");
		WebElement edit = wait.until(ExpectedConditions.elementToBeClickable(lastEdit));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", edit);
		edit.click();

		wait.until(ExpectedConditions.urlMatches(".*/owners/\\d+/pets/\\d+/edit$"));
		WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		nameInput.clear();
		String editedName = petName + "-Edited";
		nameInput.sendKeys(editedName);

		WebElement saveEdit = driver.findElement(Variables.submit_button);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", saveEdit);
		saveEdit.click();

		// Änderung prüfen
		wait.until(ExpectedConditions.visibilityOfElementLocated(Variables.Owner_Information));
		assertTrue(driver.getPageSource().contains(editedName), "Bearbeiteter Pet-Name sollte sichtbar sein.");

		Thread.sleep(2000);
	}

	@AfterEach
	void tearDown() {
		if (driver != null)
			driver.quit();
	}

}
