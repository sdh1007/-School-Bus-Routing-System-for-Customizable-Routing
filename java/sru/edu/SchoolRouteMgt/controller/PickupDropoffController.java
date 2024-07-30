/*
 * Group1 - School Routing
 * Authors: Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Pickup/Dropoff Controller
 * Controls a majority of the html pages and actions, as well as pushing data to the various databases when users
 * add new objects. Also handles deleting, and editing of these objects with respect to their databases. Routing and pickups where
 * separate because their testing required a bit more work so they where separated from the main controllers.
 *
 * 
 */
package sru.edu.SchoolRouteMgt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sru.edu.SchoolRouteMgt.datawriter.WriteToLog;
import sru.edu.SchoolRouteMgt.domain.CustomUserDetails;
import sru.edu.SchoolRouteMgt.domain.DataLog;
import sru.edu.SchoolRouteMgt.domain.LocationPoint;
import sru.edu.SchoolRouteMgt.domain.PickupDropoff;
import sru.edu.SchoolRouteMgt.domain.RouteGroups;
import sru.edu.SchoolRouteMgt.domain.Routing;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.repository.DataLogRepository;
import sru.edu.SchoolRouteMgt.repository.PickupDropoffRepository;
import sru.edu.SchoolRouteMgt.repository.RouteGroupsRepository;
import sru.edu.SchoolRouteMgt.repository.RoutingRepository;
import sru.edu.SchoolRouteMgt.repository.SchoolRepository;
import sru.edu.SchoolRouteMgt.repository.StudentRepository;

@Controller
public class PickupDropoffController {

	//repositories for pickupDropoff table and school table
	private RouteGroupsRepository groupRepo;
	private PickupDropoffRepository pdRepo;
	private SchoolRepository schoolRepo;
	private StudentRepository studentRepo;
	private RoutingRepository routeRepo;
	private DataLogRepository loggingRepo;
	
	WriteToLog logWriter = new WriteToLog();

	//create an UserRepository instance - instantiation (new) is done by Spring
	public PickupDropoffController(RouteGroupsRepository groupRepo, PickupDropoffRepository pdRepo, SchoolRepository schoolRepo, 
			StudentRepository studentRepo, RoutingRepository routeRepo, DataLogRepository loggingRepo) {
		this.groupRepo = groupRepo;
		this.pdRepo = pdRepo;
		this.schoolRepo = schoolRepo;
		this.studentRepo = studentRepo;
		this.routeRepo = routeRepo;
		this.loggingRepo = loggingRepo;
	}
	
	//returns the page to view the points on the mapS
	@GetMapping("/pickupDropoffs")
	public String listPickupDropoffs(Model model) {
		model.addAttribute("pickupDropoffs", pdRepo.findAll()); // Make it so it only gets the ones associated with districts
		
		return "pickupDropoffs";
	}
	
	// Mapping to enter a pickup/dropoff point
	@RequestMapping({"/createPickupDropoff"})
	public String createPickupDropoff(PickupDropoff pickupDropoff, Model model) {		
		model.addAttribute("schools", schoolRepo.findAllByOrderBySchoolNameAsc());
		model.addAttribute("students", studentRepo.findAll());
		
		return "create-pickupDropoff";
	}
	
	// Mapping to enter a pickup/dropoff for a specific student
	@RequestMapping({"/createSpecificPickupDropoffForm/{studId}/{groupId}/{routeId}"})
	public String createSpecificPickupDropoffForm(@PathVariable("studId") int studentId, @PathVariable("groupId") long groupId,  @PathVariable("routeId") long routeId, 
			PickupDropoff pickupDropoff, Model model) {		
		RouteGroups group = groupRepo.findById(groupId) // Get group to find the schools that are selected on the route from the group
				.orElseThrow(() -> new IllegalArgumentException("Invalid Group Id:" + groupId));
		Students student = studentRepo.findById(studentId) // Find the student that was selected so they can be preselected on the page for the user
				.orElseThrow(() -> new IllegalArgumentException("Invalid Student Id:" + studentId));
		
		model.addAttribute("pickupDropoffs", pdRepo.findAll());
		model.addAttribute("routeId", routeId); // Add recently added/edited route to return to after adding the pickup/dropoff point
		model.addAttribute("group", group);
		model.addAttribute("student", student);
		
		return "create-specificPickupDropoff";
	}
	
