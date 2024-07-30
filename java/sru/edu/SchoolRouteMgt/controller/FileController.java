package sru.edu.SchoolRouteMgt.controller;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sru.edu.SchoolRouteMgt.domain.CustomUserDetails;
import sru.edu.SchoolRouteMgt.domain.DataLog;
import sru.edu.SchoolRouteMgt.domain.FileInfo;
import sru.edu.SchoolRouteMgt.helper.CsvHelper;
import sru.edu.SchoolRouteMgt.helper.CsvSchoolHelper;
import sru.edu.SchoolRouteMgt.services.CsvService;
import sru.edu.SchoolRouteMgt.services.FilesStorageService;

@Controller
public class FileController {
	
	@Autowired
	FilesStorageService storageService;
	
	@Autowired
	CsvService csvService;
	
	// Gets a file's information
	@GetMapping("/files/{filename:.+}") 
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
	    Resource file = storageService.load(filename);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	// Returns fresh file upload form
	@GetMapping("/files/init")
	public String initFiles(Model model) {
		storageService.init();
		
		return "files";
	}
	
	// List all the files
	@GetMapping("/files")
	public String getListFiles(Model model) { 
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
		      String filename = path.getFileName().toString();
		      String url = MvcUriComponentsBuilder
		          .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

		      return new FileInfo(filename, url);
		    }).collect(Collectors.toList());

		    model.addAttribute("files", fileInfos);

		return "files";
	}
	
	// Deletes a specific file
	@GetMapping("/files/delete/{filename:.+}") 
	public String deleteFile(@PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
		try {
			boolean existed = storageService.delete(filename);

			if (existed) {
				redirectAttributes.addFlashAttribute("message", "Delete the file successfully: " + filename);
			} else {
				redirectAttributes.addFlashAttribute("message", "The file does not exist!");
			}
	    } catch (Exception e) {
	    	redirectAttributes.addFlashAttribute("message",
	    			"Could not delete the file: " + filename + ". Error: " + e.getMessage());
	    }
		
	    return "redirect:/files";
	}
	
	// Deletes all files & folder
	/*@GetMapping("/files/delete/all") 
	public String deleteAllFiles(Model model) {
		storageService.deleteAll();
		
	    return "/files";
	}*/
}
