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

public class BrowserMobProxyClass {

	public int sizeOfFile(String urlName, int i, ReadWriteExcelData oob, String sheetName) {

		System.setProperty(TestUtil.FIREFOX_DRIVER, TestUtil.FIREFOX_DRIVER_PATH_LINUX);

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

		// open url .com
		driver.get(urlName);

		// get the HAR data
		Har har = proxy.getHar();

		try {
			List<HarEntry> entries = har.getLog().getEntries();
			i++;
			oob.setCellData(sheetName, 0, i, urlName);
			i++;
			System.out.println(urlName);
			for (HarEntry entry : entries) {
				String urlNameRes = entry.getRequest().getUrl();
				float fileSize = entry.getResponse().getBodySize();
				int statusCode = entry.getResponse().getStatus();
				String versionName, dot;
				if (fileSize <= 0) {
					fileSize = 0;
				}
				boolean flag;
				if (urlNameRes.startsWith("https://")) {
					if (urlNameRes.endsWith(".svg") || urlNameRes.endsWith(".css")) {
						versionName = (urlNameRes.substring(urlNameRes.length() - 8, urlNameRes.length() - 4));
						flag = checkValue(versionName);
						if (flag) {
							dot = (urlNameRes.substring(urlNameRes.length() - 9, urlNameRes.length() - 8));
							if (dot.equals(".")) {
								urlNameRes = (urlNameRes.replace(urlNameRes.substring(urlNameRes.length() - 8), urlNameRes.substring(urlNameRes.length() - 3)));
								// System.out.println(urlNameRes);
							}
						}
					} else if (urlNameRes.endsWith(".js")) {
						versionName = (urlNameRes.substring(urlNameRes.length() - 7, urlNameRes.length() - 3));
						flag = checkValue(versionName);
						if (flag) {
							dot = (urlNameRes.substring(urlNameRes.length() - 8, urlNameRes.length() - 7));
							if (dot.equals(".")) {
								urlNameRes = (urlNameRes.replace(urlNameRes.substring(urlNameRes.length() - 8), urlNameRes.substring(urlNameRes.length() - 3)));
								// System.out.println(urlNameRes);
							}
						}
					}
					setValuesInFile(i, sheetName, oob, urlNameRes, fileSize, statusCode);
					i++;
				} else {
					System.out.println("Invalid url!!!" + urlNameRes);
				}
			}
		} finally {
			proxy.stop();  
			driver.close();
		}
		return i;
	}

	public void setValuesInFile(int i, String sheetName, ReadWriteExcelData oob, String urlNameRes, float fileSize, int statusCode) {
		oob.setCellData(sheetName, 0, i, urlNameRes);
		oob.setCellData(sheetName, 1, i, Integer.toString(statusCode));
		oob.setCellData(sheetName, 2, i, Float.toString(fileSize));
		//System.out.println("File url: \t" + urlNameRes);
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
}
