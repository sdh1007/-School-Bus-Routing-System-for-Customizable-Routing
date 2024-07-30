package sru.edu.SchoolRouteMgt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import sru.edu.SchoolRouteMgt.domain.*;
import sru.edu.SchoolRouteMgt.repository.*;


/*Designed by Group 3 - Bonus Project
 * CPSC-488-01
 * Main Author: Zack Freilano
 * This class customizes the UserDetailsService as defined by Maven. Currently, all that it
 * does is search for users and throw exceptions if not found. 
 * 
 * */
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		/*District district = districtRepository.findByName(username);
		
		if (district == null) {
			throw new UsernameNotFoundException("Invalid district code.");
		}*/
		
		return new CustomUserDetails(user);//,district
	}

}
