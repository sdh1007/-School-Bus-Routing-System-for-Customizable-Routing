/*
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Routing Controller
 * Controls a majority of the html pages and actions, as well as pushing data to the various databases when users
 * add new objects. Also handles deleting, and editing of these objects with respect to their databases. Routing and pickups where
 * separate because their testing required a bit more work so they where separated from the main controllers.
 * 
 * 
 */
package sru.edu.SchoolRouteMgt.controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

import sru.edu.SchoolRouteMgt.datawriter.WriteToLog;
import sru.edu.SchoolRouteMgt.domain.CustomUserDetails;
import sru.edu.SchoolRouteMgt.domain.DataLog;
import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.Drivers;
import sru.edu.SchoolRouteMgt.domain.LocationPoint;
import sru.edu.SchoolRouteMgt.domain.PickupDropoff;
import sru.edu.SchoolRouteMgt.domain.PickupDropoff;
import sru.edu.SchoolRouteMgt.domain.Routing;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.domain.Vehicle;
import sru.edu.SchoolRouteMgt.repository.DataLogRepository;
import sru.edu.SchoolRouteMgt.repository.DepotRepository;
import sru.edu.SchoolRouteMgt.repository.DriverRepository;
import sru.edu.SchoolRouteMgt.repository.LocationPointRepository;
import sru.edu.SchoolRouteMgt.repository.PickupDropoffRepository;
import sru.edu.SchoolRouteMgt.repository.PickupDropoffRepository;
import sru.edu.SchoolRouteMgt.repository.RouteGroupsRepository;
import sru.edu.SchoolRouteMgt.repository.RoutingRepository;
import sru.edu.SchoolRouteMgt.repository.SchoolRepository;
import sru.edu.SchoolRouteMgt.repository.StudentRepository;
import sru.edu.SchoolRouteMgt.repository.VehicleRepository;

@Controller
public class RoutingController {
	//repositories initialized
	private PickupDropoffRepository pdRepo;
	private SchoolRepository schoolRepo;
	private StudentRepository studentRepo;
	private DepotRepository depotRepo;
	private VehicleRepository vehRepo;
	private DriverRepository driverRepo;
	private LocationPointRepository locRepo;
	private RouteGroupsRepository groupRepo;
	private RoutingRepository routeRepo;
	private DataLogRepository loggingRepo;
	JFrame frame = new JFrame();
	WriteToLog logWriter = new WriteToLog();

	//create an UserRepository instance - instantiation (new) is done by Spring
	public RoutingController(DataLogRepository loggingRepo, PickupDropoffRepository pdRepo,
			SchoolRepository schoolRepo, StudentRepository studentRepo, VehicleRepository vehRepo, 
			DriverRepository driverRepo, LocationPointRepository locRepo, DepotRepository depotRepo,
			RoutingRepository routeRepo, RouteGroupsRepository groupRepo) {
		this.pdRepo = pdRepo;
		this.schoolRepo = schoolRepo;
		this.studentRepo = studentRepo;
		this.vehRepo = vehRepo;
		this.driverRepo = driverRepo;
		this.locRepo = locRepo;
		this.depotRepo  = depotRepo;
		this.routeRepo = routeRepo;
		this.groupRepo = groupRepo;
		this.loggingRepo = loggingRepo;
	}
	
