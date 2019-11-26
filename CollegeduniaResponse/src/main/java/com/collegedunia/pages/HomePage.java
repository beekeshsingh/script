package com.collegedunia.pages;
 
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.collegedunia.base.TestBase;

public class HomePage extends TestBase {

	@FindBy(xpath="")
	WebElement logo_img;
	
	// initialize the object of class
	public HomePage() {
		PageFactory.initElements(driver, this);
	}
	
	public String verifyTitleOfHomePage() {
		return driver.getTitle();
	}
	
}
