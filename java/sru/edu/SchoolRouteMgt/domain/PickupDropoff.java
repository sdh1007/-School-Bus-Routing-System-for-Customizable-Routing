package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;

/* 
 * Class designed for Pickup/Dropoff locations. The Pickup/Dropoff points are where the students get picked 
 * up and dropped off before and after school, which can change depending on the day.
 *  */ 
@Entity
public class PickupDropoff {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long pickupDropoffId;
	
	private String pickupDropoffName;
	
	// mapping a set of students to one pickup/dropoff point
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "pickup_dropoff_id")
	@JsonIgnore
	private Set<Students> students = new HashSet<>();
	
	private boolean defaultPickupDropoff;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint monPickupPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint monDropoffPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint tuePickupPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint tueDropoffPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint wedPickupPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint wedDropoffPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint thurPickupPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint thurDropoffPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint friPickupPointLocation;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private LocationPoint friDropoffPointLocation;
	
	public void insertData(String pickupDropoffName, Set<Students> students, boolean defaultPickupDropoff, LocationPoint monPickupPointLocation, LocationPoint monDropoffPointLocation,
			LocationPoint tuePickupPointLocation, LocationPoint tueDropoffPointLocation, LocationPoint wedPickupPointLocation, LocationPoint wedDropoffPointLocation,
			LocationPoint thurPickupPointLocation, LocationPoint thurDropoffPointLocation, LocationPoint friPickupPointLocation, LocationPoint friDropoffPointLocation) {
		
		this.pickupDropoffName = pickupDropoffName;
		this.students = students;
		this.defaultPickupDropoff = defaultPickupDropoff;
		this.monPickupPointLocation = monPickupPointLocation;
		this.monDropoffPointLocation = monDropoffPointLocation;
		this.tuePickupPointLocation = tuePickupPointLocation;
		this.tueDropoffPointLocation = tueDropoffPointLocation;
		this.wedPickupPointLocation = wedPickupPointLocation;
		this.wedDropoffPointLocation = wedDropoffPointLocation;
		this.thurPickupPointLocation = thurPickupPointLocation;
		this.thurDropoffPointLocation = thurDropoffPointLocation;
		this.friPickupPointLocation = friPickupPointLocation;
		this.friDropoffPointLocation = friDropoffPointLocation;
	}

	public void setSamePoints(LocationPoint location) {
		this.defaultPickupDropoff = true;
		this.monPickupPointLocation = location;
	    this.monDropoffPointLocation = location;
	    this.tuePickupPointLocation = location;
	    this.tueDropoffPointLocation = location;
	    this.wedPickupPointLocation = location;
	    this.wedDropoffPointLocation = location;
	    this.thurPickupPointLocation = location;
	    this.thurDropoffPointLocation = location;
	    this.friPickupPointLocation = location;
	    this.friDropoffPointLocation = location;
	}
	
	public void setMonday(LocationPoint location) {
		this.defaultPickupDropoff = false;
		this.monPickupPointLocation = location;
	    this.monDropoffPointLocation = location;
	    this.tuePickupPointLocation = null;
	    this.tueDropoffPointLocation = null;
	    this.wedPickupPointLocation = null;
	    this.wedDropoffPointLocation = null;
	    this.thurPickupPointLocation = null;
	    this.thurDropoffPointLocation = null;
	    this.friPickupPointLocation = null;
	    this.friDropoffPointLocation = null;
	}
	
	public void setTuesday(LocationPoint location) {
		this.defaultPickupDropoff = false;
		this.monPickupPointLocation = null;
	    this.monDropoffPointLocation = null;
	    this.tuePickupPointLocation = location;
	    this.tueDropoffPointLocation = location;
	    this.wedPickupPointLocation = null;
	    this.wedDropoffPointLocation = null;
	    this.thurPickupPointLocation = null;
	    this.thurDropoffPointLocation = null;
	    this.friPickupPointLocation = null;
	    this.friDropoffPointLocation = null;
	}
	
	public void setWednesday(LocationPoint location) {
		this.defaultPickupDropoff = false;
		this.monPickupPointLocation = null;
	    this.monDropoffPointLocation = null;
	    this.tuePickupPointLocation = null;
	    this.tueDropoffPointLocation = null;
	    this.wedPickupPointLocation = location;
	    this.wedDropoffPointLocation = location;
	    this.thurPickupPointLocation = null;
	    this.thurDropoffPointLocation = null;
	    this.friPickupPointLocation = null;
	    this.friDropoffPointLocation = null;
	}
	
	public void setThursday(LocationPoint location) {
		this.defaultPickupDropoff = false;
		this.monPickupPointLocation = null;
	    this.monDropoffPointLocation = null;
	    this.tuePickupPointLocation = null;
	    this.tueDropoffPointLocation = null;
	    this.wedPickupPointLocation = null;
	    this.wedDropoffPointLocation = null;
	    this.thurPickupPointLocation = location;
	    this.thurDropoffPointLocation = location;
	    this.friPickupPointLocation = null;
	    this.friDropoffPointLocation = null;
	}
	
	public void setFriday(LocationPoint location) {
		this.defaultPickupDropoff = false;
		this.monPickupPointLocation = null;
	    this.monDropoffPointLocation = null;
	    this.tuePickupPointLocation = null;
	    this.tueDropoffPointLocation = null;
	    this.wedPickupPointLocation = null;
	    this.wedDropoffPointLocation = null;
	    this.thurPickupPointLocation = null;
	    this.thurDropoffPointLocation = null;
	    this.friPickupPointLocation = location;
	    this.friDropoffPointLocation = location;
	}
	
	public String getDay() {
		String day = null;
		
		if(this.getDefaultPickupDropoff() == true) { // Check default first then can check which is null to determine day
			day = "All Days";
		} else if(this.getMonPickupPointLocation() != null) {
			day = "Monday";
		} else if(this.getTuePickupPointLocation() != null) {
			day = "Tuesday";
		} else if(this.getWedPickupPointLocation() != null) {
			day = "Wednesday";
		} else if(this.getThurPickupPointLocation() != null) {
			day = "Thursday";
		} else if(this.getFriPickupPointLocation() != null) {
			day = "Friday";
		}
		
		return day;
	}
	
	//getters and setters
	public long getPickupDropoffId() {
		return pickupDropoffId;
	}

	public void setPickupDropoffId(long pickupDropoffId) {
		this.pickupDropoffId = pickupDropoffId;
	}
	
	public String getPickupDropoffName() {
		return pickupDropoffName;
	}

	public void setPickupDropoffName(String pickupDropoffName) {
		this.pickupDropoffName = pickupDropoffName;
	}

	public Set<Students> getStudents() {
		return students;
	}

	public void setStudents(Set<Students> students) {
		this.students = students;
	}

	public boolean getDefaultPickupDropoff() {
		return defaultPickupDropoff;
	}

	public void setDefaultPickupDropoff(boolean defaultPickupDropoff) {
		this.defaultPickupDropoff = defaultPickupDropoff;
	}

	public LocationPoint getMonPickupPointLocation() {
		return monPickupPointLocation;
	}

	public void setMonPickupPointLocation(LocationPoint monPickupPointLocation) {
		this.monPickupPointLocation = monPickupPointLocation;
	}

	public LocationPoint getMonDropoffPointLocation() {
		return monDropoffPointLocation;
	}

	public void setMonDropoffPointLocation(LocationPoint monDropoffPointLocation) {
		this.monDropoffPointLocation = monDropoffPointLocation;
	}

	public LocationPoint getTuePickupPointLocation() {
		return tuePickupPointLocation;
	}

	public void setTuePickupPointLocation(LocationPoint tuePickupPointLocation) {
		this.tuePickupPointLocation = tuePickupPointLocation;
	}

	public LocationPoint getTueDropoffPointLocation() {
		return tueDropoffPointLocation;
	}

	public void setTueDropoffPointLocation(LocationPoint tueDropoffPointLocation) {
		this.tueDropoffPointLocation = tueDropoffPointLocation;
	}

	public LocationPoint getWedPickupPointLocation() {
		return wedPickupPointLocation;
	}

	public void setWedPickupPointLocation(LocationPoint wedPickupPointLocation) {
		this.wedPickupPointLocation = wedPickupPointLocation;
	}

	public LocationPoint getWedDropoffPointLocation() {
		return wedDropoffPointLocation;
	}

	public void setWedDropoffPointLocation(LocationPoint wedDropoffPointLocation) {
		this.wedDropoffPointLocation = wedDropoffPointLocation;
	}

	public LocationPoint getThurPickupPointLocation() {
		return thurPickupPointLocation;
	}

	public void setThurPickupPointLocation(LocationPoint thurPickupPointLocation) {
		this.thurPickupPointLocation = thurPickupPointLocation;
	}

	public LocationPoint getThurDropoffPointLocation() {
		return thurDropoffPointLocation;
	}

	public void setThurDropoffPointLocation(LocationPoint thurDropoffPointLocation) {
		this.thurDropoffPointLocation = thurDropoffPointLocation;
	}

	public LocationPoint getFriPickupPointLocation() {
		return friPickupPointLocation;
	}

	public void setFriPickupPointLocation(LocationPoint friPickupPointLocation) {
		this.friPickupPointLocation = friPickupPointLocation;
	}

	public LocationPoint getFriDropoffPointLocation() {
		return friDropoffPointLocation;
	}

	public void setFriDropoffPointLocation(LocationPoint friDropoffPointLocation) {
		this.friDropoffPointLocation = friDropoffPointLocation;
	}
}
