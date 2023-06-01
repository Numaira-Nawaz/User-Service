package com.example.userservice.Services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Entities.Users;
import com.example.userservice.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  UserService {
    @Autowired
    private UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    //add-user
    public Users addUser(UserDTO userDTO) {
        Users user = new Users();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        return userRepo.save(user);
    }

    //find-all
    public List<Users> findAll(){
       return userRepo.findAll();
    }

    //find - by - id
    public Users findUserById(Long id){
        Users user = userRepo.findById(id).get();
        return user;
    }

    public Users findByName(String fName,String lName){
        return userRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(fName,lName);
    }
    //update - user
    public Users update(Long id, UserDTO user){
        Users user1 = findUserById(id);
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        return userRepo.save(user1);
    }

    //Delete-a-User
    public Users delete(Long id){
        Users user = userRepo.findById(id).get();
        userRepo.delete(user);
        return user;
    }


}
