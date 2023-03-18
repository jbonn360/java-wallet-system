package com.betting.javawalletsystem.exception;

public class TransactionExistsException extends RuntimeException{
    public TransactionExistsException(String message){
        super(message);
    }
}
