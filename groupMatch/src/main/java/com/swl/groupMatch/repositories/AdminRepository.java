package com.swl.groupMatch.repositories;

import com.swl.groupMatch.documents.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AdminRepository extends MongoRepository<Admin, Object> {
    @Query("{'_id': {'user': ?0 }}")
    Admin findAdminByUser(String user);

    @Query("{'_id': {'user': ?0 }, 'pw': ?1}")
    Admin authAdmin(String user, String pw);
}
