package com.adesso.main;

import org.openqa.selenium.By;

public class Variables {
public static By Find_Owner=By.linkText("FIND OWNERS");
public static By Add_Owner=By.linkText("Add Owner");

public static By firstname=By.id("firstName");
public static By lastname=By.id("lastName");
public static By address=By.id("address");

public static By city=By.id("city");
public static By telefon=By.id("telefone");

public static By submit_button=By.cssSelector("button[type='submit']");

public static By Owner_Information=By.xpath("//h2[normalize-space()='Owner Information']");
public static By Add_new_pet=By.linkText("Add New Pet");

public static By Pet_name=By.id("name");

public static By Pet_birthday=By.id("birthDate");

public static By Find_Owner_button=By.xpath("//*[@id=\"search-owner-form\"]/div[2]/div/button");


public static By lastVisit = By.xpath("(//a[normalize-space()='Add Visit'])[last()]");

public static By description = By.id("description");

}
