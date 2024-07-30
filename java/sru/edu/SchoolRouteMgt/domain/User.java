package sru.edu.SchoolRouteMgt.domain;

import java.util.HashSet;
import java.util.Set;

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

import org.springframework.lang.NonNull;
/*
 * 
 * This class contains information to the database to store user information from the login, it contains id, email,
 * password, first name and last name.
 * 
 * Contributed by Group 3 - Zach Freilano
 * Contributed by Group 5 - Pavels Avdejevs
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
/* 
 * This class is designed to hold all of the information for the users of the program and is tied to a role & district
 */
@Entity
public class User {
	@Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
    
   @Column(nullable = false, unique = true, length = 45)
   private String email;
   
   @Column(nullable = false, length = 64)
   private String password;
   
   @Column(name = "first_name", nullable = false, length = 30)
   private String firstName;
   
   @Column(name = "last_name", nullable = false, length = 30)
   private String lastName;
   
   @Column(name = "verification_code", updatable = false, length = 64)
   private String verificationCode;
   
   @Column(name = "account_activity", updatable = false, length = 64)
   private Boolean accountActivity;
   
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
		   name = "users_roles",
		   joinColumns = @JoinColumn(name = "user_id"),
		   inverseJoinColumns = @JoinColumn(name = "role_id")
		   )
   private Set<Role> roles = new HashSet<>();
   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "district_id")
   private District district;
   
   //getters and setters
   public void setId(long id) {
	   this.id = id;
   }
   
   public long getId() {
	   return id;
   }

   public void setEmail(String email) {
	   this.email = email;
   }
   
   public String getEmail() {
	   return email;
   }

   public String getPassword() {
	   return password;
   }
   

   public void setPassword(String password) {
	   this.password = password;
   }
   
   public String getFirstName() {
	   return firstName;
   }

   public void setFirstName(String firstName) {
	   this.firstName = firstName;
   }

   public String getLastName() {
	   return lastName;
   }

   public void setLastName(String lastName) {
	   this.lastName = lastName;
   }

   public Set<Role> getRoles() {
	   return roles;
   }

   public void setRoles(Set<Role> roles) {
	   this.roles = roles;
   }
   
   public void addRole(Role role) {
	   this.roles.add(role);
   }
   
   public District getDistrict() {
		return district;
   }
   
   @JsonIgnore
   public String getDistrictName1() {
	    return district.getDistrictName();
	}
   
   public void setDistrict(District district) {
		this.district = district;
   }

	public String getVerificationCode() {
		return verificationCode;
	}
	
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Boolean getAccountActivity() {
		return accountActivity;
	}

	public void setAccountActivity(Boolean accountActivity) {
		this.accountActivity = accountActivity;
	}
	
   
}
   