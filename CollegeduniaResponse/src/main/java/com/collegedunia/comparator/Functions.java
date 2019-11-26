package com.collegedunia.comparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.collegedunia.utils.TestUtil;

public class Functions extends Constants {

	String requestsVersionOld, requestsVersionNew;

	// Create Excel for Result Difference
	public String createResultantExcel(LinkedHashMap<String, LinkedHashMap<String, String[][]>> diffData, String oldFilePath, String newFilePath) throws IOException {

		// Initializing variables
		String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
		String filePath = "";
		Row row;
		Cell cell;

		/* Create Workbook and Worksheet */
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("XLSX Merge Cells");

		// Set Column Width
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);

		// Merge Cells
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 8));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

		// Set Row Height
		row = sheet.createRow((short) 1);
		row.setHeight((short) 800);

		// -------------------------- CREATE EXCEL FORMAT
		// -------------------------------

		// Create First Row with some row height
		row = sheet.createRow(0);
		row.setHeight((short) 800);

		// Create 1st Cell
		createCell(workbook, row, 0, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "URLs");
		// Create 2nd Cell
		createCell(workbook, row, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Results Of : " + oldFilePath.substring(oldFilePath.length() - 29, oldFilePath.length()) + " | " + requestsVersionOld);
		// Create 3rd Cell
		createCell(workbook, row, 5, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Results Of : " + newFilePath.substring(newFilePath.length() - 29, newFilePath.length()) + " | " + requestsVersionNew);

		// Create 2nd Row
		row = sheet.createRow(1);
		createCell(workbook, row, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Is_Available");
		createCell(workbook, row, 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Response Code");
		createCell(workbook, row, 3, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Size");
		createCell(workbook, row, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "AppearanceCount");
		createCell(workbook, row, 5, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Is_Available");
		createCell(workbook, row, 6, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Response Code");
		createCell(workbook, row, 7, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "Size");
		createCell(workbook, row, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "AppearanceCount");

		// ----------------------------- FORMAT CREATED
		// -----------------------------------

		// i represents row count
		int i = 2;

		for (Entry<String, LinkedHashMap<String, String[][]>> outer : diffData.entrySet()) {

			// Enter Main Url in first column of row = i
			row = sheet.createRow(i);
			createCell(workbook, row, 0, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, outer.getKey());
			i++;

			for (Entry<String, String[][]> inner : outer.getValue().entrySet()) {
				// Create Row and Enter Request Url (towards main url) in first column
				row = sheet.createRow(i);
				createCell(workbook, row, 0, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, inner.getKey());

				// If array length is 3 this means request is missing in mail url .
				if (inner.getValue().length == 3) {

					// The requests exists in old file but not in New File
					if (inner.getValue()[2][1].contains(oldFilePath.substring(oldFilePath.length() - 29, oldFilePath.length()))) {
						createCell(workbook, row, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "YES");
						createCell(workbook, row, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[1][1]);

						createCell(workbook, row, 5, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "NO");
						createCell(workbook, row, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, Integer.toString(0));
					}
					// The requests exists in New file but not in Old File
					else if (inner.getValue()[2][1].contains(newFilePath.substring(newFilePath.length() - 29, newFilePath.length()))) {
						createCell(workbook, row, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "NO");
						createCell(workbook, row, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, Integer.toString(0));

						createCell(workbook, row, 5, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, "YES");
						createCell(workbook, row, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[1][1]);

					}

				}
				// This means there is some difference in parameters of request .
				else {
					// We just need to compare only response code and size .
					for (int m = 0; m < 2; m++) {
						if (inner.getValue()[m][0] != null) {
							String columnType = inner.getValue()[m][0];
							switch (columnType) {
							case "Response Code":
								createCell(workbook, row, 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[m][1]);
								createCell(workbook, row, 6, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[m + 4][1]);
								break;
							case "Size":
								createCell(workbook, row, 3, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[m][1]);
								createCell(workbook, row, 7, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[m + 4][1]);
								break;

							}
						}

						// We need to
						createCell(workbook, row, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[2][1]);
						createCell(workbook, row, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, inner.getValue()[6][1]);
					}
				}
				// Increment row
				i++;
			}
			// Increment row
			i++;

		}
		if (TestUtil.OS_NAME.startsWith("Window")) {
			filePath = TestUtil.OUTPUT_FOLDER_PATH_WIN + "MobProxyFileFinal" + timeStamp + ".xlsx";
		} else if (TestUtil.OS_NAME.startsWith("Linux")) {
			filePath = TestUtil.OUTPUT_FOLDER_PATH_LINUX + "MobProxyFileFinal" + timeStamp + ".xlsx";
		}

		if (!filePath.isEmpty()) {
			/* Write changes to the workbook */
			FileOutputStream out = new FileOutputStream(new File(filePath));
			workbook.write(out);
			out.close();
			System.out.println("Written Successfully");
		}

		// Close Workbook
		workbook.close();
		return filePath;
	}

	private void createCell(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign, String cellValue) {
		Cell cell = row.createCell(column);
		cell.setCellValue(cellValue);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		cell.setCellStyle(cellStyle);
	}

	// Compare Results
	public LinkedHashMap<String, LinkedHashMap<String, String[][]>> compareResults(LinkedHashMap<String, LinkedHashMap<String, String[][]>> urlDataOld, LinkedHashMap<String, LinkedHashMap<String, String[][]>> urlDataNew, String oldFilePath,
			String newFilePath) {
		String[][] errorArray;
		LinkedHashMap<String, String[][]> errorValue;
		LinkedHashMap<String, LinkedHashMap<String, String[][]>> error = new LinkedHashMap<String, LinkedHashMap<String, String[][]>>();

		// Loop on old values (Main URL)
		for (Entry<String, LinkedHashMap<String, String[][]>> oldEntry : urlDataOld.entrySet()) {
			errorValue = new LinkedHashMap<String, String[][]>();
			// Loop on New values (Main URL)
			for (Entry<String, LinkedHashMap<String, String[][]>> latestEntry : urlDataNew.entrySet()) {

				// Check if both Main URL are same or not

				if (oldEntry.getKey().equals(latestEntry.getKey())) {
					// Requests of Main URL . Comparing New With Old .
					// This FOR loop only checks the existence of latest requests in colection of
					// old requests towards one main url
					for (Entry<String, String[][]> newEntryValue : latestEntry.getValue().entrySet()) {

						errorArray = new String[3][2];
						if (!oldEntry.getValue().containsKey(newEntryValue.getKey())) {

							errorArray[0][0] = "Is_Available";
							errorArray[0][1] = "YES";

							errorArray[1][0] = "Duplicated";
							errorArray[1][1] = newEntryValue.getValue()[2][1];

							errorArray[2][0] = "Sheet Name";
							errorArray[2][1] = newFilePath.substring(newFilePath.length() - 29, newFilePath.length());

							errorValue.put(newEntryValue.getKey(), errorArray);
							continue;
						}

					}

					// Each Requests of Main old URL in New List
					// This FOR loop only checks the existence of old requests in colection of
					// latest requests towards one main url
					for (Entry<String, String[][]> oldEntryValue : oldEntry.getValue().entrySet()) {
						errorArray = new String[3][2];
						if (!latestEntry.getValue().containsKey(oldEntryValue.getKey())) {
							errorArray[0][0] = "Is_Available";
							errorArray[0][1] = "YES";

							errorArray[1][0] = "Duplicated";
							errorArray[1][1] = oldEntryValue.getValue()[2][1];

							errorArray[2][0] = "Sheet Name";
							errorArray[2][1] = oldFilePath.substring(oldFilePath.length() - 29, oldFilePath.length());

							errorValue.put(oldEntryValue.getKey(), errorArray);
							continue;

						} else {
							for (Entry<String, String[][]> latestEntryValue : latestEntry.getValue().entrySet()) {

								// If old request is found in new list also .
								if (oldEntryValue.getKey().equals(latestEntryValue.getKey())) {
									errorArray = new String[8][2];
									// Compare attributes of Request .
									for (int i = 0; i < oldEntryValue.getValue().length - 1; i++) {

										if (!oldEntryValue.getValue()[i][1].equals(latestEntryValue.getValue()[i][1])) {
											errorArray[i][0] = oldEntryValue.getValue()[i][0];
											errorArray[i][1] = oldEntryValue.getValue()[i][1];

											errorArray[i + 4][0] = latestEntryValue.getValue()[i][0];
											errorArray[i + 4][1] = latestEntryValue.getValue()[i][1];

										}

									}

									// Check if there is any difference in values of attributes
									if (errorArray[0][1] != null || errorArray[1][1] != null) {
										errorArray[2][0] = errorArray[6][0] = "Duplicated";
										errorArray[2][1] = oldEntryValue.getValue()[2][1];
										errorArray[6][1] = latestEntryValue.getValue()[2][1];

										errorArray[3][0] = errorArray[7][0] = "Sheet Name";
										errorArray[3][1] = oldFilePath.substring(oldFilePath.length() - 29, oldFilePath.length());
										errorArray[7][1] = newFilePath.substring(newFilePath.length() - 29, newFilePath.length());

										errorValue.put(oldEntryValue.getKey(), errorArray);
									} else if (Integer.parseInt(oldEntryValue.getValue()[2][1]) > 1 || Integer.parseInt(latestEntryValue.getValue()[2][1]) > 1) {
										errorArray[2][0] = errorArray[6][0] = "Duplicated";
										errorArray[2][1] = oldEntryValue.getValue()[2][1];
										errorArray[6][1] = latestEntryValue.getValue()[2][1];

										errorArray[3][0] = errorArray[7][0] = "Sheet Name";
										errorArray[3][1] = oldFilePath.substring(oldFilePath.length() - 29, oldFilePath.length());
										errorArray[7][1] = newFilePath.substring(newFilePath.length() - 29, newFilePath.length());

										errorValue.put(oldEntryValue.getKey(), errorArray);
									}

									continue;

								}

							}

						}

					}

					break;
				}
			}
			error.put(oldEntry.getKey(), errorValue);

		}

		return error;

	}

	// Read Data From Excel
	public LinkedHashMap<String, LinkedHashMap<String, String[][]>> getDataFromExcel(String filePath, String fileType) throws IOException {

		/*
		 * 
		 * String fileType can be of two types : OLD -> Previous Sheet NEW -> LAtest
		 * Sheet This is done just to capture version from sheet .
		 * 
		 * 
		 * responseSizeCount : Type --> Array Data --> [0] Response Code , [1] Response
		 * Size , [2] Duplicate [2][0] -> "Duplicate" [2][1] --> VAlue Like int count
		 * 
		 * requestUrlData : Type --> LinkedHashMap Data --> Key : Request Url, Value :
		 * responseSizeCount
		 * 
		 * urlData : Type --> LinkedHashMap Data --> Key : Main Url , Value :
		 * requestUrlData
		 * 
		 */

		// Initializing Variables
		String responseSizeCount[][];
		LinkedHashMap<String, String[][]> requestUrlData = new LinkedHashMap<String, String[][]>();
		LinkedHashMap<String, LinkedHashMap<String, String[][]>> urlData = new LinkedHashMap<String, LinkedHashMap<String, String[][]>>();
		Row row;
		Cell cell;
		String url = null;

		// Create Class Objects to access Excel Sheet
		FileInputStream file = new FileInputStream(new File(filePath));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		// Create Second Row (Where version is stored)
		row = sheet.getRow(1);
		cell = row.getCell(0);

		// Store version from sheet
		if (fileType.equalsIgnoreCase("New"))
			requestsVersionOld = cell.getStringCellValue();
		else if (fileType.equalsIgnoreCase("Old"))
			requestsVersionNew = cell.getStringCellValue();

		// Read Data from Excel till the last row of the sheet and Store it in into
		// LinkedHashMap
		for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
			// Instantiate row
			row = sheet.getRow(i);
			String requestUrl = null;
			int is_duplicate = 0;

			// Instantiating Other Variables . They will be refreshed after every iteration
			responseSizeCount = new String[3][2];

			try {
				// Variable (j) signifies Cell Number (Column Index) .
				for (int j = 0; j < 3; j++) {
					// Instantiating Column
					try {
						cell = row.getCell(j);
					} catch (NullPointerException e) {
						// If Row's first column is empty then there must be Main Url in the next row
						// Therefore Increment row by 1 .
						i++;
						row = sheet.getRow(i);
						cell = row.getCell(j);
						url = cell.getStringCellValue();
						requestUrlData = new LinkedHashMap<String, String[][]>();
						break;
					}

					// First Column will always have a link or nothing
					if (j == 0) {
						// It will throw null pointer if cell is empty else it has to be a link
						if (cell.getStringCellValue().startsWith("https://")) {
							if (urlData.get(url).containsKey(cell.getStringCellValue())) {
								String previousData = urlData.get(url).get(cell.getStringCellValue())[2][1];
								int count = Integer.parseInt(previousData);
								urlData.get(url).get(cell.getStringCellValue())[2][1] = Integer.toString(++count);
							} else {
								requestUrl = cell.getStringCellValue();
								responseSizeCount[2][0] = "Duplicated";
								responseSizeCount[2][1] = "1";
							}

						} else
							requestUrl = "INVALID URL : " + cell.getStringCellValue();
					} else {

						if (is_duplicate == 0) {
							// Append Column names as well along with the column value
							switch (j) {
							case 1:
								responseSizeCount[0][0] = "Response Code";
								responseSizeCount[0][1] = cell.getStringCellValue();
								break;
							case 2:
								responseSizeCount[1][0] = "Size";
								responseSizeCount[1][1] = cell.getStringCellValue();
								break;
							}
						}

					}
				}

				// Url can be of two types : Main | Request .
				// Main Url will not have its properties stored in responseSizeCount .
				if (requestUrl != null) {
					// Add single row values (request url , properties) into the hashmap
					requestUrlData.put(requestUrl, responseSizeCount);
				}

			} catch (NullPointerException e) {

			}

			// Finally add all the data (for single main url )to single LinkedHashMap
			urlData.put(url, requestUrlData);
		}

		// Finally return the data
		return urlData;

	}

}