	//lets the user know they still have to add information
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
		String name = ex.getParameterName();
		System.out.println(name + " parameter is missing");
		JOptionPane.showMessageDialog(frame, "Please select an option from the checkboxes", "Checkbox Error", JOptionPane.WARNING_MESSAGE);
		String referer = request.getHeader("Referer");
		return "redirect:"+ referer;
	}
	
	private LocationPoint determineLocation(Long id, String locType) {
		
		// TODO ADD CHECK IF LOCATION ALREADY EXISTS
		LocationPoint location = new LocationPoint();
		
		switch(locType) {
		case "school":
			Schools school = schoolRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid School Id:" + id));
			
			location.setAddressInfo(school.getAddress(), "", school.getCity(), school.getState(), Integer.toString(school.getZip()));
			// location = school.getLocation(); // TODO IMPLEMENT GETTING LOCATION DATA WHEN CREATING THE SCHOOL
			
			break;
		case "depot":
			Depot depot = depotRepo.findById(id.intValue())
				.orElseThrow(() -> new IllegalArgumentException("Invalid Depot Id:" + id));
			
			location.setAddressInfo(depot.getAddress1(), depot.getAddress2(), depot.getCity(), depot.getState(), depot.getZipCode());
			// location = depot.getLocation(); // TODO IMPLEMENT GETTING LOCATION DATA WHEN CREATING THE DEPOT
			
			break;
		case "driver":
			Drivers driver = driverRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Driver Id:" + id));
			
			location.setAddressInfo(driver.getAddress1(), driver.getAddress2(), driver.getCity(), driver.getState(), Long.toString(driver.getZipCode()));
			// location = driver.getLocation(); // TODO IMPLEMENT GETTING LOCATION DATA WHEN CREATING THE DRIVER
			
			break;
		}
		
		if(location.getAddress1() == null) {
			throw new RuntimeException("Unable to find address for starting/ending location.");
		}
		
		locRepo.save(location);
		
		return location;
	}
	
	private Routing saveRouteInfo(Routing route, Long vid, Long startLocId, String startLocType, Long endLocId, String endLocType, 
			List<Students> pickupStudents, List<Students> dropoffStudents) {
		if(vid != null) {
			Vehicle vehicle = vehRepo.findById(vid)
					.orElseThrow(() -> new IllegalArgumentException("Invalid Vehicle Id:" + vid));
			
			route.setMaxStudentCapacity(vehicle.getSeatNumber());
		}
		
		if(startLocId != null) {
			route.setStartingLocation(determineLocation(startLocId, startLocType));
		}
		if(endLocId != null) {
			route.setEndingLocation(determineLocation(endLocId, endLocType));
		}
						
		route.setPickupStudents(pickupStudents);
		route.setDropoffStudents(dropoffStudents);
		route.setActive(true);
		
		return route;
	}
	
	//find all routing information and display
	@RequestMapping({"/routing"})
	public String routingOptions(Model model) {
		model.addAttribute("routes", routeRepo.findAll());
		
		return "routing";
	}
	
	@RequestMapping({"/createRoute"})
	public String showCreateRouteForm(Routing route, Model model) {
		
		model.addAttribute("vehicles", vehRepo.findAll());
		model.addAttribute("drivers", driverRepo.findAll());
		model.addAttribute("students", studentRepo.findAll());
		model.addAttribute("groups", groupRepo.findAll());
		model.addAttribute("depots", depotRepo.findAll());
		
		return "create-route";
	}
	
	//adds route to the database
	@RequestMapping({"/addRoute"})
	public String addRoute(@AuthenticationPrincipal CustomUserDetails user, @Validated Routing route, BindingResult result, Model model, 
			@RequestParam(value="add", required=false) String addFlag, 
			@RequestParam(value="save", required=false) String saveFlag,
			@RequestParam(value="vehicle", required=false) Long vid, 
			@RequestParam(value="startingLocations", required=false) Long startLocId, 
			@RequestParam(value="startingLocationType", required=false) String startLocType,
			@RequestParam(value="endingLocations", required=false) Long endLocId, 
			@RequestParam(value="endingLocationType", required=false) String endLocType,
			@RequestParam(value="studentPickups", required=false) List<Students> pickupStudents, 
			@RequestParam(value="studentDropoffs", required=false) List<Students> dropoffStudents) {
		DataLog logging = new DataLog();
		
		route = saveRouteInfo(route, vid, startLocId, startLocType, endLocId, endLocType, pickupStudents, dropoffStudents);
		route.setDistrict(user.getDistrict());
		
		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Added route.");
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		routeRepo.save(route);
		
		if(saveFlag != null) {
			return "redirect:/editRoute/"+route.getId();
		} else {
			return "redirect:/routing";
		}
	}
	
	//edit route from database
	@GetMapping("/editRoute/{id}")
	public String editRoute(@PathVariable("id") long id, Model model) {
		Routing route = routeRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid route Id:" + id));
		
		model.addAttribute("vehicles", vehRepo.findAll());
		model.addAttribute("drivers", driverRepo.findAll());
		model.addAttribute("students", studentRepo.findAll());
		model.addAttribute("groups", groupRepo.findAll());
		model.addAttribute("depots", depotRepo.findAll());
		model.addAttribute("routing", route);

		return "update-route";
	}
	
	// updates route to the database
	@RequestMapping({"/updateRoute/{id}"})
	public String updateRoute(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, @Validated Routing route, BindingResult result, Model model, 
			@RequestParam(value="vehicle",required=false) Long vid, 
			@RequestParam(value="startingLocations", required=false) Long startLocId, 
			@RequestParam(value="startingLocationType", required=false) String startLocType,
			@RequestParam(value="endingLocations", required=false) Long endLocId, 
			@RequestParam(value="endingLocationType", required=false) String endLocType,
			@RequestParam(value="studentPickups", required=false) List<Students> pickupStudents, 
			@RequestParam(value="studentDropoffs", required=false) List<Students> dropoffStudents) {
		DataLog logging = new DataLog();
		
		route.setId(id);
		
		route = saveRouteInfo(route, vid, startLocId, startLocType, endLocId, endLocType, pickupStudents, dropoffStudents);
		route.setDistrict(user.getDistrict());
		
		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Updated route: " + route.getId());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		routeRepo.save(route);
		
		return "redirect:/routing";
	}
	
	//views routing information from database
	@RequestMapping({"/viewRoute/{id}"})
	public String viewRoutes(@PathVariable("id") long id, Model model) {
		Routing route = routeRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid route Id:" + id));
		
		model.addAttribute("vehicles", vehRepo.findAll());
		model.addAttribute("drivers", driverRepo.findAll());
		model.addAttribute("students", studentRepo.findAll());
		model.addAttribute("groups", groupRepo.findAll());
		model.addAttribute("depots", depotRepo.findAll());
		model.addAttribute("routing", route);
		
		return "view-route";
	}

	//delete route from database
	@GetMapping("/deleteRoute/{id}")
	public String deleteRoute(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, Model model) {
		DataLog logging = new DataLog();
		Routing route = routeRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid route Id:" + id));

		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Removed route: " + route.getId());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		routeRepo.delete(route);

		return "redirect:/routing";
	}
	
	// Saves or updates a route and sends user to createSpecificPickupDropoff
	@RequestMapping(value = {"/createSpecificPickupDropoff/{studId}/{groupId}", "/createSpecificPickupDropoff/{studId}/{groupId}/{routeId}"})
	public String createSpecificPickupDropoff(@AuthenticationPrincipal CustomUserDetails user, 
			@Validated Routing route, BindingResult result, Model model, 
			@PathVariable(name = "studId", required = true) Long studId, 
			@PathVariable(name = "groupId", required = true) Long groupId, 
			@PathVariable(name = "routeId", required = false) Long routeId, 
			@RequestParam(value="vehicle", required=false) Long vid, 
			@RequestParam(value="startingLocations", required=false) Long startLocId, 
			@RequestParam(value="startingLocationType", required=false) String startLocType,
			@RequestParam(value="endingLocations", required=false) Long endLocId, 
			@RequestParam(value="endingLocationType", required=false) String endLocType,
			@RequestParam(value="studentPickups", required=false) List<Students> pickupStudents, 
			@RequestParam(value="studentDropoffs", required=false) List<Students> dropoffStudents) {
		DataLog logging = new DataLog();
		
		if (routeId != null) {
		   route.setId(routeId);
			
		   route = saveRouteInfo(route, vid, startLocId, startLocType, endLocId, endLocType, pickupStudents, dropoffStudents);
		   route.setDistrict(user.getDistrict());
		   
		   logging.setInformation("Updated route: " + route.getId());
	   } else {
		   route = saveRouteInfo(route, vid, startLocId, startLocType, endLocId, endLocType, pickupStudents, dropoffStudents);
		   route.setDistrict(user.getDistrict());
		   
		   logging.setInformation("Added route.");
	   }
	   
	   //Logging added/edited route
	   logging .setUser(user.getUsername());
	   logging.setLoggedDate();
	   logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
	
	   loggingRepo.save(logging);
	   routeRepo.save(route);
	   
	   return "redirect:/createSpecificPickupDropoffForm/" + studId + "/" + groupId + "/" + route.getId();
	}
}


