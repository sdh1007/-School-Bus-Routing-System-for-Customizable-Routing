package sru.edu.SchoolRouteMgt.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/* 
 * This class is used to access data relating to currently logged in users
 */
public class CustomUserDetails implements UserDetails { 

	private User user;
	private District district;

	public CustomUserDetails(User user) {//, District district
		this.user = user;
		//this.district = district;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles =user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	public District getDistrict() {
		return user.getDistrict();
	}
	
	// These three methods just allow for each account to be functional, if needed, they
	// could be customized to do something different

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override 
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}
	
	public String getLastName() {
		return user.getLastName();
	}
	

	public String getFullName() {
		return user.getFirstName() + " " + user.getLastName();
	}
	
	public Long getId() {
		return user.getId();
	}
	
	/*public District getDistrict()
	{
		return district;
	}*/

}
