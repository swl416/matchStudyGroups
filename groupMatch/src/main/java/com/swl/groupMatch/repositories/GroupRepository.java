package com.swl.groupMatch.repositories;

import com.swl.groupMatch.documents.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, Object> {

    @Query("{'students.email': ?0 }")
    List<Group> findStudentGroups(String email);

    @Query("{'_id': {'groupName': ?0, 'semester': ?1 }}")
    Group searchGroup(String groupName, String semester);

    public long count();

}