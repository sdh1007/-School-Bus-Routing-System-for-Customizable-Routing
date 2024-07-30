/* Contributed by Group 3 - Zach Freilano
 * AppController focuses on the password size, along with the user being able to view the homepage,
while also allowing the user to login, they may also register for an account along with the password being encrypted 
and stored in a database, it also contains a list of users and their credentials who have registered. 
 */
package sru.edu.SchoolRouteMgt.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.maps.internal.HttpHeaders;

import net.bytebuddy.utility.RandomString;
import sru.edu.SchoolRouteMgt.datawriter.WriteToLog;
import sru.edu.SchoolRouteMgt.domain.*;
import sru.edu.SchoolRouteMgt.helper.ImageHelper;
import sru.edu.SchoolRouteMgt.repository.*;
import sru.edu.SchoolRouteMgt.services.CsvService;
import sru.edu.SchoolRouteMgt.services.DatabaseBackupService;
import sru.edu.SchoolRouteMgt.services.FilesStorageServiceImpl;
import sru.edu.SchoolRouteMgt.services.UserService;
import sru.edu.SchoolRouteMgt.controller.*;



@Controller
public class AppController {
	@Autowired private UserService userService;
	@Autowired private SchoolRepository schoolRepository;
	@Autowired private DistrictRepository districtRepo;
	@Autowired private RoleRepository RoleRepo;
	@Autowired private DataLogRepository loggingRepo;
	@Autowired private FileRepository fileRepo;
	@Autowired private UserRepository userRepository;
	@Autowired private JavaMailSender mailSender;
	@Autowired private StudentRepository studentRepository;
	@Autowired private DepotRepository depotRepository;
	@Autowired private VehicleRepository vehicleRepository;
	@Autowired private DriverRepository driversRepository;
	@Autowired private PickupDropoffRepository pickupDropoffRepository;
	@Autowired private RoutingRepository routingRepository;
	@Autowired private RouteGroupsRepository routeGroupsRepo;
	@Autowired private RoutingRepository routeRepo;
	
	
	private static final int MINPASSWORDSIZE = 5;  // password size must be 5 or more characters //
	private final FilesStorageServiceImpl storageService = new FilesStorageServiceImpl(); // Used to save/manage files

	WriteToLog logWriter = new WriteToLog();
	
	@Autowired
	  CsvService fileService;
	  
