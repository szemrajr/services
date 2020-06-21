package com.rs.EmployeeService.domain.exception;

public class DirectorLimitExceededException extends RuntimeException{
    public DirectorLimitExceededException(String message){
        super(message);
    }
}
