package sru.edu.SchoolRouteMgt.toDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.FileInfo;
import sru.edu.SchoolRouteMgt.repository.DistrictRepository;
import sru.edu.SchoolRouteMgt.repository.FileRepository;

@Component
public class LoadDistrict implements CommandLineRunner {

	@Autowired private DistrictRepository districtRepository;
	@Autowired private FileRepository fileRepo;

	@Override
	public void run(String... args) throws Exception {

		int exists = (int) districtRepository.count();
 
		if(exists == 0) {
			// Prepare the file association
			FileInfo testLogo = new FileInfo("DefaultDistrictLogo.png", "http://localhost:8080/files/DefaultDistrictLogo.png", "png");
			FileInfo testBackground = new FileInfo("DefaultDistrictBackground.png", "http://localhost:8080/files/DefaultDistrictBackground.png", "png");
			
			// Create the district
			District testDistrict = new District("District_1", "A-123", "This is the default district.", "district.edu");
			
			// Set the file association
			testDistrict.setDistrictLogo(testLogo);
			testDistrict.setDistrictBackground(testBackground);

			// Save the district & file info
			districtRepository.save(testDistrict);
			
			System.out.println("Default district created.");

		} else {
			System.out.println("Default district already created.");
		}
	}
}