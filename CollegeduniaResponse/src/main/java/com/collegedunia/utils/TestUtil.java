package com.collegedunia.utils;

import java.util.ArrayList;

import com.collegedunia.base.TestBase;
import com.collegedunia.readandwrite.ReadWriteExcelData;

public class TestUtil extends TestBase {
	public static final int PAGE_LOAD_TIME_OUT = 50;
	public static final int IMPLICITLY_TIME_OUT = 40;
	public static final String DIRECTORY = System.getProperty("user.dir");
	public static final String OS_NAME = System.getProperty("os.name");
	public static final String CHROME_DRIVER = "webdriver.chrome.driver";
	public static final String FIREFOX_DRIVER = "webdriver.gecko.driver";
	public static final String PHANTOMJS_DRIVER = "phantomjs.binary.path";
	public static final String CHROME_DRIVER_PATH_WIN = DIRECTORY + "\\src\\main\\resource\\chromedriver.exe";
	public static final String CHROME_DRIVER_PATH_LINUX = DIRECTORY + "/src/main/resource/chromedriver";
	public static final String FIREFOX_DRIVER_PATH_WIN = DIRECTORY + "\\src\\main\\resource\\geckodriver.exe";
	public static final String FIREFOX_DRIVER_PATH_LINUX = DIRECTORY + "/src/main/resource/geckodriver";
	public static final String PHANTOMJS_DRIVER_PATH_WIN = DIRECTORY + "\\src\\main\\resource\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
	public static final String PHANTOMJS_DRIVER_PATH_LINUX = DIRECTORY + "/src/main/resource/phantomjs-2.1.1-windows/bin/phantomjs.exe";
	public static final String INPUT_FOLDER_PATH_WIN = DIRECTORY + "\\src\\main\\resource\\cd-input\\";
	public static final String INPUT_FOLDER_PATH_LINUX = DIRECTORY + "/src/main/resource/cd-input/";
	public static final String RECORD_FILE_PATH_WIN = DIRECTORY + "\\src\\main\\resource\\cd-output\\Records.txt";
	public static final String RECORD_FILE_PATH_LINUX = DIRECTORY + "/src/main/resource/cd-output/Records.txt";
	public static final String OUTPUT_FOLDER_PATH_WIN = DIRECTORY + "\\src\\main\\resource\\cd-output\\";
	public static final String OUTPUT_FOLDER_PATH_LINUX = DIRECTORY + "/src/main/resource/cd-output/";

	public static ArrayList<Object[]> getDataFromExcel() {
		ReadWriteExcelData reader = null;
		ArrayList<Object[]> myData = new ArrayList<Object[]>();
		try {
			System.out.println(DIRECTORY);
			if (OS_NAME.startsWith("Windows")) {
				reader = new ReadWriteExcelData(INPUT_FOLDER_PATH_WIN + "CollegeduniaUrls.xlsx");
			} else if (OS_NAME.startsWith("Linux")) {
				reader = new ReadWriteExcelData(INPUT_FOLDER_PATH_LINUX + "CollegeduniaUrls.xlsx");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (int rowNum = 1; rowNum < reader.getRowCount("LINKS"); rowNum++) {
			String link = reader.getCellData("LINKS", 0, rowNum);
			Object ob[] = { link };
			myData.add(ob);
		}
		System.out.println("Return data!!!!!!!!");
		return myData;
	}
}
