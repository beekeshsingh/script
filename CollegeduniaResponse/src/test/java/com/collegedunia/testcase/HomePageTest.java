package com.collegedunia.testcase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.mail.MessagingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.collegedunia.base.BrowserMobProxyClass;
import com.collegedunia.base.TestBase;
import com.collegedunia.base.VersionSetBrowser;
import com.collegedunia.comparator.Execution;
import com.collegedunia.pages.HomePage;
import com.collegedunia.readandwrite.CreateExcelFile;
import com.collegedunia.readandwrite.ReadWriteExcelData;
import com.collegedunia.readandwrite.RecordFile;
import com.collegedunia.utils.DateClassUtils;
import com.collegedunia.utils.HttpResponseCode;
import com.collegedunia.utils.TestUtil;

public class HomePageTest extends TestBase {

	HomePage homePage;
	ReadWriteExcelData oob;
	HttpResponseCode httpResponseCode;
	int i = 1, fileCounter = 2;
	String sheetNameProxy = "ProxyLinks", fileName = "MobProxy", date;
	BrowserMobProxyClass broserMobProxy;
	private DateClassUtils dateClass;
	private CreateExcelFile createFile;
	private RecordFile recordFile;
	private Execution execution;
	private VersionSetBrowser versionBrowser;

	public HomePageTest() {
		super();
	}

	@BeforeClass
	public void setUp() {
		initialization();
		homePage = new HomePage();
		broserMobProxy = new BrowserMobProxyClass();
		dateClass = new DateClassUtils();
		createFile = new CreateExcelFile();
		date = dateClass.getCurrentDate();
		excelFileOperation();
		recordFile = new RecordFile();
		execution = new Execution();
		httpResponseCode = new HttpResponseCode();
		versionBrowser = new VersionSetBrowser();
	}

	public void excelFileOperation() {
		fileName = "MobProxy" + date;
		sheetNameProxy = "ProxyLinks" + date;
		boolean flag;

		flag = createFile.isFileExist(fileName);
		if (flag) {
			// oob = new CreateExcelFile(directory1 + fileName + ".xlsx");
			System.out.println("File not exists!!!");
			createFile.createFile(fileName, sheetNameProxy);
			System.out.println("File Name: " + fileName);
		} else {
			// oob.createFile(fileName, sheetName);
			System.out.println("File is exist!!");
		}
		broserMobProxy = new BrowserMobProxyClass();
		try {
			if (TestUtil.OS_NAME.startsWith("Windows")) {
				oob = new ReadWriteExcelData(TestUtil.OUTPUT_FOLDER_PATH_WIN + fileName + ".xlsx");
			} else if (TestUtil.OS_NAME.startsWith("Linux")) {
				oob = new ReadWriteExcelData(TestUtil.OUTPUT_FOLDER_PATH_LINUX + fileName + ".xlsx");
			}

			if (!oob.isSheetExist(sheetNameProxy)) {
				oob.addSheet(sheetNameProxy);
				System.out.println("Sheet Added");
			}
			oob.setHeaderCellData(sheetNameProxy, 0, 0, "Link", 22000);
			oob.setHeaderCellData(sheetNameProxy, 1, 0, "Status Code", 2000);
			oob.setHeaderCellData(sheetNameProxy, 2, 0, "File Size in Byte", 4000);
			// oob.setHeaderCellData(sheetNameProxy, 3, 0, "Response Time in MS", 6000);
			System.out.println("Added Header!!!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@DataProvider
	public Iterator<Object[]> getDataTest() {
		ArrayList<Object[]> links = TestUtil.getDataFromExcel();
		return links.iterator();
	}

	@Test(priority = 1, dataProvider = "getDataTest")
	public void mobProxyLinks(String Link) {
		try {
			fileCounter = broserMobProxy.sizeOfFile(Link, fileCounter, oob, sheetNameProxy);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong....");
		}
	}

	@Test(priority = 2)
	public void setVersionName() {
		try {
			versionBrowser.versionName("https://collegedunia.com", oob, sheetNameProxy);
		} catch (Exception e) {
		}
	}

	@Test(priority = 3)
	public void compratorFiles() throws IOException, MessagingException {
		String currentFileName = fileName.trim();
		System.out.println(currentFileName);

		System.out.println("Current File Name : " + currentFileName);
		String lastFileName = recordFile.lastFileName().trim();
		System.out.println("Last File Name : " + lastFileName);
		String DateTime = dateClass.getCurrentDateTime().trim();
		System.out.println(DateTime);
		String oldFilePath = null, newFilePath = null;
		if (TestUtil.OS_NAME.startsWith("Windows")) {
			oldFilePath = TestUtil.OUTPUT_FOLDER_PATH_WIN + lastFileName + ".xlsx";
			newFilePath = TestUtil.OUTPUT_FOLDER_PATH_WIN + currentFileName + ".xlsx";
		} else if (TestUtil.OS_NAME.startsWith("Linux")) {
			oldFilePath = TestUtil.OUTPUT_FOLDER_PATH_LINUX + lastFileName + ".xlsx";
			newFilePath = TestUtil.OUTPUT_FOLDER_PATH_LINUX + currentFileName + ".xlsx";
		}

		System.out.println("Comparator Start...........");
		// execution.mainMethod(oldFilePath, newFilePath);
		System.out.println("Comparator End...........");
		String content = currentFileName + "\t, (" + currentFileName + " <==> " + lastFileName + ")\t, " + DateTime + " \r\n";
		recordFile.writeToTextFile(content);
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
		driver.close();
	}
}
