package sru.edu.SchoolRouteMgt.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.web.multipart.MultipartFile;

public class CsvHelper {
	private static String FILETYPE = "csv";
	private static List<String> MIMETYPES = new ArrayList<>(List.of("text/csv", "application/vnd.ms-excel", 
			"application/csv", "application/x-csv", "text/comma-separated-values", "text/x-comma-separated-values"));
	  
	// Checks if the file is a CSV format & checks edge cases of different MIME types
	public static boolean hasCSVFormat(MultipartFile file) {
		String fileExtension = FileNameUtils.getExtension(file.getOriginalFilename());
		
		if (FILETYPE.equals(fileExtension)) {
			if (MIMETYPES.contains(file.getContentType())) {
				return true;
			}
		}
		
		return false;  
	}
	
	// Helper functions to handle when the user value isn't the parse type
	public static int tryParseInt(String value) {
		int defaultVal = 0;
		try {
			return Integer.parseInt(value);
	    } catch (NumberFormatException e) {
	        return defaultVal;
	    }
	}
	public static long tryParseLong(String value) {
		long defaultVal = 0;
		try {
	        return Long.parseLong(value);
	    } catch (NumberFormatException e) {
	        return defaultVal;
	    }
	}
	public static float tryParseFloat(String value) {
		float defaultVal = 0;
		try {
	        return Long.parseLong(value);
	    } catch (NumberFormatException e) {
	        return defaultVal;
	    }
	}
	public static boolean tryParseBoolean(String value) {
		boolean defaultVal = false;
	  	try {
	        return Boolean.parseBoolean(value);
	    } catch (NumberFormatException e) {
	        return defaultVal;
	    }
	}
}
