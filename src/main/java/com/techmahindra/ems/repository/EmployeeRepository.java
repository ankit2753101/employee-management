package com.techmahindra.ems.repository;

import com.techmahindra.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Employee entity.
 * Demonstrates JPA derived queries and JPQL custom queries.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find by email (used for duplicate-check and lookup).
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Check if email is already registered.
     */
    boolean existsByEmail(String email);

    /**
     * Find all employees belonging to a specific department.
     */
    List<Employee> findByDepartmentId(Long departmentId);

    /**
     * Full-text search: finds employees whose first or last name contains the given keyword.
     * Uses JPQL LOWER() for case-insensitive matching.
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchByName(@Param("keyword") String keyword);

    /**
     * Paginated list of active employees.
     */
    Page<Employee> findByActiveTrue(Pageable pageable);

    /**
     * Count employees in a given department.
     */
    long countByDepartmentId(Long departmentId);
}
