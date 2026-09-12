package com.techmahindra.ems;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Employee Management System API",
                version = "1.0.0",
                description = "REST API for managing employees and departments at Tech Mahindra",
                contact = @Contact(
                        name = "Tech Mahindra – ASE Project",
                        email = "support@techmahindra.com"
                )
        )
)
public class EmployeeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   Employee Management System Started!            ║");
        System.out.println("║   Swagger UI  → http://localhost:8080/swagger-ui.html ║");
        System.out.println("║   H2 Console  → http://localhost:8080/h2-console ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}
