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

import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.LocationPoint;
import sru.edu.SchoolRouteMgt.repository.DepotRepository;

/*
 * Parses a csv file and returns a list of students
 * 
 * Reference: https://www.bezkoder.com/spring-boot-upload-csv-file/
 */

public class CsvDepotHelper {
	  static String[] HEADERs = { "" };
	  private DepotRepository depotRepo;


	  // Loops through the csv file and saves the data to a students domain
	  public static List<Depot> csvToRepo(InputStream is) throws ParseException {
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));) {

	      List<Depot> depots = new ArrayList<Depot>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	      
	      // Check if all headers are there
	      
	      // Sets the format to parse from for the date - format is default excel date format
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US);

	      // Loop over all records and assign variables
	      for (CSVRecord csvRecord : csvRecords) {
	    	  String contractorId = csvRecord.get("ContractorId");
	    	  String name = csvRecord.get("Name");
	    	  String address1 = csvRecord.get("Address1");
	    	  String address2 = csvRecord.get("Address2");
	    	  String city = csvRecord.get("City");
	    	  String state = csvRecord.get("State");
	    	  String zip = csvRecord.get("ZipCode");
	    	  String roadNo = csvRecord.get("RoadNo");
	    	  String roadName = csvRecord.get("RoadName");
	    	  boolean active = CsvHelper.tryParseBoolean(csvRecord.get("IsActive"));
	    	  float latitude = CsvHelper.tryParseFloat(csvRecord.get("Latitude"));
	    	  float longitude = CsvHelper.tryParseFloat(csvRecord.get("Longitude"));
	    	  
			  LocationPoint locationPoint = new LocationPoint();
			  
			  locationPoint.setLatitude(latitude);
			  locationPoint.setLongitude(longitude);
			  locationPoint.setLocationPointName(address1);
			  
			  Depot depot = new Depot();
			  
			  depot.loadData(contractorId, name, address1, address2, city, state, zip, roadNo, roadName, active);
			  depot.setLocation(locationPoint);
			  		      
		      // Add the depot to the list
			  depots.add(depot);
	      	}
	      
	      return depots;
		  } catch (IOException e) {
	    	throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		  }
	  }
}
