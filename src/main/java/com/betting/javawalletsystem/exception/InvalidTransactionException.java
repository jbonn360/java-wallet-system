package com.betting.javawalletsystem.exception;

public class InvalidTransactionException extends RuntimeException{
    public InvalidTransactionException(String message){
        super(message);
    }
}
