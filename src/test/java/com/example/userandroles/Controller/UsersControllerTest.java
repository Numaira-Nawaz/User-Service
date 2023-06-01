package com.example.userandroles.Controller;

import com.example.userandroles.CustomExceptions.ApiResponse;
import com.example.userandroles.CustomExceptions.ResourceNotFoundException;
import com.example.userandroles.DTO.UserDTO;
import com.example.userandroles.Entities.Users;
import com.example.userandroles.Service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UsersControllerTest {

    @MockBean
    private UserService userService;

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
    void addUser_ShouldReturnCreatedStatus() throws Exception {

        Users user = new Users(1L, "Numaira", "Nawaz");
        String jsonRequest = om.writeValueAsString(user);

        MvcResult result = mvc.perform(post("/v1/user")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user","pass"))
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated()) .andReturn();

        // Verify the response status code
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddUser_UserNameFound() throws Exception {
        Users user = new Users(1L, "Numaira", "Nawaz");

        UserDTO add = new UserDTO(2L, "Numaira", "Nawaz");
        String jsonRequest = om.writeValueAsString(add);

        when(userService.findByName(add.getFirstName(), add.getLastName())).thenReturn(user);

        MvcResult result = mvc.perform(post("/v1/user")
                        .with(httpBasic("user","pass"))
                        .content(jsonRequest)
                        .contentType(APPLICATION_JSON))
                .andReturn();

        assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());

    }

    @Test
    public void testFindUser_Success() throws Exception {
        Users user = new Users(1L, "Numaira", "Nawaz");

        when(userService.findUserById(1L)).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user/{id}", 1L)
                        .with(httpBasic("user","pass"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        String jsonResponse = result.getResponse().getContentAsString();
        Users responseUser = om.readValue(jsonResponse, Users.class);
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
    }

    @Test
    public void testFindUser_NotFoundException() throws Exception {
        Long id = 4L;
        when(userService.findUserById(id)).thenThrow(ResourceNotFoundException.class);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user/{id}", id)
                        .with(httpBasic("user","pass"))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse response = om.readValue(jsonResponse, ApiResponse.class);
        assertEquals("User Not Found with the id: " +id,response.getMessage());

    }


    @Test
    public void testDeleteUser_Success() throws Exception {

        Users user = new Users(2L, "Numaira", "Nawaz");
        when(userService.delete(2L)).thenReturn(user);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/v1/user/{id}", 2L)
                        .with(httpBasic("user", "pass"))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println(jsonResponse);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        Users responseUser = om.readValue(jsonResponse, Users.class);
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
    }

    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        Long userId = 1L;

        when(userService.findUserById(userId)).thenThrow(new ResourceNotFoundException(userId));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/v1/user/{id}", userId)
                        .with(httpBasic("user","pass")))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse response = om.readValue(jsonResponse, ApiResponse.class);
        assertEquals("No User Found with the ID: " + userId, response.getMessage());
        assertFalse(response.isSuccess());
    }

    @Test
    public void testFindAllUsers_Success() throws Exception {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users(1L, "Nimra", "Ghumman"));
        usersList.add(new Users(2L, "Isha", "Haram"));

        when(userService.findAll()).thenReturn(usersList);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/v1/user/allUser")
                        .with(httpBasic("user","pass")))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<Users> responseUsersList = om.readValue(jsonResponse, new TypeReference<List<Users>>(){});
        assertEquals(usersList.size(), responseUsersList.size());

    }


}