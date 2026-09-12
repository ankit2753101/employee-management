package com.techmahindra.ems.config;

import com.techmahindra.ems.model.Department;
import com.techmahindra.ems.model.Employee;
import com.techmahindra.ems.repository.DepartmentRepository;
import com.techmahindra.ems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

/**
 * Seeds the H2 database with sample departments and employees on application startup.
 * This gives an instant ready-to-demo state without any manual setup.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            log.info("======= Seeding initial data =======");

            // ─── Departments ──────────────────────────────────────────────────
            Department engineering = departmentRepository.save(Department.builder()
                    .name("Engineering")
                    .location("Pune, India")
                    .description("Software development and architecture")
                    .build());

            Department hr = departmentRepository.save(Department.builder()
                    .name("Human Resources")
                    .location("Mumbai, India")
                    .description("Talent acquisition, payroll, and employee relations")
                    .build());

            Department finance = departmentRepository.save(Department.builder()
                    .name("Finance")
                    .location("Hyderabad, India")
                    .description("Accounting, budgeting, and financial reporting")
                    .build());

            log.info("Seeded {} departments", departmentRepository.count());

            // ─── Employees ────────────────────────────────────────────────────
            List<Employee> employees = List.of(
                    buildEmployee("Arjun",   "Sharma",   "arjun.sharma@techmahindra.com",   "Software Engineer",         75000.0, LocalDate.of(2023, 6, 1),  engineering),
                    buildEmployee("Priya",   "Patel",    "priya.patel@techmahindra.com",    "Senior Software Engineer",  95000.0, LocalDate.of(2021, 3, 15), engineering),
                    buildEmployee("Rohan",   "Mehta",    "rohan.mehta@techmahindra.com",    "DevOps Engineer",           88000.0, LocalDate.of(2022, 8, 10), engineering),
                    buildEmployee("Sneha",   "Iyer",     "sneha.iyer@techmahindra.com",     "QA Engineer",               70000.0, LocalDate.of(2023, 1, 20), engineering),
                    buildEmployee("Karan",   "Verma",    "karan.verma@techmahindra.com",    "HR Manager",                80000.0, LocalDate.of(2020, 5, 5),  hr),
                    buildEmployee("Anjali",  "Singh",    "anjali.singh@techmahindra.com",   "HR Executive",              55000.0, LocalDate.of(2022, 11, 1), hr),
                    buildEmployee("Vikram",  "Nair",     "vikram.nair@techmahindra.com",    "Finance Manager",           90000.0, LocalDate.of(2019, 7, 20), finance),
                    buildEmployee("Deepika", "Reddy",    "deepika.reddy@techmahindra.com",  "Accountant",                62000.0, LocalDate.of(2021, 9, 14), finance),
                    buildEmployee("Aditya",  "Joshi",    "aditya.joshi@techmahindra.com",   "Associate Software Engineer",60000.0,LocalDate.of(2024, 1, 8),  engineering),
                    buildEmployee("Meera",   "Krishnan", "meera.krishnan@techmahindra.com", "Business Analyst",          72000.0, LocalDate.of(2022, 4, 2),  engineering)
            );

            employeeRepository.saveAll(employees);
            log.info("Seeded {} employees", employeeRepository.count());
            log.info("======= Seeding complete =======");
        };
    }

    private Employee buildEmployee(String firstName, String lastName, String email,
                                   String designation, double salary,
                                   LocalDate joiningDate, Department department) {
        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .designation(designation)
                .salary(salary)
                .joiningDate(joiningDate)
                .active(true)
                .department(department)
                .build();
    }
}
