package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*Contributed by Shane Smith
 * 
 * Used to store general information about a file & it's storage location
 * 
 * */

@Entity
public class FileInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 private long id;
	
	@Column(nullable = false, length=100)
	private String fileName;
	
	@Column(nullable = false, length=200)
	private String url;
	
	@Column(nullable = false, length=45)
	private String fileExtension;
	
	public FileInfo() {
	}
	
	public FileInfo(String name, String url) {
	  this.fileName = name;
	  this.url = url;
	}
	
	public FileInfo(String name, String url, String fileExtension) {
		  this.fileName = name;
		  this.url = url;
		  this.fileExtension = fileExtension;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
	    return this.fileName;
	}
	
	public void setName(String fileName) {
	    this.fileName = fileName;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	
	@Override
	public String toString() {
		return this.fileName;
	}
}