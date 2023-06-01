package com.example.userandroles.Controller;

import com.example.userandroles.CustomExceptions.ApiResponse;
import com.example.userandroles.CustomExceptions.ResourceNotFoundException;
import com.example.userandroles.CustomExceptions.UserNameFound;
import com.example.userandroles.DTO.UserDTO;
import com.example.userandroles.Entities.Users;
import com.example.userandroles.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody UserDTO user) throws UserNameFound {
            Users exist = userService.findByName(user.getFirstName(),user.getLastName());
        if (exist==null) {
            Users add = userService.addUser(user);
            return  ResponseEntity.status(HttpStatus.CREATED).body(add);
        }
          throw new UserNameFound(user.getFirstName()+" "+user.getLastName());

    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> update(@PathVariable Long id, @RequestBody UserDTO user) throws UserNameFound {
        Users update;
            findUser(id);
            Users exist = userService.findByName(user.getFirstName(),user.getLastName());
                if (exist==null) {
                    update = userService.update(id, user);
                    return ResponseEntity.of(Optional.of(update));
                }else {
                    throw new UserNameFound(user.getFirstName() + " " + user.getLastName());
                }
    }

    //GET-USER
    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable  Long id){
        Users user;
        try {
           user = userService.findUserById(id);
           return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException(id);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Users deleted;
        try{
             findUser(id);
             deleted = userService.delete(id);
            return ResponseEntity.of(Optional.of(deleted));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("No User Found with the ID: "+id,false),HttpStatus.ACCEPTED);
        }

    }

    @GetMapping("/allUser")
    public ResponseEntity<List<Users>> findUser(){
        List<Users> usersList = userService.findAll();
        Collections.sort(usersList);
        return ResponseEntity.of(Optional.of(usersList));
    }
}
