package com.collegedunia.readandwrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.collegedunia.utils.TestUtil;

public class CreateExcelFile {

	String directory = TestUtil.OUTPUT_FOLDER_PATH_LINUX;

	// return true if file is created successfully else false
	@SuppressWarnings("unused")
	public boolean createFile(String fileName, String sheetName) {
		XSSFWorkbook workbookCreate = new XSSFWorkbook();
		XSSFSheet sheetCreate = workbookCreate.createSheet(sheetName);
		try {
			FileOutputStream out = new FileOutputStream(new File(directory + fileName + ".xlsx"));
			workbookCreate.write(out);
			out.close();
			workbookCreate.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// find whether file exists
	public boolean isFileExist(String fileName) {
		boolean flag = true;
		File file = new File(directory);
		File[] list = file.listFiles();
		if (list != null)

			for (File fil : list) {
				if (fil.isDirectory()) {
					isFileExist(fileName + ".xlsx");
				} else if ((fileName + ".xlsx").equalsIgnoreCase(fil.getName())) {
					System.out.println("File is already exist!!!");
					flag = false;
					break;
				}
			}
		return flag;
	}
}
