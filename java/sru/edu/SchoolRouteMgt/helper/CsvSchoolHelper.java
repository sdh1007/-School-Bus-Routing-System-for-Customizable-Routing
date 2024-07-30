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
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.web.multipart.MultipartFile;

import sru.edu.SchoolRouteMgt.domain.LocationPoint;
import sru.edu.SchoolRouteMgt.domain.Schools;

/*
 * Parses a csv file and returns a list of data objects
 * 
 * Reference: https://www.bezkoder.com/spring-boot-upload-csv-file/
 */

public class CsvSchoolHelper {
	  static String[] HEADERs = { "Id",	"SchoolName", "Type", "SchoolLevel", "studentCount", "StartHour", "StartMin", "StartAmPm", "EndHour", "EndMin", "EndAmPm", "SchoolDays", "StartDate", "EndDate", "Address1", "City", "State", "Zip", "Phone", "LocationPointId", "IsActive", "Latitude", "Longitude" };

	  // Loops through the csv file and saves the data to a schools domain
	  public static List<Schools> csvToRepo(InputStream is) throws ParseException {
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new BOMInputStream(is), "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

	      List<Schools> schools = new ArrayList<Schools>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	      
	      // Still need to check if all headers are there
	      
	      // Sets the format to parse from for the date - format is default excel date format
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US);

	      // Loop over all records and assign variables
	      for (CSVRecord csvRecord : csvRecords) {
			String schoolName = csvRecord.get("SchoolName");
			String type = csvRecord.get("Type");
			String schoolGrade = csvRecord.get("SchoolGrade");
			int startHour = CsvHelper.tryParseInt(csvRecord.get("StartHour"));
			int startMin = CsvHelper.tryParseInt(csvRecord.get("StartMin"));
			String startAmPm = csvRecord.get("StartAmPm");
			int endHour = CsvHelper.tryParseInt(csvRecord.get("EndHour"));
			int endMin = CsvHelper.tryParseInt(csvRecord.get("EndMin"));
			String endAmPm = csvRecord.get("EndAmPm");
			int schoolDays = CsvHelper.tryParseInt(csvRecord.get("SchoolDays"));
			
			LocalDate startDate = LocalDate.parse(csvRecord.get("StartDate"), formatter);
			Date startDateSQL = Date.valueOf(startDate);
			
			LocalDate endDate = LocalDate.parse(csvRecord.get("EndDate"), formatter);
			Date endDateSQL = Date.valueOf(endDate);
			
			String address = csvRecord.get("Address1");
			String city = csvRecord.get("City");
			String state = csvRecord.get("State");
			int zip = CsvHelper.tryParseInt(csvRecord.get("Zip"));
			String phone = csvRecord.get("Phone");
			boolean isActive = CsvHelper.tryParseBoolean(csvRecord.get("IsActive")); 
			float latitude = CsvHelper.tryParseFloat(csvRecord.get("Latitude"));
			float longitude = CsvHelper.tryParseFloat(csvRecord.get("Longitude"));
			
			// Set location point
			LocationPoint locationPoint = new LocationPoint();
			locationPoint.setLatitude(latitude);
			locationPoint.setLongitude(longitude);
			locationPoint.setLocationPointName(address);
			
			// Create School instance
			Schools schoolData = new Schools();
			
			// Insert all data for the school into the instance
			schoolData.loadData(schoolName, type, schoolGrade, 0, startHour, startMin, startAmPm, endHour, endMin, endAmPm,
					schoolDays, startDateSQL, endDateSQL, phone, zip, city, state, isActive, address);
			schoolData.setLocation(locationPoint);
			
			schools.add(schoolData);
	      }

	      	return schools;
	    } catch (IOException e) {
	    	throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
	    }
	  }
}