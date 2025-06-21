package com.fptaptech.salarymanagement.repository;

import com.fptaptech.salarymanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Tìm kiếm theo tên (không phân biệt hoa thường)
    List<Employee> findByNameContainingIgnoreCase(String name);

    // Tìm kiếm theo khoảng lương
    List<Employee> findBySalaryBetween(Double minSalary, Double maxSalary);

    // Tìm kiếm theo tuổi
    List<Employee> findByAgeBetween(Integer minAge, Integer maxAge);

    // Kiểm tra tên đã tồn tại chưa
    boolean existsByNameIgnoreCase(String name);

    // Tìm theo tên chính xác
    Optional<Employee> findByNameIgnoreCase(String name);

    // Custom query tìm kiếm đa điều kiện
    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:minAge IS NULL OR e.age >= :minAge) AND " +
            "(:maxAge IS NULL OR e.age <= :maxAge) AND " +
            "(:minSalary IS NULL OR e.salary >= :minSalary) AND " +
            "(:maxSalary IS NULL OR e.salary <= :maxSalary)")
    List<Employee> findEmployeesByCriteria(
            @Param("name") String name,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary
    );
}