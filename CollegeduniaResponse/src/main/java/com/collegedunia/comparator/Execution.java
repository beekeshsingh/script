package com.collegedunia.comparator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.mail.MessagingException;

public class Execution extends Constants {

	public void mainMethod(String oldFilePath, String newFilePath) throws IOException, MessagingException {

		// Instantiate Variables
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		System.out.println("Starting .. : " + timeStamp);
		Functions func = new Functions();
		// SendMailUsingAuthentication sendMail = new SendMailUsingAuthentication();
		LinkedHashMap<String, LinkedHashMap<String, String[][]>> urlDataOld = new LinkedHashMap<String, LinkedHashMap<String, String[][]>>();
		LinkedHashMap<String, LinkedHashMap<String, String[][]>> urlDataNew = new LinkedHashMap<String, LinkedHashMap<String, String[][]>>();
		LinkedHashMap<String, LinkedHashMap<String, String[][]>> diffData = new LinkedHashMap<String, LinkedHashMap<String, String[][]>>();

		// Get Data From Sheet 1
		urlDataOld = func.getDataFromExcel(oldFilePath, "Old");

		// Get Data From Sheet 2
		urlDataNew = func.getDataFromExcel(newFilePath, "New");

		// Compare Results . Argument 1 : Argument 2 : Latest Results
		diffData = func.compareResults(urlDataOld, urlDataNew, oldFilePath, newFilePath);

		// Create resultant Excel sheet .
		String filePath = func.createResultantExcel(diffData, oldFilePath, newFilePath);

		if (!filePath.isEmpty()) {
			// Send Mail
			timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
			String emailSubjectTxt = "Url Requests " + timeStamp;
			String emailFromAddress = "sunnygoel.collegedunia@gmail.com";
			String[] emailList = { "sunny.goel@collegedunia.com", "beekesh.singh@collegedunia.com" };// Change the
																										// mailing list
			// sendMail.postMail(emailList, emailSubjectTxt, "PFA", emailFromAddress,
			// filePath);
			System.out.println("Ended .. : " + timeStamp);
		} else {
			System.out.println("No Change Found ");
			timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
			System.out.println("Ended .. : " + timeStamp);
		}

	}

}
