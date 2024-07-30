package sru.edu.SchoolRouteMgt.helper;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import sru.edu.SchoolRouteMgt.domain.Drivers;

/*
 * Parses a csv file and returns a list of students
 * 
 * Reference: https://www.bezkoder.com/spring-boot-upload-csv-file/
 */

public class CsvDriverHelper {
	  public static String TYPE = "text/csv";
	  static String[] HEADERs = { "" };
	  
	  // Loops through the csv file and saves the data to a students domain
	  public static List<Drivers> csvToRepo(InputStream is) throws ParseException {
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));) {

	      List<Drivers> drivers = new ArrayList<Drivers>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	      
	      // Check if all headers are there
	      
	      // Sets the format to parse from for the date - format is default excel date format
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US);
	      
	      // Loop over all records and assign variables
	      for (CSVRecord csvRecord : csvRecords) {
		      int contractorId = CsvHelper.tryParseInt(csvRecord.get("ContractorId"));
		      String name = csvRecord.get("Name");
		      String type = csvRecord.get("Type");
		      
		      LocalDate approved = LocalDate.parse(csvRecord.get("Approved"), formatter);
		      Date approvedSQL = Date.valueOf(approved);
              
		      boolean isMeetAllReq = CsvHelper.tryParseBoolean(csvRecord.get("IsMeetAllReq"));
		      String address1 = csvRecord.get("Address1");
		      String address2 = csvRecord.get("Address2");
		      String city = csvRecord.get("City");
		      String state = csvRecord.get("State");
		      long zipCode = CsvHelper.tryParseLong(csvRecord.get("ZipCode"));
		      String phoneNumber = csvRecord.get("Phone");
		      String cellPhone = csvRecord.get("CellPhone");
              
		      LocalDate medClearDate = LocalDate.parse(csvRecord.get("MedClearDate"), formatter);
		      Date medClearDateSQL = Date.valueOf(medClearDate);
              
		      LocalDate commDrvLicDate = LocalDate.parse(csvRecord.get("CommDrvLicDate"), formatter);
		      Date commDrvLicDateSQL = Date.valueOf(commDrvLicDate);
              
		      LocalDate motorVehRecDate = LocalDate.parse(csvRecord.get("MotorVehRecDate"), formatter);
		      Date motorVehRecDateSQL = Date.valueOf(motorVehRecDate);
              
		      LocalDate applicationDate = LocalDate.parse(csvRecord.get("ApplicationDate"), formatter);
		      Date applicationDateSQL = Date.valueOf(applicationDate);
              
		      LocalDate driverPhotoLicDate = LocalDate.parse(csvRecord.get("DriverPhotoLicDate"), formatter);
		      Date driverPhotoLicDateSQL = Date.valueOf(driverPhotoLicDate);
              
		      LocalDate cprFirstAidDate = LocalDate.parse(csvRecord.get("CprFirstAidDate"), formatter);
		      Date cprFirstAidDateSQL = Date.valueOf(cprFirstAidDate);
              
		      LocalDate tbTestDate = LocalDate.parse(csvRecord.get("TbTestDate"), formatter);
		      Date tbTestDateSQL = Date.valueOf(tbTestDate);
              
		      LocalDate i9Date = LocalDate.parse(csvRecord.get("I9Date"), formatter);
		      Date i9DateSQL = Date.valueOf(i9Date);
              
		      LocalDate act151ChildAbuseDate = LocalDate.parse(csvRecord.get("Act151ChildAbuseDate"), formatter);
		      Date act151ChildAbuseDateSQL = Date.valueOf(act151ChildAbuseDate);
              
		      LocalDate act114FedCrimeDate = LocalDate.parse(csvRecord.get("Act114FedCrimeDate"), formatter);
		      Date act114FedCrimeDateSQL = Date.valueOf(act114FedCrimeDate);
              
		      boolean isProvEmpCheck = CsvHelper.tryParseBoolean(csvRecord.get("IsProvEmpCheck"));
              
		      LocalDate act34PaStateDate = LocalDate.parse(csvRecord.get("Act34PaStateDate"), formatter);
		      Date act34PaStateDateSQL = Date.valueOf(act34PaStateDate);
              
		      long locationPointId = CsvHelper.tryParseLong(csvRecord.get("LocationPointId"));
		      boolean isActive = CsvHelper.tryParseBoolean(csvRecord.get("IsActive"));
              	    	  
		      // Create new driver
			  Drivers driver = new Drivers();
			  
			  // Save driver data
			  driver.loadData(act114FedCrimeDateSQL, act151ChildAbuseDateSQL, act34PaStateDateSQL,
			  		address1, address2, applicationDateSQL, approvedSQL, cellPhone, city, commDrvLicDateSQL,
			  		contractorId, cprFirstAidDateSQL, driverPhotoLicDateSQL, i9DateSQL, isActive,
			  		isMeetAllReq, isProvEmpCheck, locationPointId, medClearDateSQL, motorVehRecDateSQL, 
			  		name, phoneNumber, state, tbTestDateSQL, type, zipCode);
              		      
		      // Add the school that was updated with the student's data
			  drivers.add(driver);
	      	}
	      
	      return drivers;
		  } catch (IOException e) {
	    	throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		  }
	  }
}
