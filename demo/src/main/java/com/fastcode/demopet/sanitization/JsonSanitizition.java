package com.fastcode.demopet.sanitization;

import org.springframework.stereotype.Component;
import com.google.json.JsonSanitizer;

@Component
public class JsonSanitizition {
	
	
	public static String jsonSanitize(String jsonString) {
		return JsonSanitizer.sanitize(jsonString);
	}
}