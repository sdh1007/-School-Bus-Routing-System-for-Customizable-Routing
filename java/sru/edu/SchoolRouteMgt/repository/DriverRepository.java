/*
 * Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on driver objects.
 *
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   Driver Information Repository
 * Basic maven repository that is linked to the driverinformation domain object. Is utilized as the main method to search the
 * driverinformation mysql table for data. Also contains a custom method to find objects based on their assign parent vehicle
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Drivers;
import sru.edu.SchoolRouteMgt.domain.Vehicle;

public interface DriverRepository extends JpaRepository<Drivers, Long> {
	Drivers findByVehicles(Vehicle vehicle);
	List<Drivers> findByDistrict(District district);
}