package sru.edu.SchoolRouteMgt.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import sru.edu.SchoolRouteMgt.domain.FileInfo;

/* Interface for interacting with locally stored uploaded files */
public interface FilesStorageService {
	public void init();
	
	public void save(MultipartFile file);
	
	public FileInfo saveAndInfo(MultipartFile file);

	public Resource load(String filename);
	
	public Stream<Path> loadAll();
	  
	public boolean delete(String filename);

	public void deleteAll();
}
