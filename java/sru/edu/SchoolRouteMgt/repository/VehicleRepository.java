/*
 * Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on vehicle objects.
 *
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   Vehicle Repository
 * Basic maven repository that is linked to the vehicle domain object. Is utilized as the main method to search the
 * vehicle mysql table for data. Also contains custom methods to find a vehicle based on it's vehicle code. Can also generate
 * a list of vehicle objects based on whether or not the object has a driver assigned.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Schools;
import sru.edu.SchoolRouteMgt.domain.Vehicle;


public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Vehicle findByVehicleCode(String vehicleCode);
	
	List<Vehicle> findByDistrict(District district);
	
	Iterable<Vehicle> findAllByHasDriver(boolean hasDriver);



}