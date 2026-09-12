package com.techmahindra.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for returning Employee data in API responses.
 * Decouples the API contract from the internal entity model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String designation;
    private Double salary;
    private LocalDate joiningDate;
    private Boolean active;

    // Department info (flattened – no circular reference)
    private Long departmentId;
    private String departmentName;
    private String departmentLocation;
}
