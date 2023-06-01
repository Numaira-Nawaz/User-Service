package com.example.userservice.CustomExceptions;

public class ResourceNotFoundException extends RuntimeException{
    Long id;
    public ResourceNotFoundException(){
        super(String.format("No user Found"));
    }
    public ResourceNotFoundException(Long id){
        super(String.format("User Not Found with the id: " +id));
        this.id=id;
    }
    public ResourceNotFoundException(String message){
        super(String.format(message));
    }

}
