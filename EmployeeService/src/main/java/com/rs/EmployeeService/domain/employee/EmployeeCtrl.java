package com.rs.EmployeeService.domain.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmployeeCtrl {

    @Autowired
    private Environment environment;

    @Autowired
    private EmployeeService employeeService;

    static class SuperiorId{
        private Long superior;

        public Long getSuperior() {
            return superior;
        }

        public void setSuperior(Long superior) {
            this.superior = superior;
        }
    }

    @PostMapping("/employees")
    public Employee save(@Valid @RequestBody Employee employee){
        return employeeService.save(employee);
    }

    @PutMapping("/employees")
    public Employee update(@Valid @RequestBody Employee employee){
        return employeeService.save(employee);
    }

    @GetMapping("/employees/{id}")
    public Employee get(@PathVariable Long id){
        return employeeService.get(id);
    }

    @PostMapping("/employees/{id}/superior")
    public Employee setSuperior(@PathVariable Long id, @RequestBody SuperiorId superiorId){
        return employeeService.setSuperior(id, superiorId.superior);
    }

    @GetMapping("/employees/{id}/superior")
    public Employee getSuperior(@PathVariable Long id){
        Employee employee = employeeService.get(id);
        return employee.getSuperior();
    }

    @GetMapping("/employees/{id}/subordinates")
    public List<Employee> getSubordinates(@PathVariable Long id){
        return employeeService.findBySuperiorId(id);
    }
}
