package com.techmahindra.ems.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Employee entity – represents an employee in the organization.
 */
@Entity
@Table(name = "employees",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String firstName;

    @Column(nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Many employees belong to one department.
     * The FK column "department_id" lives in the employees table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // ─── Convenience helper ───────────────────────────────────────────────────

    /** Returns the employee's full name. */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
