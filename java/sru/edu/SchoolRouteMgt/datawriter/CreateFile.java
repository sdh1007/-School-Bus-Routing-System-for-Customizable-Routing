/*
 * Group1 - School Routing
 * Authors: Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Create Log File
 * Creates a log file that the system can log changes to. Acts as a backup for the database log in case of error
 * or failure. Most of the code was provided by w3 schools and need few changes.
 * 
 * Main Author: https://www.w3schools.com/java/java_files_create.asp
 */
package sru.edu.SchoolRouteMgt.datawriter;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateFile implements CommandLineRunner{
	//creates log file in the form of .txt
	@Override
	public void run(String... args) throws Exception{
    try {
      File myObj = new File("src/main/resources/SchoolRouteMgtLogs.txt");
      if (myObj.createNewFile()) {
        System.out.println("Log file created: " + myObj.getName());
      } else {
        System.out.println("Log file already created.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
