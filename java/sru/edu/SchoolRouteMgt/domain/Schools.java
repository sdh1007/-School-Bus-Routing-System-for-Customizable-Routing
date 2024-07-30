 package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
/* This class focuses on the different types of Schools in the system, new and existing students
  can be assigned to a school, along with the user being able to create a school, and the address
  and location of where the school is located, it contains various information such as
  the name, grade, type, start and end time, etc 
  */ 
@Entity
public class Schools {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NonNull
	private String schoolName;
	@NonNull
	private String type; 
	@NonNull
	private String schoolGrade; 
	@NonNull
	private int studentCount; 
	@NonNull
	private int startHour; 
	@NonNull 
	private int startMin; 
	@NonNull 
	private String startAmPm; 
	@NonNull 
	private int endHour; 
	@NonNull
	private int endMin; 
	@NonNull
	private String endAmPm;
	@NonNull
	private Date startDate; 
	@NonNull
	private Date endDate; 
	@NonNull
	private int schoolDays;
	@NonNull
	private String address;
	@NonNull
	private String city;
	@NonNull
	private String state;
	@NonNull
	private int zip;
	@NonNull
	private String phone;
	@NonNull
	private boolean isActive;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;
	
	//one-to-one relationship so each location point is associated with one school
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
	private LocationPoint location;
	
	//mapping students one to many from schools
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval=true)
	@JoinColumn(name = "school_id")
	@JsonIgnoreProperties({ "school" })
	private Set<Students> students = new HashSet<>();
	
	//insertData method takes in information from ToDatabase class to map data to MySQL columns
	public void insertData(int id, String schoolName, String type, String schoolGrade, int studentCount,
			int startHour, int startMin, String startAmPm, int endHour, int endMin, String endAmPm, 
			int schoolDays, Date startDate, Date endDate, String phone, int zip, String city, String state, boolean isActive, String address) {
		
		this.id = id; 
		this.schoolName = schoolName; 
		this.type = type; 
		this.schoolGrade = schoolGrade; 
		this.studentCount = studentCount; 
		this.startHour = startHour;
		this.startMin = startMin; 
		this.startAmPm = startAmPm; 
		this.endHour = endHour; 
		this.endMin = endMin; 
		this.endAmPm = endAmPm; 
		this.schoolDays = schoolDays;
		this.startDate = startDate; 
		this.endDate = endDate;
		this.phone = phone;
		this.zip = zip;
		this.city = city;
		this.state = state;
		this.isActive = isActive;
		this.address = address;
	}
	
	// Takes in information CSV to map data to MySQL columns
	public void loadData(String schoolName, String type, String schoolGrade, int studentCount,
			int startHour, int startMin, String startAmPm, int endHour, int endMin, String endAmPm, 
			int schoolDays, Date startDate, Date endDate, String phone, int zip, String city, String state, boolean isActive, String address) {
		
		this.schoolName = schoolName; 
		this.type = type; 
		this.schoolGrade = schoolGrade; 
		this.studentCount = studentCount; 
		this.startHour = startHour;
		this.startMin = startMin; 
		this.startAmPm = startAmPm; 
		this.endHour = endHour; 
		this.endMin = endMin; 
		this.endAmPm = endAmPm; 
		this.schoolDays = schoolDays;
		this.startDate = startDate; 
		this.endDate = endDate;
		this.phone = phone;
		this.zip = zip;
		this.city = city;
		this.state = state;
		this.isActive = isActive;
		this.address = address;
	}

	//getters and setters for student
	public Set<Students> getStudent() {
		return students;
	}
	public void setStudent(Set<Students> students) {
		this.students = students;
	}
	public void setLocation(LocationPoint location) {
		this.location = location;
	}
	
	public LocationPoint getLocation() {
		return location;
	}
	public long getId() {
		return id; 
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSchoolGrade() {
		return schoolGrade;
	}
	public void setSchoolGrade(String schoolGrade) {
		this.schoolGrade = schoolGrade; 
	}
	public int getStudentCount() {
		return studentCount; 
	}
	public void setstudentCount(int studentCount) {
		this.studentCount = studentCount; 
	}

	public int getSchoolDays() {
		return schoolDays;
	}
	public void setschoolDays(int schoolDays) {
		this.schoolDays= schoolDays;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public boolean getisActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public void setSchoolDays(int schoolDays) {
		this.schoolDays = schoolDays;
	}
	
	
	   public District getDistrictId() {
			return district;
	   }
	   
	   public void setDistrictId(District district) {
			this.district = district;
	   }

	public void incrementStudentCount() {
		this.studentCount  = studentCount + 1;
	}
	
	public void decrementStudentCount() {
		this.studentCount  = studentCount - 1;
	}
	

	
	public String getMinString(int minInt) {
		String minString; 
		
		if(Integer.toString(minInt).trim().length() == 1) {
			minString = "0" + Integer.toString(minInt);
		}
		else {
			minString = Integer.toString(minInt);
		}
		
		return minString;
	}
} 
