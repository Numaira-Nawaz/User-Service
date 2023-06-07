package com.example.userservice.services.Interfaces;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.entities.Users;
import com.example.userservice.exceptions.Custom.UserAlreadyExit;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    public ResponseEntity<Users> addUser(UserDTO userDTO) throws UserAlreadyExit;
    public List<Users> findAll();
    public Users findByName(String fName,String lName);
    public UserDTO update(Long id, UserDTO user) throws UserAlreadyExit;
    public ResponseEntity<?> delete(Long id);
}
