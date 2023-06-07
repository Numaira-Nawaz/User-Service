package com.example.userservice.controllers;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.DTOs.UserMapper;
import com.example.userservice.entities.Users;
import com.example.userservice.exceptions.ApiResponse;
import com.example.userservice.exceptions.Custom.ResourceNotFoundException;
import com.example.userservice.exceptions.Custom.UserAlreadyExit;
import com.example.userservice.services.UserServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    @MockBean
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;
    @Autowired
    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper();


    @Test
    public void given_inValidCredentials_unauthorized_response() throws Exception {
        MvcResult result = mvc.perform(get("/v1/user/allUser")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user24", "322pass")).contentType(APPLICATION_JSON))
                .andReturn();
        Assertions.assertEquals(MockHttpServletResponse.SC_UNAUTHORIZED, result.getResponse().getStatus());

    }

    @Test
    void addUser_Should_ReturnCreatedStatus() throws Exception {
        Users user = new Users(1L, "Numaira", "Nawaz");
        UserDTO userDTO = new UserDTO(1L, "Numaira", "Nawaz");
        String jsonRequest = om.writeValueAsString(user);
        when(userServiceImpl.addUser(userDTO)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(user));

        MvcResult result = mvc.perform(post("/v1/user")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user","pass"))
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertEquals(jsonRequest,result.getResponse().getContentAsString());
        // Verify the response status code
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddUser_Should_return_UserAlreadyExist() throws Exception {
        Users user = new Users(1L, "Numaira", "Nawaz");

        UserDTO add = new UserDTO(2L, "Numaira", "Nawaz");
        String jsonRequest = om.writeValueAsString(add);

        when(userServiceImpl.findByName(add.getFirstName(), add.getLastName())).thenReturn(user);
        when(userServiceImpl.addUser(add)).thenThrow(new UserAlreadyExit(user.getFirstName()+" "+user.getLastName()));
        MvcResult result = mvc.perform(post("/v1/user")
                        .with(httpBasic("user","pass"))
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse response = om.readValue(jsonResponse, ApiResponse.class);

        assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
        assertEquals(user.getFirstName()+" "+user.getLastName()+" already Found. TRY ANOTHER.",response.getMessage());
    }

    @Test
    public void should_FindUser_Successfully() throws Exception {
        Users user = new Users(1L, "Numaira", "Nawaz");
        UserDTO userDTO = UserMapper.entityToDTO(user);
        when(userServiceImpl.findUserById(1L)).thenReturn(userDTO);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user/{id}", 1L)
                        .with(httpBasic("user","pass"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Users responseUser = om.readValue(jsonResponse, Users.class);

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
    }

    @Test
    public void given_NotFoundException_when_testFindUser() throws Exception {
        Long id = 4L;
        when(userServiceImpl.findUserById(id)).thenThrow(new ResourceNotFoundException(id));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user/{id}", id)
                        .with(httpBasic("user","pass"))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse response = om.readValue(jsonResponse, ApiResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("User Not Found with the id: " +id,response.getMessage());

    }


    @Test
    public void given_statusOK_when_DeleteUser() throws Exception {

        Users user = new Users(2L, "Numaira", "Nawaz");
        when(userServiceImpl.delete(2L)).thenReturn(ResponseEntity.of(Optional.of(user)));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/v1/user/{id}", 2L)
                        .with(httpBasic("user", "pass"))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Users responseUser = om.readValue(jsonResponse, Users.class);

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
    }

    @Test
    public void given_UserNotFound_when_DeleteUser() throws Exception {
        Long userId = 1L;

        when(userServiceImpl.delete(userId)).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("User Not Found with the id: " + userId));

        MvcResult result = mvc.perform(delete("/v1/user/{id}", userId)
                        .with(httpBasic("user","pass")))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        assertEquals(HttpStatus.ACCEPTED.value(),result.getResponse().getStatus());
        assertEquals("User Not Found with the id: " + userId, jsonResponse);
    }

    @Test
    public void should_return_all_users_successfully() throws Exception {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users(1L, "Nimra", "Ghumman"));
        usersList.add(new Users(2L, "Isha", "Haram"));

        when(userServiceImpl.findAll()).thenReturn(usersList);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user/allUser")
                        .with(httpBasic("user","pass")))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<Users> responseUsersList = om.readValue(jsonResponse, new TypeReference<List<Users>>(){});

        assertEquals(usersList.size(), responseUsersList.size());

    }


}