/*
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Main Controller
 * Controls a majority of the html pages and actions, as well as pushing data to the various databases when users
 * add new objects. Also handles deleting, and editing of these objects with respect to their databases. Routing and pickups where
 * separate because their testing required a bit more work so they where separated from the main controllers.
 * 
 * Main Author: Lucas Luczak
 */
package sru.edu.SchoolRouteMgt.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sru.edu.SchoolRouteMgt.datawriter.WriteToLog;
import sru.edu.SchoolRouteMgt.domain.CustomUserDetails;
import sru.edu.SchoolRouteMgt.domain.DataLog;
import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Drivers;
import sru.edu.SchoolRouteMgt.domain.RouteGroups;
import sru.edu.SchoolRouteMgt.domain.Routing;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.domain.Vehicle;
import sru.edu.SchoolRouteMgt.helper.CsvHelper;
import sru.edu.SchoolRouteMgt.repository.DataLogRepository;
import sru.edu.SchoolRouteMgt.repository.DepotRepository;
import sru.edu.SchoolRouteMgt.repository.DistrictRepository;
import sru.edu.SchoolRouteMgt.repository.DriverRepository;
import sru.edu.SchoolRouteMgt.repository.LocationPointRepository;
import sru.edu.SchoolRouteMgt.repository.RoleRepository;
import sru.edu.SchoolRouteMgt.repository.RouteGroupsRepository;
import sru.edu.SchoolRouteMgt.repository.RoutingRepository;
import sru.edu.SchoolRouteMgt.repository.SchoolRepository;
import sru.edu.SchoolRouteMgt.repository.StudentRepository;
import sru.edu.SchoolRouteMgt.repository.UserRepository;
import sru.edu.SchoolRouteMgt.repository.VehicleRepository;
import sru.edu.SchoolRouteMgt.services.CsvService;


@Controller
public class SchoolRouteMgtController {
	//set up Repository variables
	private StudentRepository studentRepository;
	private DepotRepository depotRepository;
	private SchoolRepository schoolRepository;
	private DriverRepository driverRepo;
	private RoutingRepository routingRepo;
	private RouteGroupsRepository routeGroupsRepo;
	private VehicleRepository vehRepo;
	private DataLogRepository loggingRepo;
	private DistrictRepository districtRepo;
	private RoleRepository roleRepo;
	
	@Autowired
	CsvService csvService;

	WriteToLog logWriter = new WriteToLog();
	JFrame frame = new JFrame();

	//create an UserRepository instance - instantiation (new) is done by Spring
	public SchoolRouteMgtController(DataLogRepository loggingRepo, VehicleRepository vehRepo,
			DriverRepository driverRepo, RoutingRepository routingRepo, RouteGroupsRepository routeGroupsRepo, SchoolRepository schoolRepository,
			StudentRepository studentRepository, DepotRepository depotRepository, DistrictRepository districtRepo, RoleRepository roleRepo) {
		this.studentRepository = studentRepository;
		this.depotRepository = depotRepository;
		this.schoolRepository = schoolRepository;
		this.driverRepo = driverRepo;
		this.routingRepo = routingRepo;
		this.routeGroupsRepo = routeGroupsRepo;
		this.vehRepo = vehRepo;
		this.loggingRepo = loggingRepo;
		this.districtRepo = districtRepo;
		this.roleRepo=roleRepo;
	}
	
	//display index page
	@RequestMapping({"/index"})
	public String requestData() {
		return "home"; 
	}

	/*Copied solution for missing parameter from stack overflow
	 * https://stackoverflow.com/questions/37746428/java-spring-how-to-handle-missing-required-request-parameters
	 * slightly modified to display pop-up on error.
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
		String name = ex.getParameterName();
		System.out.println(name + " parameter is missing");
		JOptionPane.showMessageDialog(frame, "Please select an option from the checkboxes", "Checkbox Error", JOptionPane.WARNING_MESSAGE);
		String referer = request.getHeader("Referer");
		return "redirect:"+ referer;
	}
	
	/* -----------------------------------------------------------------------------------
	 * School options
	 -----------------------------------------------------------------------------------*/

