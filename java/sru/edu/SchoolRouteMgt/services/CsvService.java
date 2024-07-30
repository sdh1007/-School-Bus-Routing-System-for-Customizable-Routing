package sru.edu.SchoolRouteMgt.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Drivers;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.domain.Vehicle;
import sru.edu.SchoolRouteMgt.helper.CsvDepotHelper;
import sru.edu.SchoolRouteMgt.helper.CsvDriverHelper;
import sru.edu.SchoolRouteMgt.helper.CsvSchoolHelper;
import sru.edu.SchoolRouteMgt.helper.CsvStudentHelper;
import sru.edu.SchoolRouteMgt.helper.CsvVehicleHelper;
import sru.edu.SchoolRouteMgt.repository.DepotRepository;
import sru.edu.SchoolRouteMgt.repository.DriverRepository;
import sru.edu.SchoolRouteMgt.repository.SchoolRepository;
import sru.edu.SchoolRouteMgt.repository.StudentRepository;
import sru.edu.SchoolRouteMgt.repository.VehicleRepository;

/* 
 * Connects the controller to the helper and handles failure to saves
 * 
 * Reference: https://www.bezkoder.com/spring-boot-upload-csv-file/
 */

@Service
public class CsvService {
	@Autowired
	SchoolRepository SchoolRepo;
	@Autowired
	StudentRepository StudentRepo;
	@Autowired
	DepotRepository DepotRepo;
	@Autowired
	VehicleRepository VehicleRepo;
	@Autowired
	DriverRepository DriverRepo;
		
	public void saveSchools(MultipartFile file, District districtId) throws Exception {
		try {
			List<Schools> schools = CsvSchoolHelper.csvToRepo(file.getInputStream());
			for(Schools school : schools) {
				if(SchoolRepo.existsBySchoolName(school.getSchoolName())) {
					throw new Exception("School name: '" + school.getSchoolName() + "' already exists.");
				}
				
				school.setDistrictId(districtId);
			}
			SchoolRepo.saveAll(schools);
			
		} catch (IOException e) {	
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
		
	}
	
	public void saveStudents(MultipartFile file) throws ParseException {
		try {
			List<Students> students = CsvStudentHelper.csvToRepo(file.getInputStream());
			List<Schools> schools = new ArrayList<>();
			
			// Loop over students & search entered school name to increment total students for each school & add school object to student
			for (Students student : students) {
				Schools school = SchoolRepo.findBySchoolName(student.getSchoolName());
				
				if(school != null) {
					school.incrementStudentCount();
					schools.add(school);
					
					student.setSchool(school);
				}
				else {
					throw new RuntimeException(student.getSchoolName() + " does not exist.");
				}
			}

			SchoolRepo.saveAll(schools);
			StudentRepo.saveAll(students);
		} catch (IOException e) {	
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public void saveDepots(MultipartFile file, District districtId) throws ParseException {
		try {
			List<Depot> depots = CsvDepotHelper.csvToRepo(file.getInputStream());
			for(Depot depot : depots)
			{
				depot.setDistrictId(districtId);
			}
			DepotRepo.saveAll(depots);
			
		} catch (IOException e) {	
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}
	
	public void saveVehicles(MultipartFile file, District districtId) throws ParseException {
		CsvVehicleHelper vehicleHelper = new CsvVehicleHelper(DepotRepo); // Needed to make
		
		try {
			List<Vehicle> vehicles = vehicleHelper.csvToRepo(file.getInputStream());
			for(Vehicle vehicle : vehicles)
			{
				vehicle.setDistrict(districtId);
			}
			VehicleRepo.saveAll(vehicles);
		} catch (IOException e) {	
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}
	
	public void saveDrivers(MultipartFile file, District districtId) throws ParseException {
		try {
			List<Drivers> drivers = CsvDriverHelper.csvToRepo(file.getInputStream());
			for(Drivers driver : drivers)
			{
				driver.setDistrictId(districtId);
			}
			DriverRepo.saveAll(drivers);
		} catch (IOException e) {	
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	

	
}