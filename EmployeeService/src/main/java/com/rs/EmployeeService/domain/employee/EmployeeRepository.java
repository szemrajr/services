package com.rs.EmployeeService.domain.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findBySuperiorId(Long id);
    Employee findByPESEL(String PESEL);
    Long countBySuperiorId(Long id);
    Long countByRole(Role role);
 }
