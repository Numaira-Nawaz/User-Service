package com.example.userservice.services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.DTOs.UserMapper;
import com.example.userservice.entities.Users;
import com.example.userservice.exceptions.ApiResponse;
import com.example.userservice.exceptions.Custom.ResourceNotFoundException;
import com.example.userservice.exceptions.Custom.UserAlreadyExit;
import com.example.userservice.repositories.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.userservice.DTOs.UserMapper.entityToDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void testAddUser_Successful() throws UserAlreadyExit {
        Mockito.when(userRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        Users savedUser = new Users(1L,"John", "Doe");
        Mockito.when(userRepo.save(Mockito.any(Users.class))).thenReturn(savedUser);
        UserDTO userDTO = entityToDTO(savedUser);
        ResponseEntity<Users> response = userServiceImpl.addUser(userDTO);

        // Assert the response status code and body
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(savedUser, response.getBody());
    }

    @Test
    void testAddUser_UserNameFound() {
        // Mock the findByName method to return an existing user
        Users existingUser = new Users(1L,"John", "Doe");
        Mockito.when(userRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(Mockito.anyString(), Mockito.anyString())).thenReturn(existingUser);
        // Create an instance of the class under test
        UserDTO userDTO = entityToDTO(existingUser);
        // Assert that the UserNameFound exception is thrown
        Assertions.assertThrows(UserAlreadyExit.class, () -> userServiceImpl.addUser(userDTO));
    }
    @Test
    void given_ExistingUser_when_AddUser_then_ThrowUserNameFoundException() {

        UserDTO userDTO = new UserDTO(1L,"Numaira","Nawaz");


        when(userServiceImpl.findByName(Mockito.anyString(), Mockito.anyString())).thenReturn(new Users());

        assertThrows(UserAlreadyExit.class, () -> userServiceImpl.addUser(userDTO));
    }

    @Test
    void given_ExistingUserId_when_FindUserById_then_ReturnUser() {

        Long userId = 1L;
        UserDTO user = new UserDTO(userId,"Numaira","Nawaz");

        Users user1 = UserMapper.dtoToEntity(user);

        when(userRepo.findById(userId)).thenReturn(Optional.ofNullable(user1));

        UserDTO response = userServiceImpl.findUserById(userId);
        assertEquals(user, response);
    }

    @Test
    void given_NonExistingUserId_when_FindUserById_then_ThrowResourceNotFoundException() {
        // Arrange
        Long userId = 1L;

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findUserById(userId));
    }
    @Test
    void delete_ValidUserId_DeletesUser() {
        Long userId = 1L;
        Users user = new Users(userId,"Numaira","Nawaz");

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        ResponseEntity<Object> response = userServiceImpl.delete(userId);

        verify(userRepo).delete(user);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testDeleteUser_UserNotFound() {

        Long userId = 1L;
        when(userServiceImpl.delete(userId)).thenThrow(ResourceNotFoundException.class);

        ResponseEntity<Object> result = userServiceImpl.delete(userId);

        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals("No User Found with the ID: " + userId, ((ApiResponse) result.getBody()).getMessage());

    }
    @Test
    public void testFindAll_Success() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setFirstName("Numaira");
        user1.setLastName("Nawaz");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setFirstName("Monisa");
        user2.setLastName("Alvi");
        List<Users> usersList = Arrays.asList(user1,user2);

        when(userServiceImpl.findAll()).thenReturn(usersList);
        List<Users> users = userServiceImpl.findAll();

        assertEquals(2,users.size());
    }

}
