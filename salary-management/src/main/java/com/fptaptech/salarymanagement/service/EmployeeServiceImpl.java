package com.fptaptech.salarymanagement.service;

import com.fptaptech.salarymanagement.model.Employee;
import com.fptaptech.salarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        if (existsByName(employee.getName())) {
            throw new RuntimeException("Tên nhân viên đã tồn tại: " + employee.getName());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (existsByNameAndNotId(employee.getName(), employee.getId())) {
            throw new RuntimeException("Tên nhân viên đã tồn tại: " + employee.getName());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy nhân viên với ID: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> searchEmployeesByCriteria(String name, Integer minAge, Integer maxAge,
                                                    Double minSalary, Double maxSalary) {
        return employeeRepository.findEmployeesByCriteria(name, minAge, maxAge, minSalary, maxSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return employeeRepository.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndNotId(String name, Long id) {
        Optional<Employee> existing = employeeRepository.findByNameIgnoreCase(name);
        return existing.isPresent() && !existing.get().getId().equals(id);
    }
}