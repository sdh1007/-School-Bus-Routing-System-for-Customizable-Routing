package sru.edu.SchoolRouteMgt.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import sru.edu.SchoolRouteMgt.controller.FileController;
import sru.edu.SchoolRouteMgt.domain.FileInfo;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.repository.SchoolRepository;

/* Contributed by Shane Smith
 * 
 * Service for interacting with locally stored uploaded files 
 * 
 * All features relating to uploading/deleting files were based from this guide: https://www.bezkoder.com/spring-boot-delete-file-thymeleaf/
 * */

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
	@Autowired
	 SchoolRepository schoolRepo;
	
	private final Path root = Paths.get("./src/main/resources/uploads/");
	
	 @Override
	 public void init() { /* Initializes the upload folder using the root variable */
		 try {
			 Files.createDirectories(root);
		 } catch (IOException e) {
			 throw new RuntimeException("Could not initialize folder for upload!");
		 }
	  }

	 /*------ MAKE THIS SO IT JUST ADDS A NUMBER TO THE FILENAME SO USER CAN UPLOAD FILES OF THE SAME NAME ------*/
	  @Override
	  public void save(MultipartFile file) { /* Save the file and checks if it's already been uploaded */
		try {
	    	Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
	    } catch (Exception e) {
	    	if (e instanceof FileAlreadyExistsException) {
	    		throw new RuntimeException("A file of that name already exists.");
	    	}
	    	
	    	throw new RuntimeException(e.getMessage());
	    }
	  }
	  
	  /*------ MAKE THIS SO IT JUST ADDS A NUMBER TO THE FILENAME SO USER CAN UPLOAD FILES OF THE SAME NAME ------*/
	  @Override
	  public FileInfo saveAndInfo(MultipartFile file) { /* Save the file and checks if it's already been uploaded */
		// Get file's information
		Path destinationFile = this.root.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
		String filename = file.getOriginalFilename();
	    String url = MvcUriComponentsBuilder
	        .fromMethodName(FileController.class, "getFile", destinationFile.getFileName().toString()).build().toString();
	    String ext = FileNameUtils.getExtension(file.getOriginalFilename());
		
	    // Check if a file with the same name exists already
	    if(Files.exists(destinationFile)) {
	    	LocalTime time = LocalTime.now();
	        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH-mm-ss");
	        String formattedTime = time.format(timeFormat);
	    	
	        String filenameWithoutExt = FileNameUtils.getBaseName(filename);
	        filenameWithoutExt +=  formattedTime.toString(); // Add current time to make file semi-unique
	        filename = filenameWithoutExt + "." + ext; // Recreate the filename
	    }
	    
		try { /* Saves File */
	    	Files.copy(file.getInputStream(), this.root.resolve(filename));
	    } catch (Exception e) {
	    	if (e instanceof FileAlreadyExistsException) {
	    		throw new RuntimeException("A file of that name already exists.");
	    	}
	    	
	    	throw new RuntimeException(e.getMessage());
	    }
		
		return new FileInfo(filename, url, ext);
	  }

	  @Override
	  public Resource load(String filename) { /* Reads the file */
		  try {
			  Path file = root.resolve(filename);
			  Resource resource = new UrlResource(file.toUri());

			  if (resource.exists() || resource.isReadable()) {
				  return resource;
			  } else {
				  throw new RuntimeException("Could not read the file!");
			  }
		  } catch (MalformedURLException e) {
			  throw new RuntimeException("Error: " + e.getMessage());
		  }
	  }
	 
	  @Override
	  public Stream<Path> loadAll() { /* Returns all of the files */
		  try {
			  return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		  } catch (IOException e) {
			  throw new RuntimeException("Could not load the files!");
		  }
	  }
	  
	  @Override
	  public boolean delete(String filename) { /* Deletes 1 file */
		  try {
			  Path file = root.resolve(filename);
			  return Files.deleteIfExists(file);
		  } catch (IOException e) {
			  throw new RuntimeException("Error: " + e.getMessage());
		  }
	  }

	  // Deletes all the files and the 'uploads' root directory for cleaning
	  @Override
	  public void deleteAll() { 
		  try {
			  FileSystemUtils.deleteRecursively(root.toFile());
		  } catch (Exception e) {
			 throw new RuntimeException("Could delete folder!");
		  }
	  }
}