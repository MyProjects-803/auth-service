package com.job.exception;

public class RoleNotFoundException extends AuthServiceException {

    public RoleNotFoundException(String msg){
        super(msg);
    }
}
