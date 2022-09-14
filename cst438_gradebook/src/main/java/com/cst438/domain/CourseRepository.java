package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends CrudRepository <Course, Integer> {
	
	@Query("select c from Course c where c.course_id = :courseID")
	Course findByCourseID(@Param("courseID") int courseID);

}
