# Employee Management System 🚀

A production-ready **REST API** for managing employees and departments, built with **Java 17** and **Spring Boot 3.2**.

> **Project built for Tech Mahindra – Associate Software Engineer (Entry Level)**

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA (Hibernate) |
| Database | H2 (In-Memory) |
| Validation | Spring Bean Validation |
| Boilerplate | Lombok |
| Build Tool | Maven |
| API Docs | SpringDoc OpenAPI (Swagger UI) |

---

## 📂 Project Structure

```
src/main/java/com/techmahindra/ems/
├── controller/       → REST endpoints
├── service/          → Business logic
├── repository/       → JPA data access
├── model/            → JPA entities
├── dto/              → Request/Response DTOs
├── exception/        → Global error handling
└── config/           → Seed data initializer
```

---

## ▶️ How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

```bash
git clone https://github.com/<your-username>/employee-management.git
cd employee-management
mvn spring-boot:run
```

### URLs

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Swagger UI – Test all APIs |
| `http://localhost:8080/h2-console` | H2 Database Console |
| `http://localhost:8080/api/employees` | Employees API |
| `http://localhost:8080/api/departments` | Departments API |

> **H2 Console:** JDBC URL → `jdbc:h2:mem:emsdb` · Username → `sa` · Password → *(blank)*

---

## 📡 API Endpoints

### 👥 Employee APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/employees` | Get all employees (paginated) |
| `GET` | `/api/employees/{id}` | Get employee by ID |
| `POST` | `/api/employees` | Create employee |
| `PUT` | `/api/employees/{id}` | Update employee |
| `DELETE` | `/api/employees/{id}` | Soft-delete employee |
| `GET` | `/api/employees/search?name=` | Search by name |
| `PATCH` | `/api/employees/{id}/salary` | Update salary |
| `GET` | `/api/employees/department/{id}` | Employees by department |

### 🏢 Department APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/departments` | Get all departments |
| `GET` | `/api/departments/{id}` | Get department with employees |
| `POST` | `/api/departments` | Create department |
| `PUT` | `/api/departments/{id}` | Update department |
| `DELETE` | `/api/departments/{id}` | Delete department |

---

## ✅ Key Concepts Demonstrated

- ✔️ **Layered Architecture** – Controller → Service → Repository
- ✔️ **JPA Relationships** – `@OneToMany` / `@ManyToOne`
- ✔️ **JPQL Custom Queries** – Name search
- ✔️ **Pagination & Sorting** – Via `Pageable`
- ✔️ **Bean Validation** – Input validation with proper error messages
- ✔️ **DTO Pattern** – Separates API layer from entity layer
- ✔️ **Global Exception Handling** – `@RestControllerAdvice`
- ✔️ **Soft Delete** – Employees marked inactive, not hard deleted
- ✔️ **Seed Data** – `CommandLineRunner` loads demo data on startup
- ✔️ **Swagger UI** – Full interactive API documentation

---

## 👤 Author

**Ankit Rajput**
📧 rajput5647699@gmail.com
