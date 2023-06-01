package com.example.userandroles.CustomExceptions;

public class UserNameFound extends Exception{
    public UserNameFound(String name){
        super(String.format(name+" already Found. TRY ANOTHER."));
    }

}
