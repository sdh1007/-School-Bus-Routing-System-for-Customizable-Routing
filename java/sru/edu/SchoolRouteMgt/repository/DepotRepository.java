/* Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on depot objects.
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   Depot Repository
 * Basic maven repository that is linked to the depot domain object. Is utilized as the main method to search the depot
 * mysql table for information. Has added options to search the database by it's address and by the depot name itself.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.Depot;
import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.Schools;


public interface DepotRepository extends JpaRepository<Depot, Integer> {
	Depot findByAddress1(String address1);
	List<Depot> findByDistrict(District district);
	Depot findByName(String name);

}
