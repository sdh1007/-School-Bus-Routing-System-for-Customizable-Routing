package sru.edu.SchoolRouteMgt.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageHelper {
	private static List<String> FILETYPE = new ArrayList<>(List.of("jpg", "jpeg", "png", "gif"));
	private static List<String> MIMETYPES = new ArrayList<>(List.of("image/jpeg", "image/png", "image/gif"));
	  
	// Checks if the file is a CSV format & checks edge cases of different MIME types
	public static boolean hasImageFormat(MultipartFile file) {
		String fileExtension = FileNameUtils.getExtension(file.getOriginalFilename());
		
		if (FILETYPE.contains(fileExtension)) {
			if (MIMETYPES.contains(file.getContentType())) {
				return true;
			}
		}
		
		return false;  
	}
}
