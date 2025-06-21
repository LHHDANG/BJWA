package com.fptaptech.salarymanagement.service;

import com.fptaptech.salarymanagement.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(Long id);
    List<Employee> searchEmployeesByName(String name);
    List<Employee> searchEmployeesByCriteria(String name, Integer minAge, Integer maxAge,
                                             Double minSalary, Double maxSalary);
    boolean existsByName(String name);
    boolean existsByNameAndNotId(String name, Long id);
}