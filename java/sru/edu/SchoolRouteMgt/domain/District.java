package sru.edu.SchoolRouteMgt.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/* 
 * This class is designed to be assigned by most types data in order to separate data by school districts
 */
@Entity
@Table(name="district")
public class District {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long districtId;
	
	@Column(nullable = false, length =45)
	private String districtName;
	
	@Column(nullable = false, length =45)
	private String districtCode;
	
	@Column(nullable = false, length =45)
	private String districtDescription;
	
	@Column(nullable = false, length =45)
	private String districtDomain;
	
    //one-to-one relationship so there is one logo for every district
	@OneToOne(cascade = CascadeType.ALL)
	private FileInfo districtLogo;
    
	//one-to-one relationship so there is one background for every district
    @OneToOne(cascade = CascadeType.ALL)
	private FileInfo districtBackground;

	public District() {
	}
	
	public District( String districtName) {
		this.districtName = districtName;
	}
	public District(Long districtId) {
		this.districtId = districtId;	
	}
	
	public District(String districtName, String districtCode, String districtDescription, String districtDomain) {
		this.districtName = districtName;
		this.districtCode = districtCode;
		this.districtDescription = districtDescription;
		this.districtDomain = districtDomain;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictDescription() {
		return districtDescription;
	}

	public void setDistrictDescription(String districtDescription) {
		this.districtDescription = districtDescription;
	}
	
	public String getDistrictDomain() {
		return districtDomain;
	}

	public void setDistrictDomain(String districtDomain) {
		this.districtDomain = districtDomain;
	}
	
    public FileInfo getDistrictLogo() {
		return districtLogo;
	}

	public void setDistrictLogo(FileInfo districtLogo) {
		this.districtLogo = districtLogo;
	}

	public FileInfo getDistrictBackground() {
		return districtBackground;
	}

	public void setDistrictBackground(FileInfo districtBackground) {
		this.districtBackground = districtBackground;
	}
	
	@Override
	public String toString() {
		return this.districtName;
	}
}
