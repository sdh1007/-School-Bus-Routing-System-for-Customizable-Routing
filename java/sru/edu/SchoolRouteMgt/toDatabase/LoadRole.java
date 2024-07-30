package sru.edu.SchoolRouteMgt.toDatabase;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sru.edu.SchoolRouteMgt.domain.Role;
import sru.edu.SchoolRouteMgt.repository.RoleRepository;

@Component
public class LoadRole implements CommandLineRunner { // runs when you load it

	@Autowired 
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {

		int exists = (int) roleRepository.count();
 
		if(exists == 0) {
			Role user = new Role("User");
			Role manager = new Role("Transportation_Manager");
			Role dis_admin = new Role("District_Admin");
			Role sys_admin = new Role("System_Admin");
			
			roleRepository.saveAll(List.of(user, manager, dis_admin, sys_admin));
			
			List<Role> listRoles = roleRepository.findAll();
			System.out.println("Number of roles created: " + listRoles.size());
	
		} else {
			System.out.println("Roles data already loaded.");
		}
	}
}