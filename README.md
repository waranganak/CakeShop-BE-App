# 🎂 Dream's Cake Backend – Smart Cake Shop & Bakery Management System

<p>
  <img src="https://img.shields.io/badge/JAVA-21%20LTS-orange.svg" alt="Java">
  <img src="https://img.shields.io/badge/SPRING%20BOOT-3.2.4-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/SPRING%20SECURITY-6.X-blue.svg" alt="Spring Security">
  <img src="https://img.shields.io/badge/THYMELEAF-3.X-green.svg" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/MYSQL-8.0-blue.svg" alt="MySQL">
</p>
<p>
  <img src="https://img.shields.io/badge/BREVO-TRANSACTIONAL%20SMTP-blueviolet.svg" alt="Brevo">
  <img src="https://img.shields.io/badge/DOCKER-CONTAINERIZED-informational.svg" alt="Docker">
  <img src="https://img.shields.io/badge/RENDER-CLOUD%20HOSTED-success.svg" alt="Render">
  <img src="https://img.shields.io/badge/GOOGLE%20DOCS-PROJECT%20REPORT-blue.svg" alt="Google Docs">
</p>

---

* **Course:** Final Coursework
* **Module:** Advanced API Development
* **Institution:** Institute of Java and Software Engineering (IJSE)
* **Author:** H. G. K. Warangana (Kaumini Warangana)
* **Project Documentation:** [Dream's Cake Project Report (Google Docs)](https://docs.google.com)[cite: 1]
* **Live API URL:** [https://dreams-cake-backend.onrender.com](https://onrender.com)[cite: 1]
* **Frontend Application:** [https://dreams-cake-webapp.vercel.app](https://vercel.app)[cite: 1]

---

## 📖 System Overview

**Dream's Cake** is an enterprise-grade RESTful bakery management and ordering platform built to streamline operations such as inventory tracking, recipe management (product-ingredient mapping), user authentication, customer orders, and automated email notifications.

The backend engine provides a secure, high-concurrency API powering primary user workflows:
* **Customers:** User registration, browsing cake catalogs, placing custom orders, and receiving automated email confirmations.
* **Staff / Cashiers:** Managing daily orders, updating order statuses, and monitoring product availability.
* **Platform Administrators:** Comprehensive management of users, inventory ingredients, low-stock alerts, product recipes, and financial reports.

---

## 🛠️ Technology Stack Breakdown & Architecture

| Technology / Library | Version / Spec | Purpose & Architectural Justification |
| :--- | :--- | :--- |
| **Java Platform** | Java 21 (LTS) | Leverages modern LTS language features (Pattern Matching, Records, enhanced switch expressions) and high-throughput memory management. |
| **Framework** | Spring Boot 3.2.4 | Core inversion-of-control (IoC) container, REST controllers, declarative transaction management (`@Transactional`). |
| **Security** | Spring Security 6 + JJWT | Stateless JWT authentication filter, role-based endpoint security, and BCrypt password hashing. |
| **Persistence / ORM** | Spring Data JPA & Hibernate | Relational mapping for 9 core domain entities, custom JPQL queries, and HikariCP connection pooling. |
| **Database** | MySQL 8+ | ACID-compliant relational storage with strict foreign keys and transactional integrity. |
| **Template Engine** | Spring Boot Thymeleaf | Decouples HTML email presentation from Java business logic for professional transactional order receipts. |
| **Transactional Email** | Brevo SMTP | Cloud-native transactional mail delivery on port 587 (STARTTLS) ensuring reliable inbox delivery. |

---

## 💡 Core Engineering Highlights

1. **Recipe-Based Ingredient Deduction:** When a customer places an order, the system automatically calculates required raw ingredients based on the product recipe (`ProductIngredient`), checks stock levels, updates inventory safely, and prevents overselling.
2. **Automated Low-Stock Tracking:** Ingredient management monitors stock quantities and unit types (`UnitType`) to ensure uninterrupted bakery operations.
3. **Secure Authentication & RBAC:** Protects administrative and user endpoints using stateless JSON Web Tokens (JWT) and encrypted passwords (BCrypt).
4. **Automated Transactional Emails:** Uses Brevo SMTP integrated with Thymeleaf HTML templates to instantly dispatch professional order receipts to customers.

📡 REST API Catalog Overview
---
Authentication (/api/v1/auth): Login, token validation, and user registration.

Products (/api/v1/products): Manage cake catalog, prices, categories, and recipes.

Ingredients (/api/v1/ingredients): Track stock quantities, units, and reorder levels.

Orders (/api/v1/orders): Place customer orders, process payments, and track order history.

Users (/api/v1/users): Admin user management and role assignments.

Installation Steps
---
Clone the Repository:

git clone [https://github.com/waranganak/CakeShop-BE-App.git](https://github.com/waranganak/CakeShop-BE-App.git)
cd CakeShop-BE-App

Build and Run the Application:
---
mvn clean install
mvn spring-boot:run

🎓 Academic Module Information
---
Coursework: Final Coursework

Module: Advanced API Development

Institution: Institute of Java and Software Engineering (IJSE)

Author: H. G. K. Warangana (Kaumini Warangana)


🔒 Security Standards
---
Password Hashing: BCrypt encryption for user credentials

Stateless Security: JWT-based request filtering

Environment Protection: Sensitive database and mail credentials managed securely

## 🏛️ System Architecture

Dream's Cake backend strictly adheres to a layered architecture:

```text
               Client Applications (Frontend / Postman)
                                │
                                ▼
         ┌──────────────────────────────────────────────┐
         │          Spring Security Filter Chain        │
         └──────────────────────┬───────────────────────┘
                                │
                                ▼
         ┌──────────────────────────────────────────────┐
         │               Controller Layer               │
         │      REST Endpoints & DTO Validation         │
         └──────────────────────┬───────────────────────┘
                                │
                                ▼
         ┌──────────────────────────────────────────────┐
         │                 Service Layer                │
         │     Business Logic & Transaction Handling    │
         └──┬────────────────────────┬──────────────────┘
            │                        │
            ▼                        ▼
     ┌──────────────┐         ┌──────────────┐
     │  Brevo SMTP  │         │  Repository  │
     │ (Thymeleaf)  │         │  (JPA / SQL) │
     └──────────────┘         └──────┬───────┘
                                     │
                                     ▼
                      ┌──────────────────────────────┐
                      │    MySQL Relational DB       │
                      └──────────────────────────────┘
dreams_cake_backend/
├── src/
│   ├── main/
│   │   ├── java/lk/ijse/DreamsCake/
│   │   │   ├── DreamsCakeApplication.java
│   │   │   ├── config/              # Security and application configurations
│   │   │   ├── controller/          # REST Controllers (Auth, Product, Order, Ingredient, User)
│   │   │   ├── dto/                 # Request & Response Data Transfer Objects
│   │   │   ├── entity/              # JPA Domain Entities (User, Product, Ingredient, Order, etc.)
│   │   │   ├── enums/               # OrderStatus, UnitType, PaymentMethod, PaymentStatus
│   │   │   ├── exception/           # Global Exception Handler
│   │   │   ├── repository/          # Spring Data JPA Interfaces
│   │   │   ├── security/            # JWT Token Provider & Filters
│   │   │   ├── service/             # Service Interfaces & Implementations (impl/)
│   │   │   └── util/                # Helper utilities
│   │   └── resources/
│   │       ├── application.properties # Core Spring configuration
│   │       └── templates/mail/        # Thymeleaf HTML Email Templates
│   └── test/                        # Unit and integration test suites
├── Dockerfile                       # Container deployment definition
├── pom.xml                          # Maven build dependencies
└── README.md                        # Project documentation

