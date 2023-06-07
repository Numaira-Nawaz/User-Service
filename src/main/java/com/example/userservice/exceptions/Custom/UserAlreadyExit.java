package com.example.userservice.exceptions.Custom;

public class UserAlreadyExit extends Exception{
    public UserAlreadyExit(String name){
        super(String.format(name+" already Found. TRY ANOTHER."));
    }

}
