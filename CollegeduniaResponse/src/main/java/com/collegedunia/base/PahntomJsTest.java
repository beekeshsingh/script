package com.collegedunia.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.collegedunia.utils.TestUtil;

public class PahntomJsTest {

	public static void main(String[] args) {
		WebDriver driver;
		System.setProperty(TestUtil.PHANTOMJS_DRIVER, TestUtil.PHANTOMJS_DRIVER_PATH_WIN);
		driver = new PhantomJSDriver();
		driver.get("https://collegedunia.com");
		String title = driver.getTitle();
		System.out.println(title);
	}
}
