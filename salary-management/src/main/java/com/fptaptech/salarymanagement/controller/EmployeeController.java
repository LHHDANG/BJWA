package com.fptaptech.salarymanagement.controller;

import com.fptaptech.salarymanagement.model.Employee;
import com.fptaptech.salarymanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Hiển thị danh sách nhân viên
    @GetMapping({"/", "/employees"})
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("employee", new Employee());
        return "employee-list";
    }

    // Thêm nhân viên mới
    @PostMapping("/employees")
    public String saveEmployee(@Valid @ModelAttribute Employee employee,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            List<Employee> employees = employeeService.getAllEmployees();
            model.addAttribute("employees", employees);
            model.addAttribute("showAddForm", true);
            return "employee-list";
        }

        try {
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/employees";
    }

    // Hiển thị form sửa
    @GetMapping("/employees/edit/{id}")
    public String editEmployee(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);

        if (employee.isPresent()) {
            List<Employee> employees = employeeService.getAllEmployees();
            model.addAttribute("employees", employees);
            model.addAttribute("employee", employee.get());
            model.addAttribute("isEdit", true);
            return "employee-list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên!");
            return "redirect:/employees";
        }
    }

    // Cập nhật nhân viên
    @PostMapping("/employees/update")
    public String updateEmployee(@Valid @ModelAttribute Employee employee,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            List<Employee> employees = employeeService.getAllEmployees();
            model.addAttribute("employees", employees);
            model.addAttribute("isEdit", true);
            return "employee-list";
        }

        try {
            employeeService.updateEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật nhân viên thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/employees";
    }

    // Xóa nhân viên
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa nhân viên thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/employees";
    }

    // Tìm kiếm nhân viên
    @GetMapping("/employees/search")
    public String searchEmployees(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Integer minAge,
                                  @RequestParam(required = false) Integer maxAge,
                                  @RequestParam(required = false) Double minSalary,
                                  @RequestParam(required = false) Double maxSalary,
                                  Model model) {

        List<Employee> employees;

        if (name != null && !name.trim().isEmpty()) {
            employees = employeeService.searchEmployeesByCriteria(name.trim(), minAge, maxAge, minSalary, maxSalary);
        } else if (minAge != null || maxAge != null || minSalary != null || maxSalary != null) {
            employees = employeeService.searchEmployeesByCriteria(null, minAge, maxAge, minSalary, maxSalary);
        } else {
            employees = employeeService.getAllEmployees();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("employee", new Employee());
        model.addAttribute("searchName", name);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        model.addAttribute("minSalary", minSalary);
        model.addAttribute("maxSalary", maxSalary);

        return "employee-list";
    }

    // Reset form
    @GetMapping("/employees/reset")
    public String resetForm() {
        return "redirect:/employees";
    }
}