package com.swl.groupMatch.repositories;

import com.swl.groupMatch.documents.Student;
import com.swl.groupMatch.documents.Takes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TakesRepository extends MongoRepository<Takes, Object> {

    @Query("{'_id.email': ?0 }")
    List<Takes> findStudentTakes(String email);

    @Query(value="{'courses': { 'loc': ?0, 'courseName': ?1, 'startDT': ?2, 'days': ?3 } }", fields="{'email' : 1, 'semester': 2, 'studentName': 3}" )
    List<Object> findAllStudentsOfCourse(String loc, String courseName, String startDT, String days);

    @Query("{'_id': {'email': ?0 , 'semester': ?1}}")
    Takes findTakesWithTakesId(String email, String semester);

    @Query(value="{'_id.email': ?0 }", fields="{'courses':1}")
    List<HashMap<String, String>> findStudentCourses(String email);

    public long count();

}