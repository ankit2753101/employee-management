package com.techmahindra.ems.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Department – used for both requests and responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    // Populated in responses only
    private Integer employeeCount;
    private List<EmployeeResponseDto> employees;
}
