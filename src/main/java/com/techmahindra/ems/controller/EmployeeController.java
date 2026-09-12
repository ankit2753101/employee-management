package com.techmahindra.ems.controller;

import com.techmahindra.ems.dto.ApiResponse;
import com.techmahindra.ems.dto.EmployeeRequestDto;
import com.techmahindra.ems.dto.EmployeeResponseDto;
import com.techmahindra.ems.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller exposing Employee CRUD and utility endpoints.
 * Base URL: /api/employees
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee APIs", description = "Endpoints for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // ─── POST /api/employees ──────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new employee")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(
            @Valid @RequestBody EmployeeRequestDto requestDto) {

        EmployeeResponseDto created = employeeService.createEmployee(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    // ─── GET /api/employees ───────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all employees (paginated & sorted)")
    public ResponseEntity<ApiResponse<Page<EmployeeResponseDto>>> getAllEmployees(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")             @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction: asc/desc") @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Page<EmployeeResponseDto> employees =
                employeeService.getAllEmployees(PageRequest.of(page, size, sort));

        return ResponseEntity.ok(ApiResponse.success("Employees fetched successfully", employees));
    }

    // ─── GET /api/employees/{id} ──────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get an employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Employee fetched successfully",
                        employeeService.getEmployeeById(id)));
    }

    // ─── PUT /api/employees/{id} ──────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto requestDto) {

        EmployeeResponseDto updated = employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    // ─── DELETE /api/employees/{id} ───────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an employee (marks as inactive)")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated successfully", null));
    }

    // ─── GET /api/employees/search ────────────────────────────────────────────

    @GetMapping("/search")
    @Operation(summary = "Search employees by name (first or last name)")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> searchByName(
            @Parameter(description = "Search keyword") @RequestParam String name) {

        List<EmployeeResponseDto> results = employeeService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success(
                results.size() + " employee(s) found", results));
    }

    // ─── PATCH /api/employees/{id}/salary ─────────────────────────────────────

    @PatchMapping("/{id}/salary")
    @Operation(summary = "Update an employee's salary")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateSalary(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body) {

        Double newSalary = body.get("salary");
        if (newSalary == null) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Request body must contain 'salary' field."));
        }

        EmployeeResponseDto updated = employeeService.updateSalary(id, newSalary);
        return ResponseEntity.ok(ApiResponse.success("Salary updated successfully", updated));
    }

    // ─── GET /api/employees/department/{departmentId} ─────────────────────────

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get all employees in a specific department")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getByDepartment(
            @PathVariable Long departmentId) {

        List<EmployeeResponseDto> employees =
                employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(
                employees.size() + " employee(s) found in department", employees));
    }
}
