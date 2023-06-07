package com.example.userservice.controllers;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.entities.Users;
import com.example.userservice.exceptions.Custom.UserAlreadyExit;
import com.example.userservice.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping
    public ResponseEntity<Users> addUser(@Valid @RequestBody UserDTO user) throws UserAlreadyExit {
           return userServiceImpl.addUser(user);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable  Long id){
        return userServiceImpl.findUserById(id);
    }

    @GetMapping("/allUser")
    public List<Users> getUsers(){
        return userServiceImpl.findAll();
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO user) throws UserAlreadyExit {
       return userServiceImpl.update(id,user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userServiceImpl.delete(id);
    }

}
