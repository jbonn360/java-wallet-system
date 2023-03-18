package com.betting.javawalletsystem.exception;

public class PlayerNotFoundException extends RuntimeException{
    public PlayerNotFoundException(String message){
        super(message);
    }
}
