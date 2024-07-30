/* Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on datalog objects.
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
 * Datalog Repository
 * Basic maven repository that is linked to the datalog domain object. Is utilized as the main method to search the datalog
 * mysql table for information.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.DataLog;

public interface DataLogRepository extends JpaRepository<DataLog, Integer> {}
