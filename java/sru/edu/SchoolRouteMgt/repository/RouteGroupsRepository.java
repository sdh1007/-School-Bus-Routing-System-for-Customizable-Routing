package sru.edu.SchoolRouteMgt.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import sru.edu.SchoolRouteMgt.domain.District;
import sru.edu.SchoolRouteMgt.domain.RouteGroups;
import sru.edu.SchoolRouteMgt.domain.Schools;


public interface RouteGroupsRepository extends JpaRepository<RouteGroups, Long> {
	List<RouteGroups> findByDistrict(District district);
	Iterable<RouteGroups> findByGroupId(String groupId);
	Set<RouteGroups> findBySelectSchools(Schools school);
}