	  @GetMapping("/exportUsers")
	  public void exportToCSVUsers(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "users.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<User> users = userRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"User ID","E-mail","first_Name", "last_Name", "Roles","District"};
		  String[] nameMapping = {"id","email","firstName","lastName","roles","district" };
		  
		  csvWriter.writeHeader(csvHeader);
		  for(User user : users)
		  {
			  csvWriter.write(user, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportDistricts")
	  public void exportToCSVDistricts(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "districts.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<District> district = districtRepo.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"District_ID","District_Name","District_Code", "District_Description", "District_Domain","District_Logo","District_Background"};
		  String[] nameMapping = {"districtId","districtName","districtCode","districtDescription","districtDomain","districtLogo","districtBackground" };
		  
		  csvWriter.writeHeader(csvHeader);
		  for(District districs : district)
		  {
			  csvWriter.write(districs, nameMapping);
		  }
		  
		  csvWriter.close();
	  }

	  

	  @GetMapping("/exportSchools")
	  public void exportToCSVSchools(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "schools.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<Schools> schools = (List<Schools>) schoolRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"Name","Type","Number_Of_Students", "Grade", "School_Days","Start_Time","End_Time","Phone_Number"};
		  String[] nameMapping = {"schoolName","type","studentCount","schoolGrade","schoolDays","startAmPm","endAmPm","phone" };
		  
		  csvWriter.writeHeader(csvHeader);
		  for(Schools school : schools)
		  {
			  csvWriter.write(school, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportStudents")
	  public void exportToCSVStudents(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "students.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<Students> students = studentRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"School", "Student ID", "First Name", "Middle Name", "Last Name", "Suffix", "Grade", 
				  "Address", "Secondary Address", "City", "State", "Zip Code", "Gender", "Date-Of-Birth", "Phone Number", 
				  "Cell Phone Number", "Emergency Phone Number", "Parent Name", "Entry Date", "Withdraw Date", "Travel Mode", 
				  "Driver Note", "Misc Note", "Journal", "Medical", "Student Type"};
		  String[] nameMapping = {"schoolName","studentId","firstName","midName","lastName","suffix","grade",
				  "address1","address2","city","state","zipCode","gender","birthDate","phoneNum",
				  "cellPhoneNum","emergencyPhoneNum","parentName","enter","Withdrawl","travelMode",
				  "driverNote","misc","journal","medical","studentType1"};
		  
		  csvWriter.writeHeader(csvHeader);
		  for(Students student : students)
		  {
			  csvWriter.write(student, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportDepot")
	  public void exportToCSVDepot(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "depot.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<Depot> depot = depotRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"Address", "City","Depot_Name","Road_Name","Zip_Code"};
		  String[] nameMapping = {"address1","city","name","roadName","zipCode"};
		  
		  csvWriter.writeHeader(csvHeader);
		  for(Depot depots : depot)
		  {
			  csvWriter.write(depots, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportVehicle")
	  public void exportToCSVVehicle(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "vehicle.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<Vehicle> vehicle = vehicleRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"Vehicle_Type","Vehicle_Code","Number_of_Seats","Entry_Date","Location_Type","Active"};
		  String[] nameMapping = {"vehicleType","vehicleCode","seatNumber","enterDate","locationType","isActive"};
		  
		  csvWriter.writeHeader(csvHeader);
		  for(Vehicle vehicles : vehicle)
		  {
			  csvWriter.write(vehicles, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportDrivers")
	  public void exportToCSVDrivers(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "drivers.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<Drivers> driver = driversRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"Name","Type","Approved","Validity","Address","City","State","Zip Code",
				  "Driver Phone Number","DriverId","Cell Phone Number","Medical Clear Date","Completed Driver License Date",
				  "Motor Vehicle Record Date","Application Date","Driver Photo Date","CPR First Aid Date","TB Test Date",
				  "I9 Date","Act 151 Date","Act 114 DAte","ProvEmp Check","Act34 State DAte","Active?"};
		  String[] nameMapping = {"name","type","approved","isMeetAllReq","address1","city","state","zipCode",
				  "phoneNumber","driverId","cellPhone","medClearDate","commDrvLicDate",
				  "motorVehRecDate","applicationDate","driverPhotoLicDate","cprFirstAidDate","tbTestDate",
				  "i9Date","act151ChildAbuseDate","act114FedCrimeDate","isProvEmpCheck","act34PaStateDate","isActive"};
		  csvWriter.writeHeader(csvHeader);
		  for(Drivers drivers : driver)
		  {
			  csvWriter.write(drivers, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportPickupDropoff")
	  public void exportToCSVPickupDropoff(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "pickupDropoff.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<PickupDropoff> pickupDropoff = pickupDropoffRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		  
		  
		  String[] csvHeader = {"Name","Student Ids",
				  "Monday Pickup","Monday Dropoff",
				  "Tuesday Pickup","Tuesday Dropoff",
				  "Wednesday Pickup","Wednesday Dropoff",
				  "Thursday Pickup", "Thursday Dropoff",
				  "Friday Pickup","Friday Dropoff"};
		  String[] nameMapping = {"pickupDropoffName","students",
				  "monPickupPointLocation","monDropoffPointLocation",
				  "tuePickupPointLocation","tueDropoffPointLocation",
				  "wedPickupPointLocation","wedDropoffPointLocation",
				  "thurPickupPointLocation","thurDropoffPointLocation",
				  "friPickupPointLocation","friDropoffPointLocation"};
		  csvWriter.writeHeader(csvHeader);
		  for(PickupDropoff pickupDropoffs : pickupDropoff)
		  {
			  csvWriter.write(pickupDropoffs, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportGroups")
	  public void exportToCSVGroups(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "groups.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<RouteGroups> routeGroups = routeGroupsRepo.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		  
		  String[] csvHeader = {"Group_ID","Group_Name","Group Code","Group Description","Schools Added"};
		  String[] nameMapping = {"groupId","groupName","groupCode","groupDescription","selectSchools"};
		  csvWriter.writeHeader(csvHeader);
		  for(RouteGroups routeGroup : routeGroups)
		  {
			  csvWriter.write(routeGroup, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	  
	  @GetMapping("/exportRoute")
	  public void exportToCSVRouteGroups(HttpServletResponse response) throws IOException {
		  response.setContentType("text/csv");
		  String fileName = "route.csv";
		  
		  String headerKey = "Content-Disposition";
		  String headerValue = "attachement; filename" + fileName;
		  
		  response.setHeader(headerKey, headerValue);
		  
		  List<Routing> routing = routingRepository.findAll();
		
		  ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		  String[] csvHeader = {"Route_Name",
				  "Group",
				  "Vehicle",
				  "Driver",
				  "Starting_Location",
				  "Ending_Location",
				  "Total_Students", 
				  "Total_Pickup_Time",
				  "Total_Dropoff_Time"};
		  
		  String[] nameMapping = {"routeName",
				  "group",
				  "vehicle",
				  "driver",
				  "startingLocation",
				  "endingLocation",
				  "totalStudents",
				  "pickupTotalTravelTime",
				  "dropoffTotalTravelTime",};
		  csvWriter.writeHeader(csvHeader);
		  for(Routing route : routing)
		  {
			  csvWriter.write(route, nameMapping);
		  }
		  
		  csvWriter.close();
	  }
	
	@GetMapping("")
	public String viewHomePage() {
		return "landing";
	}
	
	@GetMapping("/requestAccount")
	public String viewRequestAccount() {
		return "requestAccount";
	}
		
	@GetMapping("/databaseOptions")
	public String databaseOptions() {
		return "databaseOptions";
	}
	
	@RequestMapping(value = "/databaseOptions", method = RequestMethod.POST, params = "backupDatabase")
	public String runBackupDatabase(@AuthenticationPrincipal CustomUserDetails user, Model model, @RequestParam("dbUsername") String dbUsername, 
			@RequestParam("dbPassword") String dbPassword, @RequestParam("sourceFile") String sourceFile) {
		DataLog logging = new DataLog();
		String message = "";

		try {
			if(DatabaseBackupService.backup(dbUsername, dbPassword, sourceFile) == true) {
				message = "Successfully created database backup: " + sourceFile;
			} else {
				message = "Unsuccessfully created database backup: " + sourceFile;
			}
		} catch (IOException | InterruptedException e) {
			message = "Could not backup database: " + e;
		}
		
		model.addAttribute("message", message);
		
		//Log the database backup
		logging.setUser(user.getUsername());
		logging.setInformation("Backed up database");
		logging.setLoggedDate();		
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		
		return "databaseOptions";
	}

	@RequestMapping(value = "/databaseOptions", method = RequestMethod.POST, params = "restoreDatabase")
	public String restoreDatabase(@AuthenticationPrincipal CustomUserDetails user, Model model, @RequestParam("dbUsername") String dbUsername, 
			@RequestParam("dbPassword") String dbPassword, @RequestParam("sourceFile") String sourceFile) {
		DataLog logging = new DataLog();
		String message = "";
		
		try {
			if(DatabaseBackupService.restore(dbUsername, dbPassword, sourceFile) == true) {
				message = "Successfully restored from database backup: " + sourceFile;
			} else {
				message = "Unsuccessfully restored from database backup: " + sourceFile;
			}
		} catch (IOException | InterruptedException e) {
			message = "Could not restore database: " + e;
		}
		
		model.addAttribute("message", message);
		
		//Log the database backup
		logging.setUser(user.getUsername());
		logging.setInformation("Restored database from backup");
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		
		return "databaseOptions";
	}

	@GetMapping("/users")
	public String listUsers(Model model, Principal principal) {
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		Set<Role> roles = user.getRoles();
		List<User> users = userRepository.findAll();

	    if (roles.contains(RoleRepo.findByName("System_Admin"))) {
	        model.addAttribute("listUsers", users);  
	    } 
	    else if (roles.contains(RoleRepo.findByName("District_Admin"))) {
	        District district = user.getDistrict();
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {
	            if (u.getDistrict() != null && u.getDistrict().equals(district)) {
	                filteredUsers.add(u);
	            }
	        }
	        model.addAttribute("listUsers", filteredUsers);  
	    }
	    
	    else if (roles.contains(RoleRepo.findByName("Transportation_Manager"))) {
	        District district = user.getDistrict();
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {     
	            if (u.getDistrict() == district && !u.getRoles().contains(RoleRepo.findByName("District_Admin"))) {
	                filteredUsers.add(u);
	            } 
	        }
	        model.addAttribute("listUsers", filteredUsers);
	    }
	  
	    
	   else if (roles.contains(RoleRepo.findByName("User"))) {
	        // Display only the user's own account
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {
	            if (u.getEmail().equals(email)) {
	                filteredUsers.add(u);
	                break;
	            }
	        }
	        model.addAttribute("listUsers", filteredUsers);
	    }
	    
	    model.addAttribute("districts", districtRepo.findAll());

	    return "users";
	}
	
	public String listUsers(Model model) {
		List<User> listUser = userService.listAll();
		
		model.addAttribute("listUsers", listUser);		// lists users who have created an account // 
		
		return "users"; // Returns the users html page // 
	}
	
	@GetMapping("/createUser")
	public String showRegistrationForm(Model model, User user) {
		List<Role> listRoles = userService.getRoles();
		model.addAttribute("user", new User()); 
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("districts",districtRepo.findAll());
		user.setDistrict(user.getDistrict());
		
		return "registrationForm";   				// if registering for account, responds signup form //
	}

	@PostMapping("/addUser")
	public String processRegister(User user) throws UnsupportedEncodingException, MessagingException {
		String pass = user.getPassword();
		
		Pattern pattern = Pattern.compile("[A-Za-z0-9]");
		Matcher matcher = pattern.matcher(pass);
		boolean containsSpecial = matcher.find();
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 
		String encodedPassword = encoder.encode(user.getPassword()); // Encrypts the password // 
		user.setPassword(encodedPassword);

		// Add check for the email domain to ensure it matches the selected distrit domain
		
		String randmonCode = RandomString.make(64);
		user.setVerificationCode(randmonCode);
		
		user.setAccountActivity(false);
		user.setDistrict(user.getDistrict());
		sendVerificationEmail(user);
		
		
		userRepository.save(user); 
		return "redirect:/users";
	}
	
	public void sendVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException {
		 String subeject ="Thank you for joining School Route Transportation Management Family";
		 String senderName="The SRTM";
		 String mailContent ="Hello " + user.getFirstName() +", </p>";
		 mailContent += "<p>You were recently added to the School Route Transportation Management District group.<br>Your username is:"
		 		+ " "+user.getEmail()+"<br> Your password is: password"
		 				+ "<br>This is a temporary password, please change it in your account settings</p>";
		 mailContent += "<p>thank you,<br>The SRTM</p>";
		
		 MimeMessage message = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(message);
		 
		 helper.setFrom("schoolmanagement2023@mail.com", senderName);
		 helper.setTo(user.getEmail());
		 helper.setSubject(subeject);
		 helper.setText(mailContent,true);
		 
		 mailSender.send(message);
	}

	@GetMapping("/usersAccount")
	public String viewUserAccount(Model model, Principal principal) {
	    String email = principal.getName();
		User user = userRepository.findByEmail(email);
		Set<Role> roles = user.getRoles();
		List<User> users = userService.listAll();

	   // model.addAttribute("user", user);
	    //model.addAttribute("currentUser", user); // Add currentUser attribute
	    
	    
	   if (roles.contains(RoleRepo.findByName("Transportation_Manager"))) {
	        // Display only the user's own account
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {
	            if (u.getEmail().equals(email)) {
	                filteredUsers.add(u);
	                break;
	            }
	        }
	        model.addAttribute("listUsers", filteredUsers);
	    }
	   else if (roles.contains(RoleRepo.findByName("User"))) {
	        // Display only the user's own account
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {
	            if (u.getEmail().equals(email)) {
	                filteredUsers.add(u);
	                break;
	            }
	        }
	        model.addAttribute("listUsers", filteredUsers);
	    }
	   else if (roles.contains(RoleRepo.findByName("System_Admin"))) {
	        // Display only the user's own account
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {
	            if (u.getEmail().equals(email)) {
	                filteredUsers.add(u);
	                break;
	            }
	        }
	        model.addAttribute("listUsers", filteredUsers);
	    }
	   else if (roles.contains(RoleRepo.findByName("District_Admin"))) {
	        // Display only the user's own account
	        List<User> filteredUsers = new ArrayList<>();
	        for (User u : users) {
	            if (u.getEmail().equals(email)) {
	                filteredUsers.add(u);
	                break;
	            }
	        }
	        model.addAttribute("listUsers", filteredUsers);
	    }
	    
	    return "usersAccount";
	}

	@GetMapping("/viewUser/{id}")
	public String viewUser(@PathVariable("id") Long id, Model model){
		User user = userService.get(id);
		List<Role> listRoles = userService.getRoles();
		
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("districts", districtRepo.findAll());
		
		return "view-user";
	}
	
	@GetMapping("/editUser/{id}")
	public String editUser(@PathVariable("id") Long id, Model model){
		User user = userService.get(id);
		List<Role> listRoles = userService.getRoles();
		
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("districts", districtRepo.findAll());
		
		return "update-user";
	}
	
	
	
	@PostMapping("/users/save")
	public String saveUser(User user) {
		userService.save(user);
		
		return "redirect:/users";
	}
	
	//updates the user information
	@PostMapping("/updateUser/{id}")
	public String updateUser(@AuthenticationPrincipal CustomUserDetails customUser, @PathVariable("id") int id, @Validated User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "update-district";
		}
		
		user.setId(id);
		
		//Log the edit district
		DataLog logging = new DataLog();
		logging.setUser(customUser.getUsername());
		logging.setInformation("Edited User: " + user.getFirstName() + user.getLastName());
		logging.setLoggedDate();
		
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		userService.save(user);
		loggingRepo.save(logging);
		
		return "redirect:/district";
	}

	 // new mapping for deleting a user
	@GetMapping("/deleteUser/{id}")
	 public String deleteUser(@PathVariable("id") Long id) {
	   userRepository.deleteById(id);
    
	   return "redirect:/users";
	 }
	
	//--------------------------------------------------
	//district options display page
	@GetMapping({"/district"})
	public String districtOptions(Model model,Principal principal) {
		//model.addAttribute("districts", districtRepo.findAll());
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		Set<Role> roles = user.getRoles();
		List<User> users = userService.listAll();
		
		if (roles.contains(RoleRepo.findByName("System_Admin"))) {
			model.addAttribute("districts", districtRepo.findAll());
	    } 
		else if (roles.contains(RoleRepo.findByName("District_Admin"))) {
		        District district = user.getDistrict();
		        model.addAttribute("districts", Collections.singletonList(district));
		} 
		
		return "district";
	}
	
	@GetMapping("/createDistrict")
	public String showDistrictRegistrationForm(Model model) {
		model.addAttribute("district", new District()); 

		return "create-district";
	}
		
	@PostMapping("/processDistrict")
	public String processDistrictRegistration(Model model, District district, @RequestParam("logo") MultipartFile logoFile, @RequestParam("background") MultipartFile backgroundFile) {
		String message = "";
		DataLog logging = new DataLog();
		FileInfo districtLogo = new FileInfo();
		FileInfo districtBackground = new FileInfo();
		
		if (!ImageHelper.hasImageFormat(logoFile) && !ImageHelper.hasImageFormat(backgroundFile)) {
			message = "Please upload a jpg/jpeg, png, or gif file!";
			model.addAttribute("message", message);
			
			return "create-district";
		}
		
		// Save the files to system and grabs sets the file info
		try {
			districtLogo = storageService.saveAndInfo(logoFile);
		} catch( Exception e) {
			message = "Could not upload logo: " + e;
	        model.addAttribute("message", message);
			
			return "create-district";
		}
		// Save the files to system and grabs sets the file info
		try {
			districtBackground = storageService.saveAndInfo(backgroundFile);
		} catch( Exception e) {
			message = "Could not upload background: " + e;
	        model.addAttribute("message", message);
			
			return "create-district";
		}

        // Save the information about the files
 		fileRepo.save(districtLogo);
 		fileRepo.save(districtBackground);
		
		// Set the file association
		district.setDistrictLogo(districtLogo);
		district.setDistrictBackground(districtBackground);
		
		// Save the district
		districtRepo.save(district);
		
		return "redirect:/district";
	}
	
	@GetMapping("/viewDistrict/{id}")
	public String viewDistrict(@PathVariable("id") Long id, Model model){
		District district = districtRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid District Id:" + id));
		
		model.addAttribute("district", district); 
		
		return "view-district";
	}
	
	@GetMapping("/editDistrict/{id}")
	public String editDistrict(@PathVariable("id") Long id, Model model){
		District district = districtRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid District Id:" + id));
		
		model.addAttribute("district", district); 
		
		return "update-district";
	}
	
	//updates the district information
	@PostMapping("/updateDistrict/{id}")
	public String updateDepot(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, @Validated District district, BindingResult result, Model model) {
		if (result.hasErrors()) {
			district.setDistrictId(id);
			return "update-district";
		}
		
		district.setDistrictId(id);
		
		
		
		//Log the edit district
		DataLog logging = new DataLog();
		
		logging.setUser(user.getUsername());
		logging.setInformation("Edited district: " + district.getDistrictName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		districtRepo.save(district);
		loggingRepo.save(logging);

		return "redirect:/district";
	}
	
	@GetMapping("/deleteDistrict/{id}")
	 public String deleteDistrict(@PathVariable("id") Long id) {
		districtRepo.deleteById(id);
	    return "redirect:/district";
	}
	
	
	//--------------------------------------------------
	// all below are for Groups
	//group options display page
	@GetMapping("/schoolGroups")
	public String viewGroups(Model model,@AuthenticationPrincipal CustomUserDetails user)
	{
		District district = user.getDistrict(); // get the district of the logged-in user
		List<RouteGroups> routeroups = routeGroupsRepo.findByDistrict(district); // filter the schools by district
		model.addAttribute("routegroups", routeroups);
		return "schoolGroups";
	}
	
	@GetMapping("/createGroup")
	public String createGroup(Model model) {
		model.addAttribute("schools", schoolRepository.findAllByOrderBySchoolNameAsc());
		model.addAttribute("routegroups", new RouteGroups());
		return "create-group";
	}
	
	// Mapping to save school(s) to a group
	@RequestMapping({"/processGroup"})
	public String processGroup(@AuthenticationPrincipal CustomUserDetails user, @Validated RouteGroups routeGroups, 
			BindingResult result, Model model, @RequestParam("selectSchools") Set<Schools> selectSchools) {
		if (result.hasErrors()) {
			return "error"; // Change this to output why
		}
		DataLog logging = new DataLog();
		
		routeGroups.setDistrictId(user.getDistrict());	
		//routeGroups.setSelectSchools(selectSchools);
		
		//Logged the added school(s) to group
		logging.setUser(user.getUsername());
		logging.setInformation("Added Schools to Groups.");
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		routeGroupsRepo.save(routeGroups); // Saves the school(s) to the group it was added to
		
		return "redirect:/schoolGroups";
	}
	
	@GetMapping("/viewGroup/{id}")
	public String viewGroup(@PathVariable("id") long id, Model model) {
	    RouteGroups routegroups = routeGroupsRepo.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid Group Id:" + id));

	    Set<Schools> schools = routegroups.getSelectSchools(); // get the schools in the group
	    List<Schools> sortedSchools = new ArrayList<>(schools); // convert to list
	    sortedSchools.sort(Comparator.comparing(Schools::getSchoolName)); // sort by school name

	    model.addAttribute("routegroups", routegroups);
	    model.addAttribute("sortedSchools", sortedSchools); // add the sorted schools list to the model

	    return "view-group";
	}
	
	// edit whichever group the transportation manager desires
	@GetMapping("/editGroup/{id}")
	public String editGroup(@PathVariable("id") Long id, Model model){
		
		RouteGroups routegroups = routeGroupsRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Group Id:" + id));
		
		model.addAttribute("routegroups", routegroups);
		model.addAttribute("schools", schoolRepository.findAllByOrderBySchoolNameAsc());
		
		return "update-group";
	}
	
	// update specific group
	@PostMapping("/updateGroup/{id}")
	public String updateGroup(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, @Validated RouteGroups routegroups, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "redirect:/update-group/"+id;
	    }
	    
	    routegroups.setGroupId(id);
	    routegroups.setDistrictId(user.getDistrict()); // assigns the logged in user's district to the group

	    //Log the edit groups
	    DataLog logging = new DataLog();
	    logging.setUser(user.getUsername());
	    logging.setInformation("Edited group: " + routegroups.getGroupName());
	    logging.setLoggedDate();
	    logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");

	    routeGroupsRepo.save(routegroups);
	    loggingRepo.save(logging);

	    return "redirect:/schoolGroups";
	}
	
	// delete groups by specific id
	@GetMapping("/deleteGroup/{id}")
	 public String deleteGroup(@PathVariable("id") Long id) {
		RouteGroups routeGroups = routeGroupsRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Group Id:" + id));
		
		// Delete all the routes that have this group
		for(Routing route : routeRepo.findAllByGroup(routeGroups)) {
			routeRepo.delete(route);
		}
		
		routeGroupsRepo.delete(routeGroups);
		
		return "redirect:/schoolGroups";
	}
}
