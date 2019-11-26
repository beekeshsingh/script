package com.collegedunia.readandwrite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.collegedunia.utils.DateClassUtils;
import com.collegedunia.utils.TestUtil; 

public class RecordFile {
	 
	public static String readTextFile(String fileName) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(fileName)));
		return content;
	}

	public List<String> readTextFileByLines(String fileName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		return lines;
	}

	public void writeToTextFile(String content) throws IOException {
		Files.write(Paths.get(TestUtil.RECORD_FILE_PATH_LINUX), content.getBytes(), StandardOpenOption.APPEND);
	}

	public String lastLineMethod() { 
		String last = null, line;

		BufferedReader in;
		try { 
			in = new BufferedReader(new FileReader(TestUtil.RECORD_FILE_PATH_LINUX));
	
		    while ((line = in.readLine()) != null) {
		        last = line; 
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return last;
	}
	
	public String lastFileName() {
		String lastLine = lastLineMethod(); 
		String[] fileName = lastLine.split(",");
		String file = fileName[0];
		return file;
	}

	public static void main(String[] args) throws IOException {
		RecordFile rf = new RecordFile(); 
		DateClassUtils dateClass = new DateClassUtils();
		
		String date = dateClass.getCurrentDateTime();
		//"CurrentFileName 	, CampareWith(CuurentFileName <==> PreviousFileName)    , Date(dd-MM-yyyy) & Time(HH:mm:ss) \r\n";
		String input = "MobProxyFile06032018 \t, (MobProxyFile06032018 <==> MobProxyFile05032018) \t, 06-03-2018 18:21:14 "+"\r\n";
		rf.writeToTextFile(input);
		System.out.println(rf.lastFileName());
		String fileName = rf.lastFileName();
		System.out.println("File Name is : "+ fileName);
	}

}
