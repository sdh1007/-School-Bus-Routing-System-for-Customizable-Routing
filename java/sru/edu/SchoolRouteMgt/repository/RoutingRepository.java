/*
 * Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on routing objects.
 *
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   Routing Repository
 * Basic maven repository that is linked to the routing domain object. Is utilized as the main method to search the
 * routing mysql table for data.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.Drivers;
import sru.edu.SchoolRouteMgt.domain.RouteGroups;
import sru.edu.SchoolRouteMgt.domain.Routing;
import sru.edu.SchoolRouteMgt.domain.Students;
import sru.edu.SchoolRouteMgt.domain.Vehicle;

public interface RoutingRepository extends JpaRepository<Routing, Long> {
	Set<Routing> findAllByVehicle(Vehicle vehicle);
	Set<Routing> findAllByDriver(Drivers driver);
	Set<Routing> findAllByPickupStudents(Students student);
	Set<Routing> findAllByGroup(RouteGroups group);
}