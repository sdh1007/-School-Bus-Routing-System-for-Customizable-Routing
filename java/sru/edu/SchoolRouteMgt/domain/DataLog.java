/*
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Data Logger
 * An object designed to push logging information. User is the one made the changes, information is the change that was made, and 
 * the date is the time the log was captured.
 * 
 * Main Author: Lucas Luczak
 */
package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;

/* 
 * This class is used to hold the log in the MySQL database
 */
@Entity
public class DataLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NonNull
	private String information;

	@NonNull
	private String user;

	@NonNull
	private String loggedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getloggedDate() {
		return loggedDate;
	}

	//Grabs the local date time from the users pc at the time the change was made
	public void setLoggedDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		this.loggedDate = dtf.format(now);
	}



}