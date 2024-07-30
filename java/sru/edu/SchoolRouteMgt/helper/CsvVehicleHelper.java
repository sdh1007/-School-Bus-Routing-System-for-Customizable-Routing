package sru.edu.SchoolRouteMgt.helper;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.sql.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.Vehicle;
import sru.edu.SchoolRouteMgt.repository.DepotRepository;
import sru.edu.SchoolRouteMgt.repository.VehicleRepository;

/*
 * Parses a csv file and returns a list of students
 * 
 * Reference: https://www.bezkoder.com/spring-boot-upload-csv-file/
 */

@Service
public class CsvVehicleHelper {
	  public static String TYPE = "text/csv";
	  private static String[] HEADERs = { "" };
	  private final static Set<String> validVehicleTypes = new HashSet<>(Arrays.asList( "bus", "van", "other"));
	  
	  private DepotRepository depotRepo;
	  
	  public CsvVehicleHelper(DepotRepository depotRepo) {
		  this.depotRepo = depotRepo;
	  }
	  
	  // Loops through the csv file and saves the data to a students domain
	  public List<Vehicle> csvToRepo(InputStream is) throws ParseException {
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withNullString(""));) {

	      List<Vehicle> vehicles = new ArrayList<Vehicle>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	      
	      // Check if all headers are there
	      
	      // Sets the format to parse from for the date - format is default excel date format
	      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US);
	      
	      // Loop over all records and assign variables
	      for (CSVRecord csvRecord : csvRecords) {
	    	  Vehicle vehicle = new Vehicle();

	    	  String vehicleCode = csvRecord.get("VehicleCode");
	    	  
	    	  String vehicleType = csvRecord.get("VehicleType");
	    	  if (!validVehicleTypes.contains(vehicleType.toLowerCase())) {
	    		  throw new RuntimeException("Invalid vehicle type: " + vehicleType + "' for vehicle '" + vehicleCode + "'");
	    	  }
	    	  
	    	  LocalDate enterDate = LocalDate.parse(csvRecord.get("EnterDate"), formatter);
			  Date enterDateSQL = Date.valueOf(enterDate);
			  
			  String locationType = csvRecord.get("LocationType");
			  if(locationType.toLowerCase().equals("depot")) {
				  String depotName = csvRecord.get("DepotName");
				  
				  if(depotName == "" || depotName == null) {
					  throw new RuntimeException("Cannot parse depot: No depot name" + "' for vehicle '" + vehicleCode + "'");
				  } else {
					  try {
						  vehicle.setDepot(depotRepo.findByName(depotName));
					  } catch(Exception e) {
						  throw new RuntimeException("failed to find depot '" + depotName + "' for vehicle '" + vehicleCode + "'");
					  }
				  }
			  }
			  
			  int seatNumber = CsvHelper.tryParseInt(csvRecord.get("SeatNumber"));
			  
			  boolean isActive = CsvHelper.tryParseBoolean(csvRecord.get("IsActive")); 
			  boolean hasDriver = CsvHelper.tryParseBoolean(csvRecord.get("hasDriver")); 
			  if (hasDriver) {
				  int driverId = CsvHelper.tryParseInt(csvRecord.get("DriverId"));
				  
				  /*if(driverId == "" || driverId == null) { // TODO
					  throw new RuntimeException("Cannot parse driver: No driver id" + "' for vehicle '" + vehicleCode + "'");
				  } else {
					  
					  // Driver driver = driverRepository.findById(driverId);
				  }*/
			  }
			  
			  vehicle.loadData(vehicleType, vehicleCode, enterDateSQL, locationType, seatNumber, isActive);
			  
			  vehicles.add(vehicle);
	      	}
	      
	      return vehicles;
		  } catch (IOException e) {
	    	throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		  }
	  }
}
