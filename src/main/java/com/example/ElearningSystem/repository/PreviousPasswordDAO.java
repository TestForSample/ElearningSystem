package com.example.ElearningSystem.repository;

import com.example.ElearningSystem.model.PreviousPasswords;
import com.example.ElearningSystem.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreviousPasswordDAO extends JpaRepository<PreviousPasswords,Integer>{

    @Query(value = "select * from previous_passwords where created_by=:username",nativeQuery = true)
     List<PreviousPasswords> findByCreatedBy(@Param("username") String username);


}
