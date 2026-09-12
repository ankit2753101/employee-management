package com.techmahindra.ems.service;

import com.techmahindra.ems.dto.EmployeeRequestDto;
import com.techmahindra.ems.dto.EmployeeResponseDto;
import com.techmahindra.ems.exception.ResourceNotFoundException;
import com.techmahindra.ems.model.Department;
import com.techmahindra.ems.model.Employee;
import com.techmahindra.ems.repository.DepartmentRepository;
import com.techmahindra.ems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Employee business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // ─── Create ───────────────────────────────────────────────────────────────

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        log.info("Creating employee with email: {}", requestDto.getEmail());

        if (employeeRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException(
                    "An employee with email '" + requestDto.getEmail() + "' already exists.");
        }

        Department department = findDepartmentOrThrow(requestDto.getDepartmentId());

        Employee employee = Employee.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .designation(requestDto.getDesignation())
                .salary(requestDto.getSalary())
                .joiningDate(requestDto.getJoiningDate())
                .active(true)
                .department(department)
                .build();

        Employee saved = employeeRepository.save(employee);
        log.info("Employee created with ID: {}", saved.getId());
        return mapToDto(saved);
    }

    // ─── Read All (Paginated) ─────────────────────────────────────────────────

    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        log.info("Fetching all employees – page {}", pageable.getPageNumber());
        return employeeRepository.findAll(pageable).map(this::mapToDto);
    }

    // ─── Read One ─────────────────────────────────────────────────────────────

    public EmployeeResponseDto getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        return mapToDto(findEmployeeOrThrow(id));
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        log.info("Updating employee with ID: {}", id);
        Employee employee = findEmployeeOrThrow(id);

        // Email uniqueness check (only if email is being changed)
        if (!employee.getEmail().equalsIgnoreCase(requestDto.getEmail())
                && employeeRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException(
                    "An employee with email '" + requestDto.getEmail() + "' already exists.");
        }

        Department department = findDepartmentOrThrow(requestDto.getDepartmentId());

        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setEmail(requestDto.getEmail());
        employee.setDesignation(requestDto.getDesignation());
        employee.setSalary(requestDto.getSalary());
        employee.setJoiningDate(requestDto.getJoiningDate());
        employee.setDepartment(department);

        return mapToDto(employeeRepository.save(employee));
    }

    // ─── Delete (soft delete – marks inactive) ────────────────────────────────

    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Soft-deleting employee with ID: {}", id);
        Employee employee = findEmployeeOrThrow(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    // ─── Search ───────────────────────────────────────────────────────────────

    public List<EmployeeResponseDto> searchByName(String keyword) {
        log.info("Searching employees with keyword: {}", keyword);
        return employeeRepository.searchByName(keyword)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ─── Salary Update ────────────────────────────────────────────────────────

    @Transactional
    public EmployeeResponseDto updateSalary(Long id, Double newSalary) {
        log.info("Updating salary for employee ID: {} to {}", id, newSalary);
        if (newSalary <= 0) {
            throw new IllegalArgumentException("Salary must be greater than 0.");
        }
        Employee employee = findEmployeeOrThrow(id);
        employee.setSalary(newSalary);
        return mapToDto(employeeRepository.save(employee));
    }

    // ─── Employees by Department ──────────────────────────────────────────────

    public List<EmployeeResponseDto> getEmployeesByDepartment(Long departmentId) {
        log.info("Fetching employees for department ID: {}", departmentId);
        findDepartmentOrThrow(departmentId); // validate department exists
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    private Department findDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    /**
     * Maps an Employee entity to EmployeeResponseDto.
     */
    public EmployeeResponseDto mapToDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(employee.getFullName())
                .email(employee.getEmail())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .joiningDate(employee.getJoiningDate())
                .active(employee.getActive())
                .departmentId(employee.getDepartment().getId())
                .departmentName(employee.getDepartment().getName())
                .departmentLocation(employee.getDepartment().getLocation())
                .build();
    }
}
