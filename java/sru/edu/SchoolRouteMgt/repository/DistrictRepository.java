package sru.edu.SchoolRouteMgt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.User;


public interface DistrictRepository extends JpaRepository<District, Long> {
	
	@Query("Select d FROM District d WHERE d.districtName = ?1")
	
	public District findByName(String districtName);
	
}
