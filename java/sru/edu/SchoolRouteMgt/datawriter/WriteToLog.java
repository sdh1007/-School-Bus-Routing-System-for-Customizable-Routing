//Main Author: https://www.w3schools.com/java/java_files_create.asp
/*
 * This class writes the data from the logs to the text file called SchoolRouteMgtLogs.txt
 */
package sru.edu.SchoolRouteMgt.datawriter;

import java.io.BufferedWriter;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class WriteToLog {
	
	private String text;
	
  public WriteToLog() {
	 text = "";
  }
  
  public void setText(String text)
  {
	  try(FileWriter fw = new FileWriter("src/main/resources/SchoolRouteMgtLogs.txt", true);
  		    BufferedWriter bw = new BufferedWriter(fw))
  		{
  		    bw.append(text);
  		  bw.close();
  		} catch (IOException e) {
  		    System.out.println("Error writing " + text + " to file");
  		}

  }
    	
}
