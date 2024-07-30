/*
 * Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on locationpoint objects.
 *
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   Location Point Repository
 * Basic maven repository that is linked to the locationpoint domain object. Is utilized as the main method to search the
 * locationpoint mysql table for data.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.LocationPoint;

public interface LocationPointRepository extends JpaRepository<LocationPoint, Integer> {}