package com.adesso.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class AddPetTest {

	WebDriver driver;
	WebDriverWait wait;

	@BeforeEach
	void setUp() {
		System.setProperty("webdriver.http.factory","jdk-http-client");
		WebDriverManager.chromedriver().setup();

		ChromeOptions opts = new ChromeOptions();
		opts.addArguments("--headless=new","--window-size=1920,1080");
		driver = new ChromeDriver(opts);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		wait = new WebDriverWait(driver, Duration.ofSeconds(8));

		driver.get("http://localhost:8080");
	}

	@Test
	void addPet_nameMustBeUniquePerOwner() {
		// Owner anlegen
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("FIND OWNERS"))).click();
		driver.findElement(By.linkText("Add Owner")).click();
		driver.findElement(By.id("firstName")).sendKeys("Pet");
		driver.findElement(By.id("lastName")).sendKeys("Owner" + System.currentTimeMillis());
		driver.findElement(By.id("address")).sendKeys("Teststraße 1");
		driver.findElement(By.id("city")).sendKeys("Heidelberg");
		driver.findElement(By.id("telephone")).sendKeys("123456789");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Owner Information']")));

		// Erstes Pet hinzufügen
		driver.findElement(By.linkText("Add New Pet")).click();
		driver.findElement(By.id("name")).sendKeys("Mia");
		driver.findElement(By.id("birthDate")).sendKeys("2019-01-01");
		new Select(driver.findElement(By.id("type"))).selectByVisibleText("cat");
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		assertTrue(driver.findElements(By.xpath("//dd[contains(.,'Mia')]")).size() > 0);

		// Noch einmal mit demselben Namen versuchen
		driver.findElement(By.linkText("Add New Pet")).click();
		driver.findElement(By.id("name")).sendKeys("Mia");
		driver.findElement(By.id("birthDate")).sendKeys("2020-02-02");
		new Select(driver.findElement(By.id("type"))).selectByVisibleText("cat");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		// Erwartung: Fehlermeldung oder Hinweis, dass der Name bereits existiert
		boolean hasError = driver.findElements(By.cssSelector(".help-inline")).size() > 0
			|| driver.getPageSource().toLowerCase().contains("already exists")
			|| driver.getPageSource().toLowerCase().contains("is already used");

		assertTrue(hasError, "Der gleiche Pet-Name darf für einen Owner nicht doppelt vergeben werden.");
	}

	@AfterEach
	void tearDown() {
		if (driver != null) driver.quit();
	}
}
