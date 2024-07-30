/*
 * Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on school objects.
 *
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   School Repository
 * Basic maven repository that is linked to the school domain object. Is utilized as the main method to search the
 * school mysql table for data. Also contains custom methods to find objects based on the object school name as well as iterate
 * through a list of school ids to generate a list of schools.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.domain.Vehicle;

public interface SchoolRepository extends CrudRepository<Schools, Long> {
	List<Schools> findByDistrict(District district);
	List<Schools> findAllByOrderBySchoolNameAsc();
	
	Schools findBySchoolName(String schoolName);

	boolean existsBySchoolName(String schoolName);
	
	Iterable<Schools> findAllByIdIn(List<Long> id);
}