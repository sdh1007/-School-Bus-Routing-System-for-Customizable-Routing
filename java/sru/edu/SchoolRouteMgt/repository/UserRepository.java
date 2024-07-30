package sru.edu.SchoolRouteMgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import sru.edu.SchoolRouteMgt.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("Select u FROM User u WHERE u.email = ?1")
	public User findByEmail(String email);
	
}