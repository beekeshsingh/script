package com.collegedunia.base;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.collegedunia.utils.TestUtil;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;

public class HeadLessTest {

	public static void main(String[] args) {
		String urlName = "https://collegedunia.com/";
		int count = 0;
		// Creating a new instance of the HTML unit driver
		/*
		 * System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
		 * "\\src\\main\\java\\com\\collegedunia\\resource\\geckodriver.exe");
		 */
		System.setProperty(TestUtil.PHANTOMJS_DRIVER, TestUtil.PHANTOMJS_DRIVER_PATH_WIN);
		// Add options to Google Chrome. The window-size is important for responsive
		// sites
		// start the proxy
		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.start(0);

		// get the Selenium proxy object
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

		/*
		 * FirefoxOptions option = new FirefoxOptions();
		 * option.addArguments("--headless"); option.setCapability(CapabilityType.PROXY,
		 * seleniumProxy);
		 */
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability(CapabilityType.PROXY, seleniumProxy);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, TestUtil.PHANTOMJS_DRIVER_PATH_WIN);
		WebDriver driver = new PhantomJSDriver(caps);

		proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

		proxy.newHar(urlName);

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		driver.get(urlName);
		// get the har data
		Har har = proxy.getHar();

		try {
			List<HarEntry> entries = har.getLog().getEntries();
			entries.forEach(item -> {
				System.out.println("Url :\t" + item.getRequest().getUrl());
				System.out.println("File size :\t" + item.getResponse().getBodySize());
				System.out.println("Status Code :\t" + item.getResponse().getStatus());
				System.out.println(count);
			});
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			proxy.stop();
			driver.quit();
		}

	}
}
