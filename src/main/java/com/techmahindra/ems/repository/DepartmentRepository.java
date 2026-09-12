package com.techmahindra.ems.repository;

import com.techmahindra.ems.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Department entity.
 * Extends JpaRepository – provides built-in CRUD + pagination methods.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Find a department by its name (case-insensitive).
     */
    Optional<Department> findByNameIgnoreCase(String name);

    /**
     * Check if a department with the given name exists (used to prevent duplicates).
     */
    boolean existsByNameIgnoreCase(String name);
}
