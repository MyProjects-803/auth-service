package com.job.exception;

public class InvalidCredentialException extends AuthServiceException{

    public InvalidCredentialException(String msg){
        super(msg);
    }
}
