package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
/* 
 * This class generates the LocationPoints for when an address is typed in, such as where a school
 * is located, and where the points are plotted on the GoogleMap
 */ 

@Entity
public class LocationPoint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int location_id;
	@NonNull
	private String locationPointName;
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
	private String roadName;
	@NonNull
	private float longitude;
	@NonNull
	private float latitude;
	@NonNull
	private boolean isActive;
	
	public void insertData(
			int location_id, String locationPointName, String address1, String address2, String city, String state, String zipCode,
			float latitude, float longitude, String roadName, boolean isActive) {
		this.location_id = location_id;
		this.locationPointName = locationPointName;
		this.address1 = address1;
		this.address2 = address2;
		this.state = state;
		this.zipCode = zipCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.roadName = roadName;
		this.isActive = isActive;
	}
	
	public void setAddressInfo(String address1, String address2, String city, String state, String zipCode) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	
	//getters and setters
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

	public int getLocationId() {
		return location_id;
	}

	public void setLocationId(int location_id) {
		this.location_id = location_id;
	}

	public String getLocationPointName() {
		return locationPointName;
	}

	public void setLocationPointName(String locationPointName) {
		this.locationPointName = locationPointName;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public int getLocation_id() {
		return location_id;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}