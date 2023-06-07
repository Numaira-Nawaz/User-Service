package com.example.userservice.DTOs;

import com.example.userservice.entities.Users;

public class UserMapper {

    public static UserDTO entityToDTO(Users users) {
        return UserDTO.builder()
                .id(users.getId())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .build();
    }

    public static Users dtoToEntity(UserDTO userDTO) {
        return Users.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .build();
    }

}
