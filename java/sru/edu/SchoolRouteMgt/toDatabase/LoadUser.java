/*
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Load User
 * First the system counts the tables in the user table to see if there is any existing data. If the result returns 0 it follows the
 * provided steps. It creates an instance of the user class named admin that gets assigned it's password and username information.
 * The password is then encrypted and pushed to the database for the the user to sign into.
 * If the result is not 0, it skips the initial load and simply returns the count and a notification
 * that the database has data. This allows for data persistence between sessions.
 * 
 * Main Author: Lucas Luczak
 */
package sru.edu.SchoolRouteMgt.toDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Role;
import sru.edu.SchoolRouteMgt.domain.User;
import sru.edu.SchoolRouteMgt.repository.DistrictRepository;
import sru.edu.SchoolRouteMgt.repository.RoleRepository;
import sru.edu.SchoolRouteMgt.repository.UserRepository;

@Component
public class LoadUser implements CommandLineRunner { //runs when you load it

	@Autowired private RoleRepository roleRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private DistrictRepository districtRepo;
	
	final String password = "password"; // sets the default password for all the accounts
	
	@Override
	public void run(String... args) throws Exception {

		int exists = (int) userRepo.count();
		
		if(exists == 0) {
			District testDistrict = new District();
			testDistrict = districtRepo.findByName("District_1");
			
			createSysAdmin(); // Create Admin
			createDistrictAdmin(testDistrict); // Create Admin
			createDistrictManager(testDistrict); // Create Manager
			createUser(testDistrict); // Create Manager
			
			System.out.println("Default accounts created.");
		} else {
			System.out.println("Default accounts already created");
		}
	}
	
	// Create default system admin account
	public void createSysAdmin() {
		User admin = new User();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		admin.setEmail("admin@email.com");
		admin.setFirstName("System");
		admin.setLastName("Admin");
		
		Role adminRole = roleRepo.findByName("System_Admin");
		admin.addRole(adminRole);

		String encodedPassword = encoder.encode(password);
		admin.setPassword(encodedPassword);
		
		userRepo.save(admin);
	}
	
	// Create default district admin account
	public void createDistrictAdmin(District testDistrict) {
		User disAdmin = new User();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		disAdmin.setEmail("districtAdmin@email.com");
		disAdmin.setFirstName("District");
		disAdmin.setLastName("Admin");
		disAdmin.setDistrict(testDistrict);
		
		Role districtAdminRole = roleRepo.findByName("District_Admin");
		disAdmin.addRole(districtAdminRole);

		String encodedPassword = encoder.encode(password);
		disAdmin.setPassword(encodedPassword);
		
		userRepo.save(disAdmin);
	}
	
	// Create default transportation manager account
	public void createDistrictManager(District testDistrict) {
		User manager = new User();

		manager.setEmail("manager@email.com");
		manager.setFirstName("Transportation");
		manager.setLastName("Manager");
		manager.setDistrict(testDistrict);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		Role managerRole = roleRepo.findByName("Transportation_Manager");
		manager.addRole(managerRole);

		String encodedPassword = encoder.encode(password);
		manager.setPassword(encodedPassword);
		
		userRepo.save(manager);
	}
	
	// Create default user account
	public void createUser(District testDistrict) {
		User user = new User();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		user.setEmail("user@email.com");
		user.setFirstName("System");
		user.setLastName("User");
		user.setDistrict(testDistrict);
		
		Role userRole = roleRepo.findByName("User");
		user.addRole(userRole);

		String encodedPassword = encoder.encode(password);
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
	}
}