	// list schools & options //
	@RequestMapping({"/school"})
	public String schoolOptions(Model model, @AuthenticationPrincipal CustomUserDetails user) {
	    District district = user.getDistrict(); // get the district of the logged-in user
	    List<Schools> schools = schoolRepository.findByDistrict(district); // filter the schools by district
	    model.addAttribute("schools", schools); 
	    return "school";
	}

	// creates a school form with info // 
	@RequestMapping({"/createSchool"}) 
	public String showCreateSchoolForm(Schools school) {
		return "create-school"; // returns create school // 
	}

	// creates a school form with info // 
	@RequestMapping({"/uploadSchool"}) 
	public String uploadSchools(Schools school, Model model, @AuthenticationPrincipal CustomUserDetails user, @RequestParam("file") MultipartFile file) {
		String message = "";
		DataLog logging = new DataLog();
		
		// Check if CSV file and then which service to call based on user input
		if (CsvHelper.hasCSVFormat(file)) {
			try {
				csvService.saveSchools(file, user.getDistrict());
				
		        message = "Loaded the file successfully: " + file.getOriginalFilename();
		        model.addAttribute("message", message);
		        
		        logging.setUser(user.getUsername());
				logging.setInformation("Loaded schools data with file: " + file.getOriginalFilename());
				logging.setLoggedDate();
				loggingRepo.save(logging);
				logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		    } catch (Exception e) {
		        message = "Could not load the file: " + file.getOriginalFilename() + ". " + e.getMessage();
		        model.addAttribute("message", message);
		    }
		 }
		else {
			message = "Please upload a csv file!";
			model.addAttribute("message", message);
		}
		
		return "create-school";
	}
	
	//adds a school to a new district, returns to index //
	@RequestMapping({"/addSchool"}) 
	public String addSchool(@AuthenticationPrincipal CustomUserDetails user, @Validated Schools school, BindingResult result, Model model) {
		// Check if school with name already exists
		if (result.hasErrors()) {
			return "error";
			
		} else if(schoolRepository.existsBySchoolName(school.getSchoolName())) {
			String message = "ERROR: School with this name already exists";
			model.addAttribute("errorMessage", message);
			
			return "create-school";
		}
		
		school.setDistrictId(user.getDistrict());	
		
		//Logged the added school
		DataLog logging = new DataLog();
		logging.setUser(user.getUsername());
		logging.setInformation("Added School: "	+ school.getSchoolName());
		logging.setLoggedDate();
		
		loggingRepo.save(logging);
		schoolRepository.save(school); // once the school has been saved, redirects to /school page // 
		
		return "redirect:/school";
	}

	@GetMapping("/viewSchool/{id}") // view the school ID, can be invalid if ID not found // 
	public String viewSchoolForm(@PathVariable("id") long id, Model model) {
		Schools school = schoolRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid School Id:" + id));

		model.addAttribute("schools", school);
		return "view-school";
	}
	
	// editing the school ID, can be invalid if ID not found // 
	@GetMapping("/editSchool/{id}") 
	public String showUpdateSchoolForm(@PathVariable("id") long id, Model model) {

		Schools school = schoolRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid School Id:" + id));
				
		model.addAttribute("schools", school);
		return "update-school"; 		// updates the school information, such as ID after updated // 
	}

	// requests mapping for updating the school IDs //
	@PostMapping("/updateSchool/{id}") 
	public String updateSchool(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, @Validated Schools school, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "error";  
		}
		
		Schools prevSchool = schoolRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid School Id:" + id));
			
		// Check if new name is different from old name & check if the new name already exists
		if(!prevSchool.getSchoolName().equals(school.getSchoolName()) && schoolRepository.existsBySchoolName(school.getSchoolName())) {			
			String message = "ERROR: School with this name already exists";
			model.addAttribute("errorMessage", message);
			
			return "update-school";
		}
		
		// updates the school information //
		school.setId(id);
		school.setDistrictId(user.getDistrict());
		school.setStudent(prevSchool.getStudent());
		school.setStudentCount(prevSchool.getStudentCount());
		
		//Logged the edit School
		DataLog logging = new DataLog();
		logging.setUser(user.getUsername());
		logging.setInformation("Edit School: " + school.getSchoolName());
		logging.setLoggedDate();
		
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		schoolRepository.save(school);
		
		return "redirect:/school"; // redirects the user to the school page // 
	}

	// Mapping for the /delete/id URL to delete a user     
	@GetMapping("/deleteSchool/{id}") // deletes the school // 
	public String deleteSchool(@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable("id") long id, Model model) {
		
		DataLog logging = new DataLog();
		
		Schools school = schoolRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		
		// Remove the school from all routeGroups
		for(RouteGroups group : routeGroupsRepo.findBySelectSchools(school))  {
			group.getSelectSchools().remove(school);
			
			if(group.getSelectSchools().isEmpty()) {
				// Delete all the routes that have this group before deleting group
				for(Routing route : routingRepo.findAllByGroup(group)) {
					routingRepo.delete(route);
				}
				
				routeGroupsRepo.delete(group);
			}
		}
		
		// Remove every student's association within the school with routes
		for(Students student : school.getStudent()) {
			for(Routing route : routingRepo.findAllByPickupStudents(student)) {
				route.setTotalStudents(route.getTotalStudents() - 1);
				route.getPickupStudents().remove(student);
				route.getDropoffStudents().remove(student);
				
				// Count the number of times the student's pickup/dropoff occurs on the route - there is probably a better way of doing this
				int count = 0;
				for (Students routeStudent : route.getPickupStudents()) {
					if(routeStudent.getPickupDropoff() == student.getPickupDropoff()) {
						count++;
					}
				}
				
				// If the student was the only one on the route with the pickup/dropoff point, decrement the route's total stops
				if(count == 1) {
					route.setTotalStops(route.getTotalStops() - 1);
				}
			}
		}
		
		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Removed School: " + school.getSchoolName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");

		loggingRepo.save(logging);
		schoolRepository.delete(school);
		
		return "redirect:/school";
	}
	
	/* -----------------------------------------------------------------------------------
	 * Student options
	 -----------------------------------------------------------------------------------*/
	
	//student options display page
	@RequestMapping({"/student"})
	public String studentOptions(Model model, @AuthenticationPrincipal CustomUserDetails user) {
	    District district = user.getDistrict(); // get the district of the logged-in user
	    List<Schools> schools = schoolRepository.findByDistrict(district); // filter the schools by district
	    List<Students> students = (List<Students>) studentRepository.findAllBySchoolIn(schools); // filter the students by schools belonging to the district
	    model.addAttribute("schools", schools);
	    model.addAttribute("students", students);
	    return "students";
	}

	//returns form to add a student
	@RequestMapping({"/createStudent"})
	public String showCreateStudentForm(Students student, Schools school, Model model) {
		model.addAttribute("schools", schoolRepository.findAll());
		return "create-student";
	}
	
	@RequestMapping({"/uploadStudents"}) // creates a school form with info // 
	public String uploadStudents(Students student, Schools school, Model model, @AuthenticationPrincipal CustomUserDetails user, @RequestParam("file") MultipartFile file) {
		String message = "";
		DataLog logging = new DataLog();	
		
		// Check if CSV file and then which service to call based on user input
		if (CsvHelper.hasCSVFormat(file)) {
			try {
				csvService.saveStudents(file);
				
		        message = "Loaded the file successfully: " + file.getOriginalFilename();
		        model.addAttribute("message", message);
		        
		        logging.setUser(user.getUsername());
				logging.setInformation("Loaded students data with file: " + file.getOriginalFilename());
				logging.setLoggedDate();
				loggingRepo.save(logging);
				logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		    } catch (Exception e) {
		        message = "Could not load the file: " + file.getOriginalFilename() + ". " + e.getMessage();
		        model.addAttribute("message", message);
		    }
		 }
		else {
			message = "Please upload a csv file!";
			model.addAttribute("message", message);
		}
		
		model.addAttribute("schools", schoolRepository.findAll());
		
		return "create-student";
	}

	// Adds a student for from the /createStudent form
	@RequestMapping({"/addStudent"})
	public String addStudent(@AuthenticationPrincipal CustomUserDetails user, @Validated Students student, @Validated Schools school, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "error";
		}
		DataLog logging = new DataLog();
		
		student.setSchool(school);
		student.setSchoolName(school.getSchoolName());
		school.incrementStudentCount();
		
		//Logged the added student
		logging.setUser(user.getUsername());
		logging.setInformation("Added Student: " + student.getFirstName() +" "+ student.getLastName() + " To School: "
				+ school.getSchoolName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		studentRepository.save(student);
		
		return "redirect:/student";
	}

	@GetMapping("/viewStudent/{id}")
	public String viewStudentForm(@PathVariable("id") int id, Schools school, Model model) {
		Students student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Student Id:" + id));
				
		model.addAttribute("schools", student.getSchool());
		model.addAttribute("students", student);
		return "view-student";
	}
	
	// Shows the editStudent form
	@GetMapping("/editStudent/{id}")
	public String showUpdateStudentForm(@PathVariable("id") int id, Schools school, Model model) {
		Students student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Student Id:" + id));
		
		model.addAttribute("schools", schoolRepository.findAll());
		model.addAttribute("students", student);
		return "update-student";
	}
	
	// edit student information based on where the student attends
	@PostMapping("/updateStudent")
	public String updateStudent(@AuthenticationPrincipal CustomUserDetails user, @Validated Students student, @Validated Schools school, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "redirect:/editStudent/"+student.getStudentId();
		}
		
		// Get prevStudent values
		Students prevStudent = studentRepository.findById(student.getStudentId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + student.getStudentId()));
		
		// If school is different, decrease old student count & increase new one
		if(prevStudent.getSchool() != school) {
			prevStudent.getSchool().decrementStudentCount(); // Decrease prev school student count
		
			school.incrementStudentCount();
		}
		
		student.setSchool(school);
		student.setSchoolName(school.getSchoolName());
			
		//Logged the edit student
		DataLog logging = new DataLog();
		logging.setUser(user.getUsername());
		logging.setLoggedDate();
		logging.setInformation("Edited student: " + student.getFirstName() +" "+ student.getLastName());
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		studentRepository.save(student);
		loggingRepo.save(logging);
		
		return "redirect:/student";
	}
	
	//delete student method by erasing the students from the respository
	@GetMapping("/deleteStudent/{id}")
	public String deleteStudent(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") int id, Model model) {
		DataLog logging = new DataLog();
		
		Students student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		
		long studentSchoolId = student.getSchool().getId();
		Schools school = schoolRepository.findById(studentSchoolId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid newschool Id:" + studentSchoolId));

		student.setSchool(null);
		school.decrementStudentCount();
		
		// Remove the student from all associated routes
		for(Routing route : routingRepo.findAllByPickupStudents(student))  {
			route.setTotalStudents(route.getTotalStudents() - 1);
			route.getPickupStudents().remove(student);
			route.getDropoffStudents().remove(student);
		}
		
		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Deleted student " + student.getFirstName() +" "+ student.getLastName() + " from School: " + school.getSchoolName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		studentRepository.delete(student);

		return "redirect:/student";
	}
	
	/* -----------------------------------------------------------------------------------
	 * Depot options
	 -----------------------------------------------------------------------------------*/
	
	//depot options display page
	@RequestMapping({"/depot"})
	public String depotOptions(Model model,@AuthenticationPrincipal CustomUserDetails user) {
		District district = user.getDistrict(); // get the district of the logged-in user
		List<Depot> depots = depotRepository.findByDistrict(district); // filter the schools by district
		model.addAttribute("depots", depots);
		return "depot";
	}
	 
	//prints form for creation of depot
	@RequestMapping({"/createDepot"})
	public String showCreateDepotForm(Depot depot) {
		return "create-depot";
	}
	
	@RequestMapping({"/uploadDepot"}) // uploads the csv file and returns the create form with a result message // 
	public String uploadDepot(Depot depot, Model model, @AuthenticationPrincipal CustomUserDetails user, @RequestParam("file") MultipartFile file) {
		String message = "";
		DataLog logging = new DataLog();
		
		// Check if CSV file and then which service to call based on user input
		if (CsvHelper.hasCSVFormat(file)) {
			try {
				csvService.saveDepots(file, user.getDistrict());
				
		        message = "Loaded the file successfully: " + file.getOriginalFilename();
		        model.addAttribute("message", message);
		        
		        logging.setUser(user.getUsername());
				logging.setInformation("Loaded bus depot data with file: " + file.getOriginalFilename());
				logging.setLoggedDate();
				loggingRepo.save(logging);
				logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
				depot.setDistrictId(user.getDistrict());
		    } catch (Exception e) {
		        message = "Could not load the file: " + file.getOriginalFilename() + ". " + e.getMessage();
		        model.addAttribute("message", message);
		    }
		 }
		else {
			message = "Please upload a csv file!";
			model.addAttribute("message", message);
		}
		
		return "create-depot";
	}
	
	//adding a depot to the current list
	@RequestMapping({"/addDepot"})
	public String addDepot(@AuthenticationPrincipal CustomUserDetails user, @Validated Depot depot, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "create-depot";
		}
		DataLog logging = new DataLog();
		
		depot.setDistrictId(user.getDistrict());

		//Logged the added depot
		logging.setUser(user.getUsername());
		logging.setInformation("Added Depot: " + depot.getName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		depotRepository.save(depot);
		loggingRepo.save(logging);
		
		return "redirect:/depot";
	}

	//edits depot information
	@GetMapping("/viewDepot/{id}")
	public String viewDepotForm(@PathVariable("id") int id, Model model) {
		Depot depot = depotRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Depot Id:" + id));

		model.addAttribute("depot", depot);
		return "view-depot";
	}
	
	//edits depot information
	@GetMapping("/editDepot/{id}")
	public String showUpdateDepotForm(@PathVariable("id") int id, Model model) {
		Depot depot = depotRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Depot Id:" + id));

		model.addAttribute("depot", depot);
		return "update-depot";
	}
	
	//updates the depot information
	@PostMapping("/updateDepot/{id}")
	public String updateDepot(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") int id, @Validated Depot depot, BindingResult result, Model model) {
		if (result.hasErrors()) {
			depot.setDepotId(id);
			
			return "update-depot";
		}
		depot.setDistrictId(user.getDistrict());	
		depot.setDepotId(id);
		
		//Logged the edit depot
		DataLog logging = new DataLog();
		logging.setUser(user.getUsername());
		logging.setInformation("Edit Depot: " + depot.getName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		depotRepository.save(depot);
		
		return "redirect:/depot";
	}
	
	//Mapping for the /delete/id URL to delete a user     
	@GetMapping("/deleteDepot/{id}")
	public String deleteDepot(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") int id, Model model) {
		DataLog logging = new DataLog();
		Depot depot = depotRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Removed Depot: " + depot.getName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");

		loggingRepo.save(logging);
		depotRepository.delete(depot);
		
		return "redirect:/depot";
	}

	/* -----------------------------------------------------------------------------------
	 * Vehicle options
	 -----------------------------------------------------------------------------------*/
	
	//vehicle options diplay page
	@RequestMapping({"/vehicle"})
	public String vehicleOptions(Model model,@AuthenticationPrincipal CustomUserDetails user) {
		District district = user.getDistrict(); // get the district of the logged-in user
		List<Vehicle> vehicle = vehRepo.findByDistrict(district); // filter the schools by district
		
		model.addAttribute("vehicles", vehicle);
		return "vehicles";
	}
	
	// creates a vehicle // 
	@RequestMapping({"/createVehicle"})
	public String showCreateVehicleForm(Vehicle vehicle, Depot depot, Model model) {		
		model.addAttribute("depots", depotRepository.findAll());
		
		return "create-vehicle"; // Gets the vehicle information from user // 
	}
	
	@RequestMapping({"/uploadVehicle"}) // uploads the csv file and returns the create form with a result message // 
	public String uploadVehicle(Vehicle vehicle, Depot depot, Model model, @AuthenticationPrincipal CustomUserDetails user, @RequestParam("file") MultipartFile file) {
		String message = "";
		DataLog logging = new DataLog();
		
		// Check if CSV file and then which service to call based on user input
		if (CsvHelper.hasCSVFormat(file)) {
			try {
				csvService.saveVehicles(file, user.getDistrict());
				
		        message = "Loaded the file successfully: " + file.getOriginalFilename();
		        model.addAttribute("message", message);
		        
		        logging.setUser(user.getUsername());
				logging.setInformation("Loaded vehicle data with file: " + file.getOriginalFilename());
				logging.setLoggedDate();
				loggingRepo.save(logging);
				logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		    } catch (Exception e) {
		        message = "Could not load the file: " + file.getOriginalFilename() + ". " + e.getMessage();
		        model.addAttribute("message", message);
		    }
		 }
		else {
			message = "Please upload a csv file!";
			model.addAttribute("message", message);
		}
		
		return "create-vehicle";
	}

	@RequestMapping({"/addVehicle"}) // adds a vehicle // 
	public String addVehicle(@AuthenticationPrincipal CustomUserDetails user, @Validated Vehicle vehicle, BindingResult result, Model model, 
			@RequestParam(value="depot",required=false) Long depotId) {
		if (result.hasErrors()) {
			return "create-vehicle"; // sends the user back to the create vehicle page // 
		}
		DataLog logging = new DataLog();
		
		vehicle.setDistrict(user.getDistrict());
		
		//Logged the added Vehicle
		logging.setUser(user.getUsername());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		if(depotId != null) {
			Depot depot = depotRepository.findById(depotId.intValue())
					.orElseThrow(() -> new IllegalArgumentException("Invalid Depot Id:" + depotId));
			
			vehicle.setDepot(depot);
						
			logging.setInformation("Added Vehicle: " + vehicle.getVehicleCode() + " To Depot: " + depot.getName());
		} else {
			logging.setInformation("Added Vehicle: " + vehicle.getVehicleCode());
		}
		
		loggingRepo.save(logging);
		vehRepo.save(vehicle);
		
		return "redirect:/vehicle"; // once the depot has been set, redirects to vehicle // 
	}

	@GetMapping("/viewVehicle/{id}") // views all the vehicle information
	public String viewVehicleForm(@PathVariable("id") long id, Depot depotO, Model model) {
		Vehicle vehicle = vehRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		
		model.addAttribute("vehicle", vehicle);
		
		return "view-vehicle"; // returns the updated vehicle // 
	}
	
	@GetMapping("/editVehicle/{id}") // edits the vehicle ID // 
	public String showUpdateVehicleForm(@PathVariable("id") long id, Model model) {
		Vehicle vehicle = vehRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + id));
		
		model.addAttribute("depots", depotRepository.findAll());
		model.addAttribute("vehicle", vehicle);
		
		return "update-vehicle"; // returns the updated vehicle // 
	}

	@PostMapping("/updateVehicle/{id}") // updates the vehicle ID // 
	public String updateVehicle(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, @Validated Vehicle vehicle, 
			BindingResult result, Model model, @RequestParam(value="depot",required=false) Long depotId) {
		if (result.hasErrors()) {
			return "redirect:/editVehicle/" + id; // Return to the edit form
		}
		
		DataLog logging = new DataLog();

		// Ensure that the vehicle being edited exists
		if(vehRepo.findById(id) != null) {
			vehicle.setId(id);
		} else {
			throw new IllegalArgumentException("Invalid vehicle Id:" + id);
		}
		
		vehicle.setDistrict(user.getDistrict()); // reset the district - workaround to prevent errors
		
		// Check if user entered a depot to link
		if(depotId != null) {
			Depot depot = depotRepository.findById(depotId.intValue())
					.orElseThrow(() -> new IllegalArgumentException("Invalid Depot Id:" + depotId));
			
			vehicle.setDepot(depot);
			//depot.setVehicle(depot.getVehicle().add(vehicle));
		}
		
		//Logged the edit vehicle
		logging.setInformation("Edited Vehicle: " + vehicle.getId());
		logging.setUser(user.getUsername());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
				
		vehRepo.save(vehicle);
		loggingRepo.save(logging);
		
		return "redirect:/vehicle"; // redirects the user to the vehicle page // 
	}
	
	//Mapping for the /delete/id URL to delete a Vehicle and it's driver    
	@GetMapping("/deleteVehicle/{id}")
	public String deleteVehicle(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, Model model) {
		DataLog logging = new DataLog();
		Vehicle vehicle = vehRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));

		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Removed Vehicle: " + vehicle.getId() + " From Depot: " /*+ depot.getName()*/);
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		// Remove the driver from all routes
		for (Routing route : routingRepo.findAllByVehicle(vehicle)) { 
		    route.setVehicle(null);
		}
		
		loggingRepo.save(logging);
		vehRepo.delete(vehicle);

		return "redirect:/vehicle";
	}
	
	/* -----------------------------------------------------------------------------------
	 * Driver options
	 -----------------------------------------------------------------------------------*/
	
	//driver options display page
	@RequestMapping({"/driver"})
	public String driverOptions(Model model, @AuthenticationPrincipal CustomUserDetails user) {
		District district = user.getDistrict(); 
		List<Drivers> driver = driverRepo.findByDistrict(district); // filter the schools by district
		model.addAttribute("drivers", driver);
		return "driver";
	}
	
	@RequestMapping({"/createDriver"})
	public String showCreateDriverForm(Drivers driver, Model model) {
		return "create-driver";
	}
	
	@RequestMapping({"/uploadDriver"}) // uploads the csv file and returns the create form with a result message // 
	public String uploadDriver(Drivers driver, Model model, @AuthenticationPrincipal CustomUserDetails user, @RequestParam("file") MultipartFile file) {
		String message = "";
		DataLog logging = new DataLog();
		
		// Check if CSV file and then which service to call based on user input
		if (CsvHelper.hasCSVFormat(file)) {
			try {
				csvService.saveDrivers(file, user.getDistrict());
				
				
		        message = "Loaded the file successfully: " + file.getOriginalFilename();
		        model.addAttribute("message", message);
		        
		        logging.setUser(user.getUsername());
				logging.setInformation("Loaded driver data with file: " + file.getOriginalFilename());
				logging.setLoggedDate();
				loggingRepo.save(logging);
				logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		    } catch (Exception e) {
		        message = "Could not load the file: " + file.getOriginalFilename() + ". " + e.getMessage();
		        model.addAttribute("message", message);
		    }
		 }
		else {
			message = "Please upload a csv file!";
			model.addAttribute("message", message);
		}
		
		return "create-driver";
	}

	@RequestMapping({"/addDriver"})
	public String addDriver(@AuthenticationPrincipal CustomUserDetails user, @Validated Drivers driver, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "create-driver";
		}
		
		DataLog logging = new DataLog();

		driver.setDistrictId(user.getDistrict());
		
		//Logged the added Driver
		logging.setUser(user.getUsername());
		logging.setInformation("Added Driver: " + driver.getName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		driverRepo.save(driver);
		
		return "redirect:/driver";
	}
	
	// views the drivers information // 
	@GetMapping("/viewDriver/{id}")
	public String viewDriverForm(@PathVariable("id") long id, Model model) {
		Drivers driver = driverRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));

		model.addAttribute("driver", driver);
		return "view-driver";
	}
	
	// edits the drivers information // 
	@GetMapping("/editDriver/{id}")
	public String showUpdateDriverForm(@PathVariable("id") long id, Model model) {
		Drivers driver = driverRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));
		model.addAttribute("vehicle", vehRepo.findAll());
		model.addAttribute("driver", driver);
		return "update-driver";
	}
	
	//updates the drivers information // 
	@PostMapping("/updateDriver/{dId}")
	public String updateDriver(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("dId") long dId, 
			@Validated Drivers driver, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			driver.setId(dId);
			return "redirect:/edit-driver/"+dId; //the driver information is updated // 
		}
		
		DataLog logging = new DataLog();

		driver.setId(dId);
		driver.setDistrictId(user.getDistrict()); // reset the district - workaround to prevent errors

		//Logged the edit driver
		logging.setUser(user.getUsername());
		logging.setInformation("Edit driver: " + driver.getName());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		driverRepo.save(driver);
		
		return "redirect:/driver";
	}
	
	//Mapping for the /delete/id URL to delete a user     
	@GetMapping("/deleteDriver/{id}")
	public String deleteDriver(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, Model model) {
		DataLog logging = new DataLog();
		Drivers driver = driverRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
		
		if (driver.getVehicle() != null) {
			long vehicleID = driver.getVehicle().getId();
			Vehicle vehicle = vehRepo.findById(vehicleID)
					.orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
			
			vehicle.setDriver(null);
			
			logging.setInformation("Removed Driver: " + driver.getName() + " From Vehicle: " + vehicle.getId());
		} else {
			logging.setInformation("Removed Driver: " + driver.getName());
		}
		
		// Remove the student from all associated routes
		for(Routing route : routingRepo.findAllByDriver(driver))  {
			route.setDriver(null);
		}

		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");

		loggingRepo.save(logging);
		driverRepo.delete(driver);
		
		return "redirect:/driver";
	}
}

