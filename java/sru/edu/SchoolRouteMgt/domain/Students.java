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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

/* 
 * This class is designed to hold all of the information for the Students within a school & linked to pickup/dropoff points
 */
@Entity
public class Students {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int studentId;
	@NonNull private int studId;
	@NonNull private boolean graduated;
	@NonNull private String firstName;
	@NonNull private String midName;
    @NonNull private String lastName;
    @NonNull private String suffix;
    @NonNull private String grade;
    @NonNull private String address1;
    @NonNull private String address2;
    @NonNull private String city;
    @NonNull private String state;
    @NonNull private int zipCode;
    @NonNull private String gender; 
    @NonNull private Date birthDate; 
    @NonNull private String phoneNum;
    @NonNull private String cellPhoneNum;    
    @NonNull private String emergencyPhoneNum;
    @NonNull private String parentName;
    @NonNull private String schoolName;
    @NonNull private Date enter;
    @NonNull private boolean isEnter;
    @NonNull private Date Withdrawl;
    @NonNull private boolean isWithdrawl;
    @NonNull private boolean hazardousRoad;
    @NonNull private String travelMode;
    @NonNull private String driverNote;
    @NonNull private String misc;
    @NonNull private String journal;
    @NonNull private String medical;
    @NonNull private boolean isCustody;
    @Column(columnDefinition = "TEXT") private String alt1Name;
    @Column(columnDefinition = "TEXT") private String alt1Relationship;
    @Column(columnDefinition = "TEXT") private String alt1Address1;
    @Column(columnDefinition = "TEXT") private String alt1Address2;
    @Column(columnDefinition = "TEXT") private String alt1City;
    @Column(columnDefinition = "TEXT") private String alt1State;
    @Column(columnDefinition = "TEXT") private long alt1ZipCode;
    @Column(columnDefinition = "TEXT") private String alt1Phone;
    @Column(columnDefinition = "TEXT") private String alt1CellPhone;
    @Column(columnDefinition = "TEXT") private String alt2Name;
    @Column(columnDefinition = "TEXT") private String alt2Relationship;
    @Column(columnDefinition = "TEXT") private String alt2Address1;
    @Column(columnDefinition = "TEXT") private String alt2Address2;
    @Column(columnDefinition = "TEXT") private String alt2City;
    @Column(columnDefinition = "TEXT") private String alt2State;
    @Column(columnDefinition = "TEXT") private long alt2ZipCode;
    @Column(columnDefinition = "TEXT") private String alt2Phone;
    @Column(columnDefinition = "TEXT") private String alt2CellPhone;
    @Column(columnDefinition = "TEXT") private String alt3Name;
    @Column(columnDefinition = "TEXT") private String alt3Relationship;
    @Column(columnDefinition = "TEXT") private String alt3Address1;
    @Column(columnDefinition = "TEXT") private String alt3Address2;
    @Column(columnDefinition = "TEXT") private String alt3City;
    @Column(columnDefinition = "TEXT") private String alt3State;
    @Column(columnDefinition = "TEXT") private long alt3ZipCode;
    @Column(columnDefinition = "TEXT") private String alt3Phone;
    @Column(columnDefinition = "TEXT") private String alt3CellPhone;
    @NonNull private String studentType1;
    @NonNull private String studentType2;
    @NonNull private String studentType3;
   
