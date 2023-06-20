package com.example.userservice.services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.DTOs.UserMapper;
import com.example.userservice.entities.Users;
import com.example.userservice.exceptions.ApiResponse;
import com.example.userservice.exceptions.Custom.ResourceNotFoundException;
import com.example.userservice.exceptions.Custom.UserAlreadyExit;
import com.example.userservice.repositories.UserRepo;
import com.example.userservice.services.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.userservice.DTOs.UserMapper.dtoToEntity;
import static com.example.userservice.DTOs.UserMapper.entityToDTO;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepo userRepo;
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    //add-user
    public ResponseEntity<Users> addUser(UserDTO userDTO) throws UserAlreadyExit {
        Users user = dtoToEntity(userDTO);
        Users exist = findByName(user.getFirstName(),user.getLastName());
        if (exist == null) {
             Users userAdded = userRepo.save(user);
             //UserDTO userDTO_added = entityToDTO(userAdded);
            return ResponseEntity.status(HttpStatus.CREATED).body(userAdded);
        }
        throw new UserAlreadyExit(user.getFirstName()+" "+user.getLastName());
    }

    //find-all
    public List<Users> findAll(){
        List<Users> usersList = userRepo.findAll();
        Collections.sort(usersList);
        return ResponseEntity.of(Optional.of(usersList)).getBody();
    }

    //find - by - id
    public UserDTO findUserById(Long id){

        try {
            Users user = userRepo.findById(id).get();
            UserDTO userDTO = entityToDTO(user);
            return ResponseEntity.ok(userDTO).getBody();
        } catch (Exception e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public Users findByName(String fName,String lName){
        return userRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(fName,lName);
    }
    //update - user
    public UserDTO update(Long id, UserDTO userDTO) throws UserAlreadyExit {
        UserDTO user1 = findUserById(id);
        user1.setFirstName(userDTO.getFirstName());
        user1.setLastName(userDTO.getLastName());
        Users exist = findByName(userDTO.getFirstName(),userDTO.getLastName());
        if (exist==null) {
            Users user = UserMapper.dtoToEntity(user1);
            Users usersUpdated = userRepo.save(user);
            UserDTO userDTOUpdated =  entityToDTO(usersUpdated);
            return ResponseEntity.of(Optional.of(userDTOUpdated)).getBody();
        }else {
            throw new UserAlreadyExit(userDTO.getFirstName() + " " + userDTO.getLastName());
        }
    }

    //Delete-a-User
    public ResponseEntity<Object> delete(Long id){

        try{
            UserDTO userDTO = findUserById(id);
            Users deleted  = dtoToEntity(userDTO);
            userRepo.delete(deleted);
            return ResponseEntity.of(Optional.of(deleted));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("No User Found with the ID: "+id,false),HttpStatus.ACCEPTED);
        }
    }


}
