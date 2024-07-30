package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
/* 
 * The Routing class focuses on the pickup/dropoff points for schools, along with vehicles 
 * being assigned to drivers and the routes that they take to get to the school, all the points
 * are placed on the GoogleMaps route
   */ 
@Entity
public class Routing {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NonNull private String routeName;
	@Nullable private int startHour; 
	@Nullable private int startMin; 
	@Nullable private String startAmPm; 
	@Nullable private int endHour; 
	@Nullable private int endMin; 
	@Nullable private String endAmPm;
	@Nullable private int totalStudents;
	@Nullable private int totalStops;
	@Nullable private int maxStudentCapacity;
	@Nullable private String pickupTotalDistance;
	@Nullable private String pickupTotalTravelTime;
	@Nullable private String pickupTotalTravelTimeWithoutRiders;
	@Nullable private String dropoffTotalDistance;
	@Nullable private String dropoffTotalTravelTime;
	@Nullable private String dropoffTotalTravelTimeWithoutRiders;
	@Nullable private boolean isActive;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

	// mapping a set of students to one pickup/dropoff point
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Nullable
	private List<Students> pickupStudents = new ArrayList<>(); // Use ArrayList since it needs to be in order
	
	// mapping a set of students to one pickup/dropoff point
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Nullable
	private List<Students> dropoffStudents = new ArrayList<>(); // Use ArrayList since it needs to be in order
	
	// Might be useful to also store the direct list of pickup/dropoffs it doesn't need to be calculated using the student's list
	
	// Many to one relationship to assign a vehicle to many different routes
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Nullable
	private Vehicle vehicle;
	
	// Many to one relationship to assign a driver to many different routes
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Nullable
	private Drivers driver;
	
	// Many to one relationship to assign a group to many different routes
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@NonNull
	private RouteGroups group;

	// Many to one relationship to assign a startingLocation to many different routes
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Nullable
	private LocationPoint startingLocation;
	
	// Many to one relationship to assign an endingLocation to many different routes
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@Nullable
	private LocationPoint endingLocation;
	
	//insertData method takes data in from ToDatabase package to map data to MySQL columns
	public void insertData( 
			long id, String routeName, int startHour, int startMin, String startAmPm, int endHour, int endMin, String endAmPm, 
			int totalStudents, int totalStops, int maxStudentCapacity, String pickupTotalDistance, String pickupTotalTravelTime, String pickupTotalTravelTimeWithoutRiders,
			String dropoffTotalDistance, String dropoffTotalTravelTime, String dropoffTotalTravelTimeWithoutRiders, boolean isActive,
			List<Students> pickupStudents, List<Students> dropoffStudents, Vehicle vehicle, Drivers driver, 
			RouteGroups group, LocationPoint startingLocation, LocationPoint endingLocation) {
		
		this.id = id;
		this.routeName = routeName;
		this.startHour = startHour;
		this.startMin = startMin;
		this.startAmPm = startAmPm;
		this.startHour = endHour;
		this.startMin = endMin;
		this.startAmPm = endAmPm;
		this.totalStudents = totalStudents;
		this.totalStops = totalStops;
		this.maxStudentCapacity = maxStudentCapacity;
		this.pickupTotalDistance = pickupTotalDistance;
		this.pickupTotalTravelTime = pickupTotalTravelTime;
		this.pickupTotalTravelTimeWithoutRiders = pickupTotalTravelTimeWithoutRiders;
		this.pickupTotalDistance = dropoffTotalDistance;
		this.pickupTotalTravelTime = dropoffTotalTravelTime;
		this.pickupTotalTravelTimeWithoutRiders = dropoffTotalTravelTimeWithoutRiders;
		this.isActive = isActive;
		this.pickupStudents = pickupStudents;
		this.dropoffStudents = dropoffStudents;
		this.vehicle = vehicle;
		this.driver = driver;
		this.group = group;
		this.startingLocation = startingLocation;
		this.endingLocation = endingLocation;
	}

	//getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTotalStops() {
		return totalStops;
	}

	public void setTotalStops(int totalStops) {
		this.totalStops = totalStops;
	}
	
	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMin() {
		return startMin;
	}

	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}

	public String getStartAmPm() {
		return startAmPm;
	}

	public void setStartAmPm(String startAmPm) {
		this.startAmPm = startAmPm;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMin() {
		return endMin;
	}

	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}

	public String getEndAmPm() {
		return endAmPm;
	}

	public void setEndAmPm(String endAmPm) {
		this.endAmPm = endAmPm;
	}

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}

	public int getMaxStudentCapacity() {
		return maxStudentCapacity;
	}

	public void setMaxStudentCapacity(int maxStudentCapacity) {
		this.maxStudentCapacity = maxStudentCapacity;
	}

	public String getPickupTotalDistance() {
		return pickupTotalDistance;
	}

	public void setPickupTotalDistance(String pickupTotalDistance) {
		this.pickupTotalDistance = pickupTotalDistance;
	}

	public String getPickupTotalTravelTime() {
		return pickupTotalTravelTime;
	}

	public void setPickupTotalTravelTime(String pickupTotalTravelTime) {
		this.pickupTotalTravelTime = pickupTotalTravelTime;
	}

	public String getPickupTotalTravelTimeWithoutRiders() {
		return pickupTotalTravelTimeWithoutRiders;
	}

	public void setPickupTotalTravelTimeWithoutRiders(String pickupTotalTravelTimeWithoutRiders) {
		this.pickupTotalTravelTimeWithoutRiders = pickupTotalTravelTimeWithoutRiders;
	}

	public String getDropoffTotalDistance() {
		return dropoffTotalDistance;
	}

	public void setDropoffTotalDistance(String dropoffTotalDistance) {
		this.dropoffTotalDistance = dropoffTotalDistance;
	}

	public String getDropoffTotalTravelTime() {
		return dropoffTotalTravelTime;
	}

	public void setDropoffTotalTravelTime(String dropoffTotalTravelTime) {
		this.dropoffTotalTravelTime = dropoffTotalTravelTime;
	}

	public String getDropoffTotalTravelTimeWithoutRiders() {
		return dropoffTotalTravelTimeWithoutRiders;
	}

	public void setDropoffTotalTravelTimeWithoutRiders(String dropoffTotalTravelTimeWithoutRiders) {
		this.dropoffTotalTravelTimeWithoutRiders = dropoffTotalTravelTimeWithoutRiders;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public List<Students> getPickupStudents() {
		return pickupStudents;
	}

	public void setPickupStudents(List<Students> pickupStudents) {
		this.pickupStudents = pickupStudents;
	}
	
	public List<Students> getDropoffStudents() {
		return dropoffStudents;
	}

	public void setDropoffStudents(List<Students> dropoffStudents) {
		this.dropoffStudents = dropoffStudents;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Drivers getDriver() {
		return driver;
	}

	public void setDriver(Drivers driver) {
		this.driver = driver;
	}

	public RouteGroups getGroup() {
		return group;
	}

	public void setGroup(RouteGroups group) {
		this.group = group;
	}

	public LocationPoint getStartingLocation() {
		return startingLocation;
	}

	public void setStartingLocation(LocationPoint startingLocation) {
		this.startingLocation = startingLocation;
	}

	public LocationPoint getEndingLocation() {
		return endingLocation;
	}

	public void setEndingLocation(LocationPoint endingLocation) {
		this.endingLocation = endingLocation;
	}
}