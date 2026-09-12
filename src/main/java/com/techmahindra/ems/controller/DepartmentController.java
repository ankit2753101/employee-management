package com.techmahindra.ems.controller;

import com.techmahindra.ems.dto.ApiResponse;
import com.techmahindra.ems.dto.DepartmentDto;
import com.techmahindra.ems.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing Department CRUD endpoints.
 * Base URL: /api/departments
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department APIs", description = "Endpoints for managing departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    // ─── POST /api/departments ────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<ApiResponse<DepartmentDto>> createDepartment(
            @Valid @RequestBody DepartmentDto dto) {

        DepartmentDto created = departmentService.createDepartment(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", created));
    }

    // ─── GET /api/departments ─────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all departments (summary – no employee list)")
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getAllDepartments() {
        return ResponseEntity.ok(
                ApiResponse.success("Departments fetched successfully",
                        departmentService.getAllDepartments()));
    }

    // ─── GET /api/departments/{id} ────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get a department by ID (includes employee list)")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Department fetched successfully",
                        departmentService.getDepartmentById(id)));
    }

    // ─── PUT /api/departments/{id} ────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Update a department")
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDto dto) {

        DepartmentDto updated = departmentService.updateDepartment(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", updated));
    }

    // ─── DELETE /api/departments/{id} ─────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department (only if no employees are assigned)")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully", null));
    }
}
