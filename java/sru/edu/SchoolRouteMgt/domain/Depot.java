package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
/*
 *  This class holds the information for the  depot table. 
 *  The depot is used as a location for the bus to be parked before and after making its rounds.
 */
 
@Entity
public class Depot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int depotId;
	@NonNull
	private String contractor;
	@NonNull
	private String name;
	@NonNull
	private String address1;
	@NonNull
	private String address2;
	@NonNull
	private String city;
	@NonNull
	private String state;
	@NonNull
	private String zipCode;
	@NonNull
	private String roadNo;
	@NonNull
	private String roadName;
	@NonNull
	private boolean isActive;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;
	
	//one-to-one relationship so a depot can be associated with a location point
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
	private LocationPoint location;
	
	//one-to-many relationship so each depot can store many vehicles inside of it via hash set
	//@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval=true)
	@JoinColumn(name = "depot_depot_id")
	private Set<Vehicle> vehicles = new HashSet<>();
	
	//one-to-many relationship so each depot can be associated with multiple routes
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "depotId")
	private Set<Routing> routing = new HashSet<>();
	
	//insert data method allows for data from the LoadDepot class to be mapped to the database
	public void insertData(int depotId, String contractor, String name, String address1, String address2,
			String city, String state, String zipCode, String roadNo, String roadName, boolean isActive) {
    	
    	this.depotId = depotId;
    	this.contractor = contractor;
    	this.name = name;
    	this.address1 = address1;
    	this.address2 = address2;
    	this.city = city;
    	this.state = state;
    	this.zipCode = zipCode;
    	this.roadNo = roadNo;
    	this.roadName = roadName;
    	this.isActive = isActive;
    }
	
	// Allows for loading data from CSV to be mapped to the database
	public void loadData(String contractor, String name, String address1, String address2,
			String city, String state, String zipCode, String roadNo, String roadName, boolean isActive) {
    	
    	this.contractor = contractor;
    	this.name = name;
    	this.address1 = address1;
    	this.address2 = address2;
    	this.city = city;
    	this.state = state;
    	this.zipCode = zipCode;
    	this.roadNo = roadNo;
    	this.roadName = roadName;
    	this.isActive = isActive;
    }
	
	//getters and setters
	public void setLocation(LocationPoint location) {
		this.location = location;
	}
	
	public LocationPoint getLocation() {
		return location;
	}
	
	public Set<Routing> getRouting() {
		return routing;
	}
	
	public void setRouting(Set<Routing> routing) {
		this.routing=routing;
	}

	
	public Set<Vehicle> getVehicle() {
		return vehicles;
	}
	public void setVehicle(Set<Vehicle> vehicles) {
		this.vehicles=vehicles;
	}
	
	public int getDepotId() {
		return depotId;
	}
	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	public String getContractor() {
		return contractor;
	}
	public void setContractor(String contractor) {
		this.contractor = contractor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
/*	
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
*/	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getRoadNo() {
		return roadNo;
	}
	public void setRoadNo(String roadNo) {
		this.roadNo = roadNo;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	 public District getDistrictId() {
			return district;
	 }
	   
	 public void setDistrictId(District district) {
		this.district = district;
	 }
}
