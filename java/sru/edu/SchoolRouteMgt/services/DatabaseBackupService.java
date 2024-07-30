package sru.edu.SchoolRouteMgt.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

// Creates backup and restore services for the database to be controlled by the system admin
public final class DatabaseBackupService {
	private static String dbName = "schoolroutemgt";
	private static String programPath = Paths.get("").toAbsolutePath().toString();
	private static String pathToUploads = "src\\main\\resources\\uploads\\";
	//static String sourceFile = "backup";
	
	public static boolean backup(String dbUsername, String dbPassword, String sourceFile)
            throws IOException, InterruptedException {
		
		sourceFile = "\"" + programPath + "\\" + pathToUploads + sourceFile + ".sql\""; // Quotes used to escape potential spaces in the path to the project
		
		String command = String.format("mysqldump -u%s -p%s --add-drop-table --databases %s -r %s",
				dbUsername, dbPassword, dbName, sourceFile);
				
		Process process = Runtime.getRuntime().exec("cmd.exe /c " + command);
        int processComplete = process.waitFor();
                
		if (processComplete == 0) {
            return true;
        }
        else {
            return false;
        }
    }
	
	public static boolean restore(String dbUsername, String dbPassword, String sourceFile)
            throws IOException, InterruptedException {
		
		sourceFile = pathToUploads + sourceFile + ".sql";
		
		String command = String.format("mysql -u%s -p%s %s < %s",
                dbUsername, dbPassword, dbName, sourceFile);
		
		Process process = Runtime.getRuntime().exec("cmd.exe /c " + command);
        int processComplete = process.waitFor();
                
        if (processComplete == 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
