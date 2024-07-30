/*
 * Group1 - School Routing
 * Authors: Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Vehicle Domain
 * Holds all of the vehicle information and possesses a one to one relationship with a driver. So each vehicle can only
 * have one driver, and can only be assigned to one depot. But a depot can hold multiple vehicles.
 * 
 *
 */
package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;

/* 
 * This class is designed to hold all of the information for the vehicles is can be tied to a depot & driver
 */
@Entity
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long vehicleInfo_id;
	@NonNull
	private String vehicleType;
	@NonNull
	private String vehicleCode;
	@NonNull
	private Date enterDate;
	@NonNull
	private String locationType;
	@NonNull
	private boolean hasDriver;
	@NonNull
	private int seatNumber;
	@NonNull
	private boolean isActive;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;
	
	//Maps Vehicles OneToOne to driver info by ID
	@OneToOne(mappedBy = "vehicles", fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private Drivers driverInformation;
	
	//maps vehicle IDs to Depot IDs
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	@JsonIgnoreProperties({ "vehicle" })
	private Depot depot;

	// Used for testing
	public void insertData(long vehicleInfo_id, String vehicleType, String vehicleCode, Date enterDate, 
			String locationType, int seatNumber, boolean isActive) {

		this.vehicleInfo_id = vehicleInfo_id;
		this.vehicleType = vehicleType;
		this.vehicleCode = vehicleCode;
		this.enterDate = enterDate;
		this.locationType = locationType;
		this.seatNumber = seatNumber;
		this.isActive = isActive;
	}
	
	// Takes in data from CSV to map data to MySQL columns
	public void loadData(String vehicleType, String vehicleCode, Date enterDate, 
			String locationType, int seatNumber, boolean isActive) {

		this.vehicleType = vehicleType;
		this.vehicleCode = vehicleCode;
		this.enterDate = enterDate;
		this.locationType = locationType;
		this.seatNumber = seatNumber;
		this.isActive = isActive;
	}
	
	//getters and setters for Depot
	public long getVehicleInfo_id() {
		return vehicleInfo_id;
	}

	public void setVehicleInfo_id(long vehicleInfo_id) {
		this.vehicleInfo_id = vehicleInfo_id;
	}
	
	public long getId() {
		return vehicleInfo_id;
	}

	public void setId(long vehicleInfo_id) {
		this.vehicleInfo_id = vehicleInfo_id;
	}
	
	public void setDriver(Drivers driverInformation) {
		this.driverInformation = driverInformation;
	}
	
	public Drivers getDriver() {
		return driverInformation;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public boolean isHasDriver() {
		return hasDriver;
	}

	public void setHasDriver(boolean hasDriver) {
		this.hasDriver = hasDriver;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Drivers getDriverInformation() {
		return driverInformation;
	}

	public void setDriverInformation(Drivers driverInformation) {
		this.driverInformation = driverInformation;
		
		if(this.driverInformation != null) {
			this.hasDriver = true;
		} else {
			this.hasDriver = false;
		}
	}

	public Depot getDepot() {
		return depot;
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}
}