package com.collegedunia.readandwrite;
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
 
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadWriteExcelData {

	public FileInputStream fis = null;
	public FileOutputStream fos = null;
	public XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;
	public XSSFRow row = null;
	public XSSFCell cell = null;
	String path;

	public ReadWriteExcelData(String path) throws IOException {
		this.path = path;
		fis = new FileInputStream(path);
		workbook = new XSSFWorkbook(fis);
		fis.close();
	}

	// return the row count in sheet
	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			return 0;
		} else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}
	}

	// returns true if sheet is created successfully else false
	public boolean addSheet(String sheetname) {
		try {
			workbook.createSheet(sheetname);
			fos = new FileOutputStream(path);
			workbook.write(fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	} 

	// find whether sheets exists
	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNum);
			cell = row.getCell(colNum);
			if (cell.getCellTypeEnum() == CellType.STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				return String.valueOf(cell.getNumericCellValue());
			} else {
				return String.valueOf(cell.getBooleanCellValue());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in Excel";
		}
	}
	
	public boolean setHeaderCellData(String sheetName, int colNum, int rowNum, String value, int width) {
		try {
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);
			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);
			cell.setCellValue(value);
			sheet.setColumnWidth(colNum, width);
			fos = new FileOutputStream(path);
			workbook.write(fos);
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	public boolean setCellData(String sheetName, int colNum, int rowNum, String value) {
		try {
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);
			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);
			cell.setCellValue(value);
			fos = new FileOutputStream(path); 
			workbook.write(fos);
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		ReadWriteExcelData oob = new ReadWriteExcelData(
				"C:\\Users/cd user/Beekesh Singh/Automation/Collegedunia/src/main/java/com/collegedunia/testdata/Links.xlsx");

		if (!oob.isSheetExist("WRITELINKS")) {
			oob.addSheet("WRITELINKS");
		}

		int row = oob.getRowCount("LINKS");
		for (int i = 1; i < row; i++) {
			String data = oob.getCellData("LINKS", 0, i);
			System.out.println(i);
			System.out.println(data);
			oob.setCellData("WRITELINKS", 0, 0, "Link");
			oob.setCellData("WRITELINKS", 0, i, data);
			oob.setCellData("WRITELINKS", 1, 0, "Result");
			oob.setCellData("WRITELINKS", 1, i, "Pass");
		}
		System.out.println(row);
	}
}
