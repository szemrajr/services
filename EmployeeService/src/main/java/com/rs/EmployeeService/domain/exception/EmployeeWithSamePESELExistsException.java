package com.rs.EmployeeService.domain.exception;

public class EmployeeWithSamePESELExistsException extends RuntimeException {
    public EmployeeWithSamePESELExistsException(String message){
        super(message);
    }
}
