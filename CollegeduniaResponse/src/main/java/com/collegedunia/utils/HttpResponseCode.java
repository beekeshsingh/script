package com.collegedunia.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
 

public class HttpResponseCode {
	public int httpResponseCode(String links) throws IOException {
		URL url = new URL(links);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		return http.getResponseCode();
	}
}
