package com.rs.EmployeeService.domain.exception;

public class ManagerSubordinatesLimitExceededException extends RuntimeException {
    public ManagerSubordinatesLimitExceededException(String message){
        super(message);
    }
}
