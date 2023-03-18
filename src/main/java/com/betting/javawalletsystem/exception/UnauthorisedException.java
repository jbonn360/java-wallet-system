package com.betting.javawalletsystem.exception;

public class UnauthorisedException extends RuntimeException{
    public UnauthorisedException(String message){
        super(message);
    }
}