    // bi-directional relationship & many-to-one relationship so multiple students are associated with one school
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "school_id")
    @JsonIgnoreProperties({ "student" })
    private Schools school;
    
    //many-to-one relationship so multiple students are associated with one pickupDropoff
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_dropoff_id")
	private PickupDropoff pickupDropoff;

    //one-to-one mapping so each student is associated with one location point
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private LocationPoint location;
    
    //one-to-many mapping so each student location point
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private LocationPoint monMornLocation;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private LocationPoint monAfterLocation;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint tuesMornLocation;
       
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint tuesAfterLocation;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint wedMornLocation;
       
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint wedAfterLocation;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint thurMornLocation;
       
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint thurAfterLocation;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint friMornLocation;
       
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   	private LocationPoint friAfterLocation;

    //insertData method takes in data from ToDatabase package to map data to MySQL columns
    public void insertData(int studentId, int studId, boolean graduated, String firstName, String midName,
    		String lastName, String suffix, String grade, String address1, String address2, String city,
    		String state, int zipCode, String gender, Date birthDate, String phoneNum,
    		String cellPhoneNum, String emergencyPhoneNum, String parentName, String schoolName,
    		Date enter, boolean isEnter, Date Withdrawl, boolean isWithdrawl, boolean hazardousRoad,
    		String travelMode, String driverNote, String misc, String journal, String medical,
    		boolean isCustody, String alt1Name, String alt1Relationship, String alt1Address1,
    		String alt1Address2, String alt1City, String alt1State, long alt1ZipCode,
    		String alt1Phone, String alt1CellPhone, String alt2Name, String alt2Relationship,
    		String alt2Address1, String alt2Address2, String alt2City, String alt2State,
    		long alt2ZipCode, String alt2Phone, String alt2CellPhone, String alt3Name,
    		String alt3Relationship, String alt3Address1, String alt3Address2, String alt3City,
    		String alt3State, long alt3ZipCode, String alt3Phone, String alt3CellPhone,
    		String studentType1, String studentType2, String studentType3) {
    	
    	this.studentId = studentId;
		this.studId = studId;
		this.graduated = graduated;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.suffix = suffix;
		this.grade = grade;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.gender = gender;
		this.birthDate = birthDate;
		this.phoneNum = phoneNum;
		this.cellPhoneNum = cellPhoneNum;
		this.emergencyPhoneNum = emergencyPhoneNum;
		this.parentName = parentName;
		this.schoolName = schoolName;
		this.enter = enter;
		this.isEnter = isEnter;
		this.Withdrawl=Withdrawl;
		this.isWithdrawl = isWithdrawl;
		this.hazardousRoad = hazardousRoad;
		this.travelMode = travelMode;
		this.driverNote = driverNote;
		this.misc = misc;
		this.journal = journal;
		this.medical = medical;
		this.isCustody = isCustody;
		this.alt1Name = alt1Name;
		this.alt1Relationship = alt1Relationship;
		this.alt1Address1 = alt1Address1;
		this.alt1Address2 = alt1Address2;
		this.alt1City = alt1City;
		this.alt1State = alt1State;
		this.alt1ZipCode = alt1ZipCode;
		this.alt1Phone = alt1Phone;
		this.alt1CellPhone = alt1CellPhone;
		this.alt2Name = alt2Name;
		this.alt2Relationship = alt2Relationship;
		this.alt2Address1 = alt2Address1;
		this.alt2Address2 = alt2Address2;
		this.alt2City = alt2City;
		this.alt2State = alt2State;
		this.alt2ZipCode = alt2ZipCode;
		this.alt2Phone = alt2Phone;
		this.alt2CellPhone = alt2CellPhone;
		this.alt3Name = alt3Name;
		this.alt3Relationship = alt3Relationship;
		this.alt3Address1 = alt3Address1;
		this.alt3Address2 = alt3Address2;
		this.alt3City = alt3City;
		this.alt3State = alt3State;
		this.alt3ZipCode = alt3ZipCode;
		this.alt3Phone = alt3Phone;
		this.alt3CellPhone = alt3CellPhone;
		this.studentType1 = studentType1;
		this.studentType2 = studentType2;
		this.studentType3 = studentType3;
    }
    
    // Takes in data from CSV file to map data to MySQL columns
    public void loadData(int studId, boolean graduated, String firstName, String midName,
    		String lastName, String suffix, String grade, String address1, String address2, String city,
    		String state, int zipCode, String gender, Date birthDate, String phoneNum,
    		String cellPhoneNum, String emergencyPhoneNum, String parentName, String schoolName,
    		Date enter, boolean isEnter, Date Withdrawl, boolean isWithdrawl, boolean hazardousRoad,
    		String travelMode, String driverNote, String misc, String journal, String medical,
    		boolean isCustody, String alt1Name, String alt1Relationship, String alt1Address1,
    		String alt1Address2, String alt1City, String alt1State, long alt1ZipCode,
    		String alt1Phone, String alt1CellPhone, String alt2Name, String alt2Relationship,
    		String alt2Address1, String alt2Address2, String alt2City, String alt2State,
    		long alt2ZipCode, String alt2Phone, String alt2CellPhone, String alt3Name,
    		String alt3Relationship, String alt3Address1, String alt3Address2, String alt3City,
    		String alt3State, long alt3ZipCode, String alt3Phone, String alt3CellPhone,
    		String studentType1, String studentType2, String studentType3) {
    	
		this.studId = studId;
		this.graduated = graduated;
		this.firstName = firstName;
		this.midName = midName;
		this.lastName = lastName;
		this.suffix = suffix;
		this.grade = grade;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.gender = gender;
		this.birthDate = birthDate;
		this.phoneNum = phoneNum;
		this.cellPhoneNum = cellPhoneNum;
		this.emergencyPhoneNum = emergencyPhoneNum;
		this.parentName = parentName;
		this.schoolName = schoolName;
		this.enter = enter;
		this.isEnter = isEnter;
		this.Withdrawl=Withdrawl;
		this.isWithdrawl = isWithdrawl;
		this.hazardousRoad = hazardousRoad;
		this.travelMode = travelMode;
		this.driverNote = driverNote;
		this.misc = misc;
		this.journal = journal;
		this.medical = medical;
		this.isCustody = isCustody;
		this.alt1Name = alt1Name;
		this.alt1Relationship = alt1Relationship;
		this.alt1Address1 = alt1Address1;
		this.alt1Address2 = alt1Address2;
		this.alt1City = alt1City;
		this.alt1State = alt1State;
		this.alt1ZipCode = alt1ZipCode;
		this.alt1Phone = alt1Phone;
		this.alt1CellPhone = alt1CellPhone;
		this.alt2Name = alt2Name;
		this.alt2Relationship = alt2Relationship;
		this.alt2Address1 = alt2Address1;
		this.alt2Address2 = alt2Address2;
		this.alt2City = alt2City;
		this.alt2State = alt2State;
		this.alt2ZipCode = alt2ZipCode;
		this.alt2Phone = alt2Phone;
		this.alt2CellPhone = alt2CellPhone;
		this.alt3Name = alt3Name;
		this.alt3Relationship = alt3Relationship;
		this.alt3Address1 = alt3Address1;
		this.alt3Address2 = alt3Address2;
		this.alt3City = alt3City;
		this.alt3State = alt3State;
		this.alt3ZipCode = alt3ZipCode;
		this.alt3Phone = alt3Phone;
		this.alt3CellPhone = alt3CellPhone;
		this.studentType1 = studentType1;
		this.studentType2 = studentType2;
		this.studentType3 = studentType3;
    }

    //getters and setters
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getStudId() {
		return studId;
	}

	public void setStudId(int studId) {
		this.studId = studId;
	}

	public boolean isGraduated() {
		return graduated;
	}

	public void setGraduated(boolean graduated) {
		this.graduated = graduated;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

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

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getCellPhoneNum() {
		return cellPhoneNum;
	}

	public void setCellPhoneNum(String cellPhoneNum) {
		this.cellPhoneNum = cellPhoneNum;
	}

	public String getEmergencyPhoneNum() {
		return emergencyPhoneNum;
	}

	public void setEmergencyPhoneNum(String emergencyPhoneNum) {
		this.emergencyPhoneNum = emergencyPhoneNum;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Date getEnter() {
		return enter;
	}

	public void setEnter(Date enter) {
		this.enter = enter;
	}

	public boolean isEnter() {
		return isEnter;
	}

	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
	}

	public Date getWithdrawl() {
		return Withdrawl;
	}

	public void setWithdrawl(Date withdrawl) {
		Withdrawl = withdrawl;
	}

	public boolean isWithdrawl() {
		return isWithdrawl;
	}

	public void setWithdrawl(boolean isWithdrawl) {
		this.isWithdrawl = isWithdrawl;
	}

	public boolean isHazardousRoad() {
		return hazardousRoad;
	}

	public void setHazardousRoad(boolean hazardousRoad) {
		this.hazardousRoad = hazardousRoad;
	}

	public String getTravelMode() {
		return travelMode;
	}

	public void setTravelMode(String travelMode) {
		this.travelMode = travelMode;
	}

	public String getDriverNote() {
		return driverNote;
	}

	public void setDriverNote(String driverNote) {
		this.driverNote = driverNote;
	}

	public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getMedical() {
		return medical;
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public boolean isCustody() {
		return isCustody;
	}

	public void setCustody(boolean isCustody) {
		this.isCustody = isCustody;
	}

	public String getAlt1Name() {
		return alt1Name;
	}

	public void setAlt1Name(String alt1Name) {
		this.alt1Name = alt1Name;
	}

	public String getAlt1Relationship() {
		return alt1Relationship;
	}

	public void setAlt1Relationship(String alt1Relationship) {
		this.alt1Relationship = alt1Relationship;
	}

	public String getAlt1Address1() {
		return alt1Address1;
	}

	public void setAlt1Address1(String alt1Address1) {
		this.alt1Address1 = alt1Address1;
	}

	public String getAlt1Address2() {
		return alt1Address2;
	}

	public void setAlt1Address2(String alt1Address2) {
		this.alt1Address2 = alt1Address2;
	}

	public String getAlt1City() {
		return alt1City;
	}

	public void setAlt1City(String alt1City) {
		this.alt1City = alt1City;
	}

	public String getAlt1State() {
		return alt1State;
	}

	public void setAlt1State(String alt1State) {
		this.alt1State = alt1State;
	}

	public long getAlt1ZipCode() {
		return alt1ZipCode;
	}

	public void setAlt1ZipCode(long alt1ZipCode) {
		this.alt1ZipCode = alt1ZipCode;
	}

	public String getAlt1Phone() {
		return alt1Phone;
	}

	public void setAlt1Phone(String alt1Phone) {
		this.alt1Phone = alt1Phone;
	}

	public String getAlt1CellPhone() {
		return alt1CellPhone;
	}

	public void setAlt1CellPhone(String alt1CellPhone) {
		this.alt1CellPhone = alt1CellPhone;
	}

	public String getAlt2Name() {
		return alt2Name;
	}

	public void setAlt2Name(String alt2Name) {
		this.alt2Name = alt2Name;
	}

	public String getAlt2Relationship() {
		return alt2Relationship;
	}

	public void setAlt2Relationship(String alt2Relationship) {
		this.alt2Relationship = alt2Relationship;
	}

	public String getAlt2Address1() {
		return alt2Address1;
	}

	public void setAlt2Address1(String alt2Address1) {
		this.alt2Address1 = alt2Address1;
	}

	public String getAlt2Address2() {
		return alt2Address2;
	}

	public void setAlt2Address2(String alt2Address2) {
		this.alt2Address2 = alt2Address2;
	}

	public String getAlt2City() {
		return alt2City;
	}

	public void setAlt2City(String alt2City) {
		this.alt2City = alt2City;
	}

	public String getAlt2State() {
		return alt2State;
	}

	public void setAlt2State(String alt2State) {
		this.alt2State = alt2State;
	}

	public long getAlt2ZipCode() {
		return alt2ZipCode;
	}

	public void setAlt2ZipCode(long alt2ZipCode) {
		this.alt2ZipCode = alt2ZipCode;
	}

	public String getAlt2Phone() {
		return alt2Phone;
	}

	public void setAlt2Phone(String alt2Phone) {
		this.alt2Phone = alt2Phone;
	}

	public String getAlt2CellPhone() {
		return alt2CellPhone;
	}

	public void setAlt2CellPhone(String alt2CellPhone) {
		this.alt2CellPhone = alt2CellPhone;
	}

	public String getAlt3Name() {
		return alt3Name;
	}

	public void setAlt3Name(String alt3Name) {
		this.alt3Name = alt3Name;
	}

	public String getAlt3Relationship() {
		return alt3Relationship;
	}

	public void setAlt3Relationship(String alt3Relationship) {
		this.alt3Relationship = alt3Relationship;
	}

	public String getAlt3Address1() {
		return alt3Address1;
	}

	public void setAlt3Address1(String alt3Address1) {
		this.alt3Address1 = alt3Address1;
	}

	public String getAlt3Address2() {
		return alt3Address2;
	}

	public void setAlt3Address2(String alt3Address2) {
		this.alt3Address2 = alt3Address2;
	}

	public String getAlt3City() {
		return alt3City;
	}

	public void setAlt3City(String alt3City) {
		this.alt3City = alt3City;
	}

	public String getAlt3State() {
		return alt3State;
	}

	public void setAlt3State(String alt3State) {
		this.alt3State = alt3State;
	}

	public long getAlt3ZipCode() {
		return alt3ZipCode;
	}

	public void setAlt3ZipCode(long alt3ZipCode) {
		this.alt3ZipCode = alt3ZipCode;
	}

	public String getAlt3Phone() {
		return alt3Phone;
	}

	public void setAlt3Phone(String alt3Phone) {
		this.alt3Phone = alt3Phone;
	}

	public String getAlt3CellPhone() {
		return alt3CellPhone;
	}

	public void setAlt3CellPhone(String alt3CellPhone) {
		this.alt3CellPhone = alt3CellPhone;
	}

	public String getStudentType1() {
		return studentType1;
	}

	public void setStudentType1(String studentType1) {
		this.studentType1 = studentType1;
	}

	public String getStudentType2() {
		return studentType2;
	}

	public void setStudentType2(String studentType2) {
		this.studentType2 = studentType2;
	}

	public String getStudentType3() {
		return studentType3;
	}

	public void setStudentType3(String studentType3) {
		this.studentType3 = studentType3;
	}

	public Schools getSchool() {
		return school;
	}

	public void setSchool(Schools school) {
		this.school = school;
	}

	public PickupDropoff getPickupDropoff() {
		return pickupDropoff;
	}

	public void setPickupDropoff(PickupDropoff pickupDropoff) {
		this.pickupDropoff = pickupDropoff;
	}

	public LocationPoint getLocation() {
		return location;
	}

	public void setLocation(LocationPoint location) {
		this.location = location;
	}

	public LocationPoint getMonMornLocation() {
		return monMornLocation;
	}

	public void setMonMornLocation(LocationPoint monMornLocation) {
		this.monMornLocation = monMornLocation;
	}

	public LocationPoint getMonAfterLocation() {
		return monAfterLocation;
	}

	public void setMonAfterLocation(LocationPoint monAfterLocation) {
		this.monAfterLocation = monAfterLocation;
	}

	public LocationPoint getTuesMornLocation() {
		return tuesMornLocation;
	}

	public void setTuesMornLocation(LocationPoint tuesMornLocation) {
		this.tuesMornLocation = tuesMornLocation;
	}

	public LocationPoint getTuesAfterLocation() {
		return tuesAfterLocation;
	}

	public void setTuesAfterLocation(LocationPoint tuesAfterLocation) {
		this.tuesAfterLocation = tuesAfterLocation;
	}

	public LocationPoint getWedMornLocation() {
		return wedMornLocation;
	}

	public void setWedMornLocation(LocationPoint wedMornLocation) {
		this.wedMornLocation = wedMornLocation;
	}

	public LocationPoint getWedAfterLocation() {
		return wedAfterLocation;
	}

	public void setWedAfterLocation(LocationPoint wedAfterLocation) {
		this.wedAfterLocation = wedAfterLocation;
	}

	public LocationPoint getThurMornLocation() {
		return thurMornLocation;
	}

	public void setThurMornLocation(LocationPoint thurMornLocation) {
		this.thurMornLocation = thurMornLocation;
	}

	public LocationPoint getThurAfterLocation() {
		return thurAfterLocation;
	}

	public void setThurAfterLocation(LocationPoint thurAfterLocation) {
		this.thurAfterLocation = thurAfterLocation;
	}

	public LocationPoint getFriMornLocation() {
		return friMornLocation;
	}

	public void setFriMornLocation(LocationPoint friMornLocation) {
		this.friMornLocation = friMornLocation;
	}

	public LocationPoint getFriAfterLocation() {
		return friAfterLocation;
	}

	public void setFriAfterLocation(LocationPoint friAfterLocation) {
		this.friAfterLocation = friAfterLocation;
	}
}
