package com.swl.groupMatch.repositories;

import com.swl.groupMatch.documents.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, Object> {

    @Query("{'_id': {'email': ?0 }}")
    Student findStudentByEmail(String email);

    @Query(value="{'studentName': ?0 }", fields="{'email' : 1}")
    List<Student> findAll(String name);

    @Query("{'_id': {'email': ?0 }, 'pw': ?1}")
    Student authStudent(String email, String pw);

    public long count();

}