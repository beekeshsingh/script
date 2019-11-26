package com.collegedunia.base;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.collegedunia.readandwrite.ReadWriteExcelData;
import com.collegedunia.utils.TestUtil;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class VersionSetBrowser {

	public void versionName(String urlName, ReadWriteExcelData oob, String sheetName) {

		if (TestUtil.OS_NAME.startsWith("Windows")) {
			System.setProperty(TestUtil.FIREFOX_DRIVER, TestUtil.FIREFOX_DRIVER_PATH_WIN);
		} else if (TestUtil.OS_NAME.startsWith("Linux")) {
			System.setProperty(TestUtil.FIREFOX_DRIVER, TestUtil.FIREFOX_DRIVER_PATH_LINUX);
		}

		// Add options to Google Chrome. The window-size is important for responsive
		// sites
		// start the proxy
		BrowserMobProxy proxy = new BrowserMobProxyServer();
		proxy.start(0);

		// get the Selenium proxy object
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

		FirefoxOptions option = new FirefoxOptions();
		option.addArguments("--headless");
		option.setCapability(CapabilityType.PROXY, seleniumProxy);

		WebDriver driver = new FirefoxDriver(option);


		// enable more detailed HAR capture, if desired (see CaptureType for the
		// complete list)
		proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

		// create a new HAR with the label
		proxy.newHar(urlName);

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIME_OUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICITLY_TIME_OUT, TimeUnit.SECONDS);

		// open url.com
		driver.get(urlName);

		// get the HAR data
		Har har = proxy.getHar();
		try {
			List<HarEntry> entries = har.getLog().getEntries();
			for (HarEntry entry : entries) {
				String urlNameRes = entry.getRequest().getUrl();
				String versionName, dot = null;
				boolean flag;
				if (urlNameRes.startsWith("https://")) {
					if (urlNameRes.endsWith(".svg") || urlNameRes.endsWith(".css")) {
						versionName = (urlNameRes.substring(urlNameRes.length() - 8, urlNameRes.length() - 4));
						flag = checkValue(versionName);
						if (flag) {
							dot = (urlNameRes.substring(urlNameRes.length() - 9, urlNameRes.length() - 8));
							if (dot.equals(".")) {
								urlNameRes = (urlNameRes.replace(urlNameRes.substring(urlNameRes.length() - 8), urlNameRes.substring(urlNameRes.length() - 3)));
								//System.out.println(urlNameRes);
								oob.setCellData(sheetName, 0, 1, "Version: " + versionName);
								break;
							}
						}
					} else if (urlNameRes.endsWith(".js")) {
						versionName = (urlNameRes.substring(urlNameRes.length() - 7, urlNameRes.length() - 3));
						flag = checkValue(versionName);
						if (flag) {
							dot = (urlNameRes.substring(urlNameRes.length() - 8, urlNameRes.length() - 7));
							System.out.println(dot);
							if (dot.equals(".")) {
								urlNameRes = (urlNameRes.replace(urlNameRes.substring(urlNameRes.length() - 8), urlNameRes.substring(urlNameRes.length() - 3)));
								//System.out.println(urlNameRes);
								oob.setCellData(sheetName, 0, 1, "Version: " + versionName);
								break;
							}
						}
					}
				} else {
					System.out.println("Invalid url!!!" + urlNameRes);
				}
			}
		} finally {
			proxy.stop();
			driver.close();
			driver.quit();
		}
	}

	public static boolean checkValue(String value) {
		boolean valueValid = false;
		try {
			Integer.parseInt(value);
			// value is a valid number
			valueValid = true;

		} catch (NumberFormatException e) { 
			System.out.println("Not an integer number!!!!!!!!!");
		}
		return valueValid;
	}

	public static void main(String[] args) {
		String urlNameRes = "https://images.static-collegedunia.com/public/asset/build/js/m_index.min.1889.js";
		String versionName, dot = null;
		boolean flag;
		if (urlNameRes.startsWith("https://")) {
			System.out.println("Inner");
			if (urlNameRes.endsWith(".svg") || urlNameRes.endsWith(".css")) {
				versionName = (urlNameRes.substring(urlNameRes.length() - 8, urlNameRes.length() - 4));
				flag = checkValue(versionName);
				if (flag) {
					dot = (urlNameRes.substring(urlNameRes.length() - 9, urlNameRes.length() - 8));
					if (dot.equals(".")) {
						System.out.println(dot);
						urlNameRes = (urlNameRes.replace(urlNameRes.substring(urlNameRes.length() - 8), urlNameRes.substring(urlNameRes.length() - 3)));
					}
					// System.out.println(urlNameRes);
				}
				// oob.setCellData(sheetName, 0, 1, "Version: " + versionName);
				// break;

			} else if (urlNameRes.endsWith(".js")) {
				versionName = (urlNameRes.substring(urlNameRes.length() - 7, urlNameRes.length() - 3));
				flag = checkValue(versionName);
				if (flag) {
					dot = (urlNameRes.substring(urlNameRes.length() - 8, urlNameRes.length() - 7));
					System.out.println(dot);
					if (dot.equals(".")) {
						urlNameRes = (urlNameRes.replace(urlNameRes.substring(urlNameRes.length() - 8), urlNameRes.substring(urlNameRes.length() - 3)));
						System.out.println(urlNameRes);
					}
				}
				// oob.setCellData(sheetName, 0, 1, "Version: " + versionName);
				// break;
			}
		} else {
			System.out.println("Invalid url!!!" + urlNameRes);
		}
	}
}
