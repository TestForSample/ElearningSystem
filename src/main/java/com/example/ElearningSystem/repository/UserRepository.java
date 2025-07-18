package com.example.ElearningSystem.repository;

import com.example.ElearningSystem.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query(value = "Select * from users where username=:username", nativeQuery = true)
    Users findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "delete from users where username=:username", nativeQuery = true)
    void deleteTheUsersByUsername(@Param("username") String username);

}
