package com.collegedunia.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.collegedunia.utils.TestUtil;

public class TestBase {

	public static WebDriver driver;
	public static Properties properties;
	

	public TestBase() {
		try {
			properties = new Properties();
			FileInputStream fis = null;
			if (TestUtil.OS_NAME.startsWith("Windows")) {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\main\\java\\com\\collegedunia\\config\\config.properties");
			} else if (TestUtil.OS_NAME.startsWith("Linux")) {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "/src/main/java/com/collegedunia/config/config.properties");
			} 
			properties.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void initialization() {
		String browserName = properties.getProperty("browser");
		if (browserName.equals("chrome")) {
			System.out.println(System.getProperty("os.name"));
			if (TestUtil.OS_NAME.startsWith("Windows")) {
				System.setProperty(TestUtil.CHROME_DRIVER, TestUtil.CHROME_DRIVER_PATH_WIN);
			} else if (TestUtil.OS_NAME.startsWith("Linux")) {
				System.setProperty(TestUtil.CHROME_DRIVER, TestUtil.CHROME_DRIVER_PATH_LINUX);
			} 
			
			// Mobile View Setup
			Map<String, String> mobileEmulation = new HashMap<>();
			mobileEmulation.put("deviceName", "Nexus 5");
			ChromeOptions chromeOptions = new ChromeOptions();
			//chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation); 
			//chromeOptions.setHeadless(true);
			
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.BROWSER, Level.ALL);
			chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			chromeOptions.addArguments("headless"); 
			
			driver = new ChromeDriver(chromeOptions);
		} else if (browserName.equals("FF")) {
			System.out.println(System.getProperty("os.name"));
			if (TestUtil.OS_NAME.startsWith("Windows")) {
				System.setProperty(TestUtil.FIREFOX_DRIVER, TestUtil.FIREFOX_DRIVER_PATH_WIN);
			} else if (TestUtil.OS_NAME.startsWith("Linux")) {
				System.setProperty(TestUtil.FIREFOX_DRIVER, TestUtil.FIREFOX_DRIVER_PATH_LINUX);
			}  
			//set firefox option to console
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); 

			// start the browser up
			driver = new FirefoxDriver(options);  
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIME_OUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICITLY_TIME_OUT, TimeUnit.SECONDS);
		driver.get(properties.getProperty("url"));
		System.out.println("Main Url is running......");
	}

}
