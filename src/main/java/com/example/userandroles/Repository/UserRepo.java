package com.example.userandroles.Repository;

import com.example.userandroles.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    //@Query("SELECT u FROM Users u WHERE LOWER(u.firstName) = LOWER(:firstName) AND LOWER(u.lastName) = LOWER(:lastName)")
    Users findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
