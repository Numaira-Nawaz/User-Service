package com.example.userservice.Services;

import com.example.userservice.Controllers.UserController;
import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Entities.Users;
import com.example.userservice.Repositories.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @InjectMocks
    private UserController userController;

    @Test
    public void testAddUser_Success() {

        UserDTO user = new UserDTO();
        user.setFirstName("Numaira");
        user.setLastName("Nawaz");
        Users savedUser = new Users(1L,"Numaira","Nawaz");
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(savedUser);

        Users response = userService.addUser(user);
        assertEquals("Numaira",response.getFirstName());

    }

    @Test
    void findByName_Success() {
        Users user = new Users(1L,"Numaira", "Nawaz");

        when(userService.findByName("Numaira", "Nawaz")).thenReturn(user);

        Users result = userService.findByName("Numaira", "Nawaz");

        assertEquals("Numaira",result.getFirstName());
        Assertions.assertEquals(user, result);
    }

    @Test
    void findByName_NotFound() {
        Users result = userService.findByName("Numaira", "Nawaz");
        Assertions.assertNull(result);
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

        when(userService.findAll()).thenReturn(usersList);
        List<Users> users = userService.findAll();

        assertEquals(2,users.size());
        assertEquals("Numaira",users.get(0).getFirstName());
    }

}
