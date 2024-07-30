package sru.edu.SchoolRouteMgt.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/* 
 * This class is designed to hold a grouping of schools to be used for creating routes
 */
@Entity
public class RouteGroups {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long groupId;
	
	@Column(nullable = false, length =45)
	private String groupName;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;
	
	// map many group to to many schools
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private Set<Schools> selectSchools = new HashSet<>();
	
	@Column(nullable = false, length =45)
	 private String groupCode;
	
	@Column(nullable = false, length =45)
	private String groupDescription;
	
	public RouteGroups() {
	}
	
	public RouteGroups(String groupName) {
		this.groupName = groupName;
	}
	
	public RouteGroups(Long groupId) {
		this.groupId = groupId;	
	}
	
	public RouteGroups(Long groupId, String groupName) {
		this.groupId = groupId;
		this.groupName = groupName;
	}
	
	//getters and setters
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}	

	public Set<Schools> getSelectSchools() {
		return selectSchools;
	}
	public void setSelectSchools(Set<Schools> selectSchools) {
		this.selectSchools = selectSchools;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public void setDistrictId(District district) {
		this.district = district;
	 }
}
