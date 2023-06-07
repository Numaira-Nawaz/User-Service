package com.example.userservice.exceptions.Custom;

public class ResourceNotFoundException extends RuntimeException{
    Long id;
    public ResourceNotFoundException(Long id){
        super(String.format("User Not Found with the id: " +id));
        this.id=id;
    }

}
