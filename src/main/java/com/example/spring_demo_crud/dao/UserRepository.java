package com.example.spring_demo_crud.dao;

import com.example.spring_demo_crud.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = "travels")
    Optional findWithTravelsById(int id);
}


