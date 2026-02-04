package com.job.exception;

public class UserAlreadyExistException extends AuthServiceException{

    public UserAlreadyExistException(String message){
        super(message);
    }
}
