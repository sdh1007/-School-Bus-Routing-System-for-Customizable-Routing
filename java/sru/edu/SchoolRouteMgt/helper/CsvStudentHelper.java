package sru.edu.SchoolRouteMgt.helper;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.sql.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.repository.SchoolRepository;
import sru.edu.SchoolRouteMgt.repository.StudentRepository;

/*
 * Parses a csv file and returns a list of students
 * 
 * Reference: https://www.bezkoder.com/spring-boot-upload-csv-file/
 */

public class CsvStudentHelper {
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "" };
	private static SchoolRepository schoolRepo;

	// Loops through the csv file and saves the data to a students domain
	public static List<Students> csvToRepo(InputStream is) throws ParseException {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		CSVParser csvParser = new CSVParser(fileReader,
			CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));) {

	    List<Students> students = new ArrayList<Students>();

	    Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	    
	    // Check if all headers are there
	    
	    // Sets the format to parse from for the date - format is default excel date format
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US);     
	    
	    // Loop over all records and assign variables
	    for (CSVRecord csvRecord : csvRecords) {
			int studId = CsvHelper.tryParseInt(csvRecord.get("StudentId"));
			boolean graduated = CsvHelper.tryParseBoolean(csvRecord.get("Graduated"));
			String firstName = csvRecord.get("FirstName");
			String midName = csvRecord.get("MiddleName");
			String lastName = csvRecord.get("LastName");
			String suffix = csvRecord.get("Suffix");
			String grade = csvRecord.get("Grade");
			String address1 = csvRecord.get("Address1");
			String address2 = csvRecord.get("Address2");
			String city = csvRecord.get("City");
			String state = csvRecord.get("State");
			int zipCode = CsvHelper.tryParseInt(csvRecord.get("ZipCode"));
			String gender = csvRecord.get("Sex");
		        
			LocalDate birthDate = LocalDate.parse(csvRecord.get("DOB"), formatter);
			Date birthDateSQL = Date.valueOf(birthDate);
		        
			String phoneNum = csvRecord.get("Phone");
			String cellPhoneNum = csvRecord.get("CellPhone");
			String emergencyPhoneNum = csvRecord.get("EmergencyPhone");
			String parentName = csvRecord.get("ParentName");
			String schoolName = csvRecord.get("SchoolName");
		        
			LocalDate enter = LocalDate.parse(csvRecord.get("Enter"), formatter);
			Date enterSQL = Date.valueOf(enter);
		        
			boolean isEnter = CsvHelper.tryParseBoolean(csvRecord.get("IsEnter")); 
		        
			LocalDate withdrawl = LocalDate.parse(csvRecord.get("Withdrawl"), formatter);
			Date withdrawlSQL = Date.valueOf(withdrawl);
		        
			boolean isWithdrawl = CsvHelper.tryParseBoolean(csvRecord.get("IsWithdrawl")); 
			boolean hazardousRoad = CsvHelper.tryParseBoolean(csvRecord.get("HazardousRoad")); 
			String travelMode = csvRecord.get("TravelMode");
			String driverNote = csvRecord.get("DriverNote");
			String misc = csvRecord.get("Misc");
			String journal = csvRecord.get("Journal");
			String medical = csvRecord.get("Medical");
			boolean isCustody = CsvHelper.tryParseBoolean(csvRecord.get("IsCustody")); 
			String alt1Name = csvRecord.get("Alt1Name");
			String alt1Relationship = csvRecord.get("Alt1Relationship");
			String alt1Address1 = csvRecord.get("Alt1Address1");
			String alt1Address2 = csvRecord.get("Alt1Address2");
			String alt1City = csvRecord.get("Alt1City");
			String alt1State = csvRecord.get("Alt1State");
			long alt1ZipCode = CsvHelper.tryParseLong(csvRecord.get("Alt1ZipCode"));
			String alt1Phone = csvRecord.get("Alt1Phone");
			String alt1CellPhone = csvRecord.get("Alt1CellPhone");
			String alt2Name = csvRecord.get("Alt2Name");
			String alt2Relationship = csvRecord.get("Alt2Relationship");
			String alt2Address1 = csvRecord.get("Alt2Address1");
			String alt2Address2 = csvRecord.get("Alt2Address2");
			String  alt2City = csvRecord.get("Alt2City");
			String  alt2State = csvRecord.get("Alt2State");
			long alt2ZipCode = CsvHelper.tryParseLong(csvRecord.get("Alt2ZipCode"));
			String alt2Phone = csvRecord.get("Alt2Phone");
			String alt2CellPhone = csvRecord.get("Alt2CellPhone");
			String alt3Name = csvRecord.get("Alt3Name");
			String alt3Relationship = csvRecord.get("Alt3Relationship");
			String alt3Address1 = csvRecord.get("Alt3Address1");
			String alt3Address2 = csvRecord.get("Alt3Address2");
			String alt3City = csvRecord.get("Alt3City");
			String alt3State = csvRecord.get("Alt3State");
			long alt3ZipCode = CsvHelper.tryParseLong(csvRecord.get("Alt3ZipCode"));
			String alt3Phone = csvRecord.get("Alt3Phone");
			String alt3CellPhone = csvRecord.get("Alt3CellPhone");
			String studentType1 = csvRecord.get("StudentType1");
			String studentType2 = csvRecord.get("StudentType2");
			String studentType3 = csvRecord.get("StudentType3");
	    	  
		    // Create new student
		    Students student = new Students();
		    
		    // Save student data to student
		    student.loadData(studId, graduated, firstName, midName,
		    		lastName, suffix, grade, address1, address2, city,
		    		state, zipCode, gender, birthDateSQL, phoneNum,
		    		cellPhoneNum, emergencyPhoneNum, parentName, schoolName,
		    		enterSQL, isEnter, withdrawlSQL, isWithdrawl, hazardousRoad,
		    		travelMode, driverNote, misc, journal, medical,
		    		isCustody, alt1Name, alt1Relationship, alt1Address1,
		    		alt1Address2, alt1City, alt1State, alt1ZipCode,
		    		alt1Phone, alt1CellPhone, alt2Name, alt2Relationship,
		    		alt2Address1, alt2Address2, alt2City, alt2State,
		    		alt2ZipCode, alt2Phone, alt2CellPhone, alt3Name,
		    		alt3Relationship, alt3Address1, alt3Address2, alt3City,
		    		alt3State, alt3ZipCode, alt3Phone, alt3CellPhone,
		    		studentType1, studentType2, studentType3);
            
		    student.setSchoolName(schoolName);
		    
		    // Add the school that was updated with the student's data
		    students.add(student);
	    }
	      
	    return students;
		} catch (IOException e) {
	    	throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}
}
