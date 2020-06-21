package com.rs.EmployeeService.domain.employee;

import com.rs.EmployeeService.domain.exception.DirectorLimitExceededException;
import com.rs.EmployeeService.domain.exception.EmployeeWithSamePESELExistsException;
import com.rs.EmployeeService.domain.exception.ManagerSubordinatesLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Long SUBORDINATES_MANAGER_LIMIT = 5L;
    private static final Long DIRECTOR_LIMIT = 5L;
    private static final String PESEL_EXISTS_MESSAGE = "Employee with this PESEL: %s already exists";
    private static final String DIRECTOR_LIMIT_MESSAGE = "There is already 5 employee with director role";
    private static final String USER_WITH_ID_NOT_EXISTS_MESSAGE = "Employee with given id: %d doesn't exist";
    private static final String SUBORDINATES_MANAGER_LIMIT_MESSAGE = "This manager already have 5 subordinates";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional()
    public Employee save(Employee employee) {
        validate(employee);
        return employeeRepository.save(employee);
    }

    private void validate(Employee employee) {
        String PESEL = employee.getPESEL();
        Employee employeeFromDB = employeeRepository.findByPESEL(PESEL);
        if (employeeFromDB != null && employee.getId() != null) {
            ifEmployeeWithGivenPESELCanUpdate(employeeFromDB, employee);
        } else if (employeeFromDB != null) {
            throw new EmployeeWithSamePESELExistsException(String.format(PESEL_EXISTS_MESSAGE, PESEL));
        }

        if (employee.getRole().equals(Role.DIRECTOR)) {
            Long directorOccurNumber = employeeRepository.countByRole(Role.DIRECTOR);
            if (directorOccurNumber >= DIRECTOR_LIMIT) {
                throw new DirectorLimitExceededException(DIRECTOR_LIMIT_MESSAGE);
            }
        }
    }

    private void ifEmployeeWithGivenPESELCanUpdate(Employee userFromDB, Employee userToUpdate) {
        if (!userFromDB.getId().equals(userToUpdate.getId())) {
            throw new EmployeeWithSamePESELExistsException(String.format(PESEL_EXISTS_MESSAGE, userToUpdate.getPESEL()));
        }
    }

    @Transactional(readOnly = true)
    public Employee get(Long id) {
        Optional<Employee> optEmployee = employeeRepository.findById(id);
        return optEmployee.orElseThrow(() -> new IllegalArgumentException(String.format(USER_WITH_ID_NOT_EXISTS_MESSAGE, id)));
    }

    @Transactional(readOnly = true)
    public List<Employee> findBySuperiorId(Long id) {
        return employeeRepository.findBySuperiorId(id);
    }

    @Transactional
    public Employee setSuperior(Long employeeId, Long superiorId) {
        Employee superior = get(superiorId);
        if (superior.getRole().equals(Role.MANAGER)) {
            checkManagerSubordinatesLimit(superiorId);
        }
        Employee employee = get(employeeId);
        employee.setSuperior(superior);
        return save(employee);
    }

    private void checkManagerSubordinatesLimit(Long superiorId) {
        Long superiorOccurNumber = employeeRepository.countBySuperiorId(superiorId);
        if (superiorOccurNumber >= SUBORDINATES_MANAGER_LIMIT) {
            throw new ManagerSubordinatesLimitExceededException(SUBORDINATES_MANAGER_LIMIT_MESSAGE);
        }
    }
}
