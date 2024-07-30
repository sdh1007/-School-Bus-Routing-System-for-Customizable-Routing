/*
 * Spring Data JPA allows one to implement JPA-based repositories (DAO (Data Access Object) pattern).
 * Spring Data JPA makes it easy to add CRUD functionality through a powerful later of abstraction 
 * placed on top of a JPA implementation. The abstraction later allows us to access the persistence layer
 * without having to provide our own DAO implementation from scratch.
 * 
 * Extending the CrudRepository interface provides the application with basic CRUD functionality on student objects.
 *
 * Group1 - School Routing
 * Lucas Luczak, Tyler Hammerschmidt, Nick Glass
 * CPSC-488-01
   FileInfo Repository
 * Basic maven repository that is linked to the student domain object. Is utilized as the main method to search the
 * student mysql table for data. Also contains custom methods to find student based on the school they are assigned to
 * and to find students by iterating through a list of school objects. Can also find a list of students via a list of 
 * school ids.
 * 
 */
package sru.edu.SchoolRouteMgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.FileInfo;
import sru.edu.SchoolRouteMgt.domain.Schools;

public interface FileRepository extends JpaRepository<FileInfo, Integer> {
	Iterable<FileInfo> findById(long id);
	
	FileInfo findByFileName(String fileName);
}