package com.techmahindra.ems.service;

import com.techmahindra.ems.dto.DepartmentDto;
import com.techmahindra.ems.exception.ResourceNotFoundException;
import com.techmahindra.ems.model.Department;
import com.techmahindra.ems.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Department business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // ─── Create ───────────────────────────────────────────────────────────────

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto dto) {
        log.info("Creating department: {}", dto.getName());

        if (departmentRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException(
                    "Department with name '" + dto.getName() + "' already exists.");
        }

        Department department = Department.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .build();

        Department saved = departmentRepository.save(department);
        log.info("Department created with ID: {}", saved.getId());
        return mapToDto(saved, false);
    }

    // ─── Read All ─────────────────────────────────────────────────────────────

    public List<DepartmentDto> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll()
                .stream()
                .map(d -> mapToDto(d, false))
                .collect(Collectors.toList());
    }

    // ─── Read One (with employees) ────────────────────────────────────────────

    public DepartmentDto getDepartmentById(Long id) {
        log.info("Fetching department with ID: {}", id);
        Department department = findDepartmentOrThrow(id);
        return mapToDto(department, true);
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentDto dto) {
        log.info("Updating department with ID: {}", id);
        Department department = findDepartmentOrThrow(id);

        // Check name uniqueness only if name is being changed
        if (!department.getName().equalsIgnoreCase(dto.getName())
                && departmentRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException(
                    "Department with name '" + dto.getName() + "' already exists.");
        }

        department.setName(dto.getName());
        department.setLocation(dto.getLocation());
        department.setDescription(dto.getDescription());

        return mapToDto(departmentRepository.save(department), false);
    }

    // ─── Delete ───────────────────────────────────────────────────────────────

    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with ID: {}", id);
        Department department = findDepartmentOrThrow(id);

        if (!department.getEmployees().isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot delete department '" + department.getName()
                    + "' because it still has " + department.getEmployees().size() + " employee(s). "
                    + "Reassign or remove the employees first.");
        }

        departmentRepository.delete(department);
        log.info("Department with ID {} deleted successfully", id);
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    private Department findDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    /**
     * Maps a Department entity to DepartmentDto.
     *
     * @param includeEmployees if true, the employee list is populated (for detail view)
     */
    private DepartmentDto mapToDto(Department dept, boolean includeEmployees) {
        DepartmentDto dto = DepartmentDto.builder()
                .id(dept.getId())
                .name(dept.getName())
                .location(dept.getLocation())
                .description(dept.getDescription())
                .employeeCount(dept.getEmployees().size())
                .build();

        if (includeEmployees) {
            dto.setEmployees(
                    dept.getEmployees().stream()
                            .map(emp -> com.techmahindra.ems.dto.EmployeeResponseDto.builder()
                                    .id(emp.getId())
                                    .firstName(emp.getFirstName())
                                    .lastName(emp.getLastName())
                                    .fullName(emp.getFullName())
                                    .email(emp.getEmail())
                                    .designation(emp.getDesignation())
                                    .salary(emp.getSalary())
                                    .joiningDate(emp.getJoiningDate())
                                    .active(emp.getActive())
                                    .build())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
