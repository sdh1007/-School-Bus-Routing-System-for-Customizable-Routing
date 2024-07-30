package sru.edu.SchoolRouteMgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sru.edu.SchoolRouteMgt.domain.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	@Query("SELECT r FROM Role r WHERE r.name =?1")
	public Role findByName(String name);
	
	
}
