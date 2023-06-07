package com.example.userservice.repositories;

import com.example.userservice.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    //@Query("SELECT u FROM Users u WHERE LOWER(u.firstName) = LOWER(:firstName) AND LOWER(u.lastName) = LOWER(:lastName)")
    Users findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    public Users getById(Long id);
}