	// Mapping to save a pickup/dropoff point and a specific pickup/dropoff point
	@RequestMapping(value = {"/addPickupDropoff", "/addPickupDropoff/{routeId}"})
	public String addPickupDropoff(@AuthenticationPrincipal CustomUserDetails user, @Validated PickupDropoff pickupDropoff, 
			BindingResult result, Model model ,
			@PathVariable(name = "routeId", required = false) Long routeId, 
			@RequestParam(name = "checkboxStudents", required = false) Set<Students> formStudents, 
			@RequestParam("days") String day, @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, 
			@RequestParam("roadName") String roadName) {
		if (result.hasErrors()) {
			return "error"; // Change this to output why
		}
		DataLog logging = new DataLog();
		LocationPoint pickupDropoffLocation = new LocationPoint();
		
		pickupDropoffLocation.setLatitude(Float.valueOf(latitude));
		pickupDropoffLocation.setLongitude(Float.valueOf(longitude));
		pickupDropoffLocation.setRoadName(roadName);
						
		switch(day) {
			case "all":
				pickupDropoff.setSamePoints(pickupDropoffLocation);
				break;
			case "monday":
				pickupDropoff.setMonday(pickupDropoffLocation);
				break;
			case "tuesday":
				pickupDropoff.setTuesday(pickupDropoffLocation);
				break;
			case "wednesday":
				pickupDropoff.setWednesday(pickupDropoffLocation);
				break;
			case "thursday":
				pickupDropoff.setThursday(pickupDropoffLocation);
				break;
			case "friday":
				pickupDropoff.setFriday(pickupDropoffLocation);
				break;
		}
		
		if(formStudents != null) {
			pickupDropoff.setStudents(formStudents);
			for (Students student : formStudents) { 
				int id = student.getStudentId();
				
				student = studentRepo.findById(id)
						.orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
				
				// Find all routes the student is already assigned to and increment their stop
				for(Routing route : routeRepo.findAllByPickupStudents(student)) {
					route.setTotalStops(route.getTotalStops() + 1);
				}
				
				student.setPickupDropoff(pickupDropoff);
			}
		} else {
			pickupDropoff.setStudents(null);
		}

		//Logged the added pickup/dropoff
		logging.setUser(user.getUsername());
		logging.setInformation("Added Pickup/Dropoff Point.");
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		pdRepo.save(pickupDropoff); // Saves the pickupDropoff & updates each student's pickupDropoff
		
		if (routeId != null) {
			return "redirect:/editRoute/"+routeId;
		}
		return "redirect:/pickupDropoffs";
	}

	// Mapping to view a pickup/dropoff point
	@GetMapping("/viewPickupDropoff/{id}")
	public String viewPickupDropoff(@PathVariable("id") long id, Model model) {		
		PickupDropoff pickupDropoff = pdRepo.findById(id);
		
		model.addAttribute("pickupDropoff", pickupDropoff);
		model.addAttribute("day", pickupDropoff.getDay());
		
		return "view-pickupDropoff";
	}
	
	// Mapping to edit a pickup/dropoff point
	@GetMapping("/editPickupDropoff/{id}")
	public String editPickupDropoff(@PathVariable("id") long id, Model model) {
		PickupDropoff pickupDropoff = pdRepo.findById(id);
		
		model.addAttribute("schools", schoolRepo.findAllByOrderBySchoolNameAsc());
		model.addAttribute("students", studentRepo.findAll());
		model.addAttribute("pickupDropoff", pickupDropoff);
		model.addAttribute("day", pickupDropoff.getDay());
		
		return "update-pickupDropoff";
	}
	
	// Mapping to save a pickup/dropoff point
	@RequestMapping({"/updatePickupDropoff"})
	public String updatePickupDropoff(@AuthenticationPrincipal CustomUserDetails user, @Validated PickupDropoff pickupDropoff, Model model, BindingResult result,
			@RequestParam("id") long id, @RequestParam("days") String day, @RequestParam(name = "checkboxStudents", required = false) Set<Students> formStudents, 
			@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude, @RequestParam("roadName") String roadName) {
		if (result.hasErrors()) {
			return "error"; // Change this to output why
		}
		
		DataLog logging = new DataLog();
		pickupDropoff = pdRepo.findById(id);
		LocationPoint pickupDropoffLocation = new LocationPoint();
		
		// Find current location based on day
		switch(pickupDropoff.getDay()) {
			case "All Days":
				pickupDropoffLocation = pickupDropoff.getMonPickupPointLocation();
				break;
			case "Monday":
				pickupDropoffLocation = pickupDropoff.getMonPickupPointLocation();
				break;
			case "tuesday":
				pickupDropoffLocation = pickupDropoff.getTuePickupPointLocation();
				break;
			case "wednesday":
				pickupDropoffLocation = pickupDropoff.getWedPickupPointLocation();
				break;
			case "thursday":
				pickupDropoffLocation = pickupDropoff.getThurPickupPointLocation();
				break;
			case "friday":
				pickupDropoffLocation = pickupDropoff.getFriPickupPointLocation();
				break;
		}
		
		// Set updated location point info
		pickupDropoffLocation.setLatitude(Float.valueOf(latitude));
		pickupDropoffLocation.setLongitude(Float.valueOf(longitude));
		pickupDropoffLocation.setRoadName(roadName);
		
		// Set new point based on new day
		switch(day) {
			case "all":
				pickupDropoff.setSamePoints(pickupDropoffLocation);
				break;
			case "monday":
				pickupDropoff.setMonday(pickupDropoffLocation);
				break;
			case "tuesday":
				pickupDropoff.setTuesday(pickupDropoffLocation);
				break;
			case "wednesday":
				pickupDropoff.setWednesday(pickupDropoffLocation);
				break;
			case "thursday":
				pickupDropoff.setThursday(pickupDropoffLocation);
				break;
			case "friday":
				pickupDropoff.setFriday(pickupDropoffLocation);
				break;
		}
				
		if(formStudents != null) {
			pickupDropoff.setStudents(formStudents);
			for (Students student : formStudents) { 
				int studId = student.getStudentId();
				
				student = studentRepo.findById(studId)
						.orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
				
				student.setPickupDropoff(pickupDropoff);
			}
		} else {
			pickupDropoff.setStudents(null);
		}
				
		//Logged the added pickup/dropoff
		logging.setUser(user.getUsername());
		logging.setInformation("Updated Pickup/Dropoff Point." + "Id: " + pickupDropoff.getPickupDropoffId());
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");
		
		loggingRepo.save(logging);
		pdRepo.save(pickupDropoff); // Saves the pickupDropoff & updates each student's pickupDropoff
		
		return "redirect:/pickupDropoffs";
	}
	
	// Mapping to delete a pickup/dropoff point
	@GetMapping("/deletePickupDropoff/{id}")
	public String deletePickupDropoff(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("id") long id, Model model) {
		DataLog logging = new DataLog();
		PickupDropoff pickupDropoff = pdRepo.findById(id);

		// Sets every student's pickup/dropoff to null to prevent deleting the student when deleting the pickup/dropoff
		for (Students student : pickupDropoff.getStudents()) {	
			// Decrement all routes' totalStop count if there are any students with this pickup/dropoff point on the route
			for(Routing route : routeRepo.findAllByPickupStudents(student)) {				
				route.setTotalStops(route.getTotalStops() - 1);
			}
			
			student.setPickupDropoff(null);
		}
		
		//Logging deleted item
		logging.setUser(user.getUsername());
		logging.setInformation("Removed Pickup/Dropoff Point");
		logging.setLoggedDate();
		logWriter.setText(logging.getInformation() + " " + logging.getloggedDate() +" " + logging.getUser() + "\n");

		loggingRepo.save(logging);
		pdRepo.delete(pickupDropoff);
		
		return "redirect:/pickupDropoffs";
	}
}
