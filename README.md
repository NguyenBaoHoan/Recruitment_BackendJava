<div align="center">

# 🎯 Recruitment Platform

### A Modern, Full-Featured Job Recruitment System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Build](https://img.shields.io/badge/Build-Gradle-green.svg)](https://gradle.org/)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-purple.svg)](https://stomp.github.io/)

[Features](#-features) • [Tech Stack](#-tech-stack) • [Installation](#-installation) • [API Docs](#-api-documentation) • [Contributing](#-contributing)

</div>

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Testing](#-testing)
- [Security](#-security)
- [WebSocket Integration](#-websocket-integration)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)
- [Contact](#-contact)
- [Acknowledgments](#-acknowledgments)

---

## 🌟 Overview

**JobHunter** is a comprehensive recruitment platform built with Spring Boot that connects job seekers with employers. The system provides robust features for job posting, candidate management, real-time chat communication, and portfolio showcasing.

### 🎯 Key Objectives

- **For Job Seekers**: Easy job search, application tracking, career expectation management, and portfolio building
- **For Employers**: Efficient job posting, candidate filtering, resume management, and real-time communication
- **For System**: Secure authentication, role-based access control, and scalable architecture

### 🌐 Live Demo

> **Note**: Add your deployment URL here when available

---

## ✨ Features

### 👤 User Management
- ✅ User registration and authentication (JWT-based)
- ✅ OAuth2 resource server integration
- ✅ Role-based access control (RBAC)
- ✅ Profile management with customizable career expectations
- ✅ Password encryption with BCrypt
- ✅ Refresh token mechanism for persistent sessions

### 💼 Job Management
- ✅ Create, read, update, delete (CRUD) job postings
- ✅ Advanced job filtering by location, salary, education level
- ✅ Job status tracking (Active/Inactive)
- ✅ Job categorization by type and skill requirements
- ✅ Date-based job expiration management
- ✅ Favorite jobs feature for candidates

### 🏢 Company Management
- ✅ Company profile management
- ✅ Company-job relationship tracking
- ✅ Company information display on job postings

### 📝 Resume & Portfolio
- ✅ Resume submission and management
- ✅ Portfolio creation with image uploads
- ✅ Multi-file upload support (up to 50MB)
- ✅ Portfolio showcase for candidates
- ✅ Career expectation documentation

### 💬 Real-time Communication
- ✅ WebSocket-based real-time chat
- ✅ Private messaging between users
- ✅ Chat room management
- ✅ Message history tracking
- ✅ STOMP protocol integration

### 🔍 Advanced Features
- ✅ Pagination support for all list endpoints
- ✅ Advanced filtering with JPA specifications
- ✅ Email notification system (Spring Mail)
- ✅ File upload and management system
- ✅ RESTful API design
- ✅ API documentation with OpenAPI/Swagger
- ✅ Actuator endpoints for monitoring

### 🛡️ Security Features
- ✅ JWT (JSON Web Token) authentication
- ✅ Token-based authorization
- ✅ CORS configuration
- ✅ XSS protection
- ✅ CSRF protection
- ✅ Secure password storage

---

## 🛠 Tech Stack

### Backend Framework
- **[Spring Boot 3.4.1](https://spring.io/projects/spring-boot)** - Main application framework
- **[Spring Security](https://spring.io/projects/spring-security)** - Authentication & authorization
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)** - Database access layer
- **[Spring WebSocket](https://spring.io/guides/gs/messaging-stomp-websocket/)** - Real-time communication
- **[Spring Mail](https://docs.spring.io/spring-framework/reference/integration/email.html)** - Email functionality
- **[Spring Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)** - Application monitoring

### Security & Authentication
- **[Spring OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)** - OAuth2 implementation
- **[JWT (jjwt 0.11.5)](https://github.com/jwtk/jjwt)** - JSON Web Token handling
- **BCrypt** - Password hashing

### Database
- **[MySQL 8.0+](https://www.mysql.com/)** - Primary relational database
- **[H2 Database](https://www.h2database.com/)** - In-memory database for testing
- **[Hibernate](https://hibernate.org/)** - ORM framework

### Documentation & API
- **[SpringDoc OpenAPI 2.5.0](https://springdoc.org/)** - API documentation (Swagger UI)
- **[Thymeleaf](https://www.thymeleaf.org/)** - Template engine

### Utilities & Libraries
- **[Lombok](https://projectlombok.org/)** - Reduce boilerplate code
- **[Spring Filter (Turkraft) 3.1.7](https://github.com/turkraft/spring-filter)** - Advanced filtering for JPA
- **[Bean Validation](https://beanvalidation.org/)** - Request validation

### Build Tool
- **[Gradle 8.x](https://gradle.org/)** - Build automation with Kotlin DSL

### Development Tools
- **[Spring DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools)** - Hot reload during development
- **[Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)** - LTS Java version

---

## 🏗 Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                          │
│  (Web Browser / Mobile App / Third-party Applications)      │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ HTTPS / WSS
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                     API Gateway / CORS                       │
└──────────────────────────┬──────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌───────────────┐  ┌──────────────┐  ┌──────────────┐
│  REST API     │  │  WebSocket   │  │   Security   │
│  Controllers  │  │   (STOMP)    │  │    Filter    │
└───────┬───────┘  └──────┬───────┘  └──────┬───────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                      Service Layer                           │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │   Auth   │ │   Job    │ │   Chat   │ │   User   │ ...   │
│  │ Service  │ │ Service  │ │ Service  │ │ Service  │       │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   Repository Layer (JPA)                     │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │   User   │ │   Job    │ │ Message  │ │ Company  │ ...   │
│  │   Repo   │ │   Repo   │ │   Repo   │ │   Repo   │       │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                      MySQL Database                          │
│  ┌────────────────────────────────────────────────┐         │
│  │  users | jobs | companies | messages | ...     │         │
│  └────────────────────────────────────────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

### Package Structure

```
com.example.jobhunter/
├── config/                          # Configuration classes
│   ├── SecurityConfiguration.java   # Spring Security setup
│   ├── CorsConfig.java              # CORS configuration
│   ├── WebSocketConfig.java         # WebSocket configuration
│   ├── OpenAPIConfig.java           # Swagger/OpenAPI config
│   └── DateTimeFormatConfiguration.java
│
├── controller/                      # REST Controllers
│   ├── AuthController.java          # Authentication endpoints
│   ├── JobController.java           # Job management endpoints
│   ├── UserController.java          # User management endpoints
│   ├── ChatController.java          # Chat/messaging endpoints
│   ├── CompanyController.java       # Company management
│   ├── SkillController.java         # Skills management
│   ├── PortfolioController.java     # Portfolio management
│   ├── FavoriteJobController.java   # Favorite jobs
│   ├── CareerExpectationController.java
│   ├── FileController.java          # File upload/download
│   └── RestResponse.java            # Standard response wrapper
│
├── domain/                          # Entity classes (JPA)
│   ├── User.java                    # User entity
│   ├── Job.java                     # Job entity
│   ├── Company.java                 # Company entity
│   ├── Resume.java                  # Resume entity
│   ├── Skills.java                  # Skills entity
│   ├── ChatRoom.java                # Chat room entity
│   ├── Message.java                 # Message entity
│   ├── UserPortfolio.java           # Portfolio entity
│   ├── UserPortfolioImage.java      # Portfolio images
│   ├── FavoriteJob.java             # Favorite jobs
│   └── CareerExpectation.java       # Career expectations
│
├── repository/                      # JPA Repositories
│   ├── UserRepository.java
│   ├── JobRepository.java
│   ├── CompanyRepository.java
│   └── ... (other repositories)
│
├── service/                         # Business logic layer
│   ├── AuthService.java             # Authentication logic
│   ├── UserService.java             # User operations
│   ├── JobService.java              # Job operations
│   ├── ChatService.java             # Chat logic
│   └── ... (other services)
│
├── dto/                             # Data Transfer Objects
│   ├── request/                     # Request DTOs
│   │   ├── ReqLoginDTO.java
│   │   ├── ReqRegisterDTO.java
│   │   └── ...
│   └── response/                    # Response DTOs
│       ├── ResLoginDTO.java
│       ├── job/                     # Job-related responses
│       └── message/                 # Message-related responses
│
├── util/                            # Utility classes
│   ├── annotation/                  # Custom annotations
│   │   └── ApiMessage.java
│   ├── constant/                    # Constants & enums
│   │   ├── GenderEnum.java
│   │   └── LevelEnum.java
│   └── error/                       # Error handling
│       ├── SecurityUtil.java
│       └── IdInvalidException.java
│
└── JobhunterApplication.java        # Main application class
```

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

### Required
- **Java Development Kit (JDK) 17 or higher**
  ```bash
  java -version  # Should show version 17+
  ```

- **MySQL 8.0 or higher**
  ```bash
  mysql --version
  ```

- **Gradle 8.x** (or use included Gradle Wrapper)
  ```bash
  gradle --version
  ```

### Optional
- **Git** - For version control
- **Postman** or **Insomnia** - For API testing
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Docker** (for containerized deployment)

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/NguyenBaoHoan/Recruitment.git
cd Recruitment
```

### 2. Create MySQL Database

```sql
-- Login to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE jobhunter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user (optional)
CREATE USER 'jobhunter_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON jobhunter.* TO 'jobhunter_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Application name
spring.application.name=jobhunter

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/jobhunter
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
hoan.jwt.base64-secret=YOUR_BASE64_SECRET_KEY_HERE
hoan.jwt.access-token-validity-in-seconds=86400
hoan.jwt.refresh-token-validity-in-seconds=604800

# File Upload Configuration
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
hoan.upload-file.base-uri=file:///YOUR_UPLOAD_PATH/

# Pagination Configuration
spring.data.web.pageable.one-indexed-parameters=true
```

### 4. Generate JWT Secret Key

Generate a secure Base64-encoded secret key:

```bash
# Using OpenSSL (Linux/Mac)
openssl rand -base64 64

# Using PowerShell (Windows)
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
```

Copy the generated key to `hoan.jwt.base64-secret` in application.properties.

### 5. Create Upload Directory

```bash
# Windows
mkdir D:\uploads\portfolio

# Linux/Mac
mkdir -p /var/uploads/portfolio
```

### 6. Build the Application

Using Gradle Wrapper (recommended):

```bash
# Windows
gradlew.bat clean build

# Linux/Mac
./gradlew clean build
```

Or using installed Gradle:

```bash
gradle clean build
```

### 7. Run the Application

```bash
# Using Gradle Wrapper
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun

# Or run the JAR directly
java -jar build/libs/jobhunter-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

---

## ⚙ Configuration

### Environment-Specific Configuration

Create separate property files for different environments:

- `application.properties` - Default configuration
- `application-dev.properties` - Development environment
- `application-prod.properties` - Production environment

Run with specific profile:

```bash
# Development
java -jar jobhunter.jar --spring.profiles.active=dev

# Production
java -jar jobhunter.jar --spring.profiles.active=prod
```

### Key Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `spring.datasource.url` | Database connection URL | `jdbc:mysql://localhost:3306/jobhunter` |
| `spring.jpa.hibernate.ddl-auto` | Hibernate DDL mode | `update` |
| `hoan.jwt.access-token-validity-in-seconds` | Access token expiration | `86400` (24 hours) |
| `hoan.jwt.refresh-token-validity-in-seconds` | Refresh token expiration | `604800` (7 days) |
| `spring.servlet.multipart.max-file-size` | Max upload file size | `50MB` |

---

## 📖 Usage

### Starting the Application

1. **Start MySQL Database**
   ```bash
   # Windows (XAMPP/WAMP)
   # Start MySQL from control panel
   
   # Linux
   sudo systemctl start mysql
   
   # Mac
   brew services start mysql
   ```

2. **Run the Application**
   ```bash
   ./gradlew bootRun
   ```

3. **Verify Application is Running**
   - Open browser: `http://localhost:8080/actuator/health`
   - Expected response: `{"status":"UP"}`

### First-Time Setup

1. **Create Admin User** (via API or direct database insert)
   ```sql
   INSERT INTO users (name, email, password, role)
   VALUES ('Admin', 'admin@jobhunter.com', '$2a$10$...bcrypt_hash...', 'ADMIN');
   ```

2. **Login to Get JWT Token**
   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"admin@jobhunter.com","password":"admin123"}'
   ```

3. **Access Protected Endpoints**
   ```bash
   curl http://localhost:8080/api/v1/jobs \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

---

## 📚 API Documentation

### Accessing API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Main API Endpoints

#### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/auth/register` | Register new user | ❌ |
| POST | `/api/v1/auth/login` | Login user | ❌ |
| POST | `/api/v1/auth/refresh` | Refresh access token | ✅ |
| POST | `/api/v1/auth/logout` | Logout user | ✅ |
| GET | `/api/v1/auth/account` | Get current user | ✅ |

#### Jobs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/jobs` | Get all jobs (paginated) | ❌ |
| GET | `/api/v1/jobs/{id}` | Get job by ID | ❌ |
| POST | `/api/v1/jobs` | Create new job | ✅ (ADMIN) |
| PUT | `/api/v1/jobs/{id}` | Update job | ✅ (ADMIN) |
| DELETE | `/api/v1/jobs/{id}` | Delete job | ✅ (ADMIN) |

#### Users

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/users` | Get all users | ✅ (ADMIN) |
| GET | `/api/v1/users/{id}` | Get user by ID | ✅ |
| PUT | `/api/v1/users/{id}` | Update user | ✅ |
| DELETE | `/api/v1/users/{id}` | Delete user | ✅ (ADMIN) |

#### Companies

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/companies` | Get all companies | ❌ |
| GET | `/api/v1/companies/{id}` | Get company by ID | ❌ |
| POST | `/api/v1/companies` | Create company | ✅ (ADMIN) |
| PUT | `/api/v1/companies/{id}` | Update company | ✅ (ADMIN) |
| DELETE | `/api/v1/companies/{id}` | Delete company | ✅ (ADMIN) |

#### Chat / Messaging

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/chat/rooms` | Get user's chat rooms | ✅ |
| GET | `/chat/rooms/{roomId}/messages` | Get chat history | ✅ |
| POST | `/chat/rooms` | Create chat room | ✅ |
| WS | `/ws` | WebSocket connection | ✅ |
| WS | `/app/chat` | Send message | ✅ |

#### Portfolio

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/portfolios` | Get user portfolios | ✅ |
| POST | `/api/v1/portfolios` | Create portfolio | ✅ |
| PUT | `/api/v1/portfolios/{id}` | Update portfolio | ✅ |
| DELETE | `/api/v1/portfolios/{id}` | Delete portfolio | ✅ |

#### Skills

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/skills` | Get all skills | ❌ |
| POST | `/api/v1/skills` | Create skill | ✅ (ADMIN) |
| PUT | `/api/v1/skills/{id}` | Update skill | ✅ (ADMIN) |
| DELETE | `/api/v1/skills/{id}` | Delete skill | ✅ (ADMIN) |

### Example API Requests

#### 1. Register New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "age": 25,
    "gender": "MALE",
    "address": "123 Main St"
  }'
```

#### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

Response:
```json
{
  "access_token": "eyJhbGciOiJIUzUxMiJ9...",
  "refresh_token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

#### 3. Get Jobs with Pagination

```bash
curl http://localhost:8080/api/v1/jobs?page=1&size=10&sort=createdAt,desc
```

#### 4. Create Job (Admin only)

```bash
curl -X POST http://localhost:8080/api/v1/jobs \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Senior Java Developer",
    "location": "Hanoi",
    "salary": "$2000-$3000",
    "educationLevel": "BACHELOR",
    "description": "We are looking for...",
    "requirements": "5+ years Java experience...",
    "benefits": "Competitive salary, health insurance...",
    "isActive": true
  }'
```

---

## 🗄 Database Schema

### Entity Relationship Diagram

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│    User     │────────<│   Resume     │>────────│     Job     │
└─────────────┘         └──────────────┘         └─────────────┘
      │                                                  │
      │ 1:N                                           N:M│
      │                                                  │
┌─────▼────────┐                              ┌─────────▼──────┐
│ UserPortfolio│                              │     Skills     │
└──────────────┘                              └────────────────┘
      │ 1:N                                           │
      │                                               │
┌─────▼─────────────┐                         ┌─────▼──────────┐
│UserPortfolioImage │                         │    Company     │
└───────────────────┘                         └────────────────┘

┌─────────────┐         ┌──────────────┐
│  ChatRoom   │────────<│   Message    │
└─────────────┘         └──────────────┘
      │ N:M
      │
┌─────▼────────┐
│    User      │
└──────────────┘
```

### Core Tables

#### users
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| name | VARCHAR(255) | User full name |
| email | VARCHAR(255) | User email (unique) |
| password | VARCHAR(255) | Encrypted password |
| age | INT | User age |
| gender | ENUM | MALE/FEMALE/OTHER |
| address | VARCHAR(255) | User address |
| refresh_token | MEDIUMTEXT | JWT refresh token |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

#### jobs
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| name | VARCHAR(255) | Job title |
| location | VARCHAR(255) | Job location |
| salary | VARCHAR(100) | Salary range |
| education_level | ENUM | Education requirement |
| job_type | MEDIUMTEXT | Job type/category |
| description | MEDIUMTEXT | Job description |
| requirements | MEDIUMTEXT | Job requirements |
| benefits | MEDIUMTEXT | Job benefits |
| work_address | VARCHAR(255) | Work location |
| start_date | DATE | Job start date |
| end_date | DATE | Job end date |
| is_active | BOOLEAN | Job status |

#### companies
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| name | VARCHAR(255) | Company name |
| description | TEXT | Company description |
| address | VARCHAR(255) | Company address |
| logo | VARCHAR(255) | Company logo URL |

#### messages
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| content | TEXT | Message content |
| sender_id | BIGINT | Foreign key to users |
| room_id | BIGINT | Foreign key to chat_rooms |
| created_at | TIMESTAMP | Message timestamp |

---

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests UserServiceTest

# Run tests without building
./gradlew test --rerun-tasks
```

### Test Configuration

The project uses **JUnit 5 (Jupiter)** for testing. Test configuration is in `build.gradle.kts`:

```kotlin
testImplementation("org.springframework.boot:spring-boot-starter-test")
testImplementation("org.springframework.security:spring-security-test")
testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
testRuntimeOnly("com.h2database:h2") // In-memory database for tests
```

### Writing Tests

Example test structure:

```java
@SpringBootTest
@AutoConfigureMockMvc
class JobControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateJob() throws Exception {
        mockMvc.perform(post("/api/v1/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Job\"}"))
                .andExpect(status().isCreated());
    }
}
```

### Test Coverage

View coverage report after running tests:
```
build/reports/jacoco/test/html/index.html
```

---

## 🔒 Security

### Authentication Flow

1. **User Registration**
   - Password is hashed using BCrypt
   - User details stored in database

2. **User Login**
   - Credentials validated
   - JWT access token generated (24h validity)
   - JWT refresh token generated (7 days validity)
   - Tokens returned to client

3. **Accessing Protected Resources**
   - Client sends JWT token in Authorization header
   - Server validates token signature and expiration
   - User identity extracted from token claims

4. **Token Refresh**
   - Client sends refresh token
   - New access token generated if refresh token valid

### Security Best Practices Implemented

✅ **Password Security**
- BCrypt hashing with salt
- Minimum password complexity requirements
- Password never returned in API responses

✅ **JWT Security**
- Tokens signed with HS512 algorithm
- Short-lived access tokens
- Secure secret key storage
- Token blacklisting on logout

✅ **API Security**
- HTTPS recommended for production
- CORS configuration for allowed origins
- CSRF protection for state-changing operations
- Rate limiting (recommended to implement)

✅ **Input Validation**
- Bean Validation annotations
- SQL injection prevention via JPA
- XSS protection via output encoding

### Role-Based Access Control

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access to all resources |
| **EMPLOYER** | Manage own jobs, view resumes |
| **USER** | Apply to jobs, manage own profile |

---

## 🔌 WebSocket Integration

### Connecting to WebSocket

```javascript
// JavaScript example using SockJS and STOMP
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({
    'Authorization': 'Bearer YOUR_JWT_TOKEN'
}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to private messages
    stompClient.subscribe('/user/queue/messages', function(message) {
        console.log('Received: ' + message.body);
    });
    
    // Subscribe to chat room
    stompClient.subscribe('/topic/room/' + roomId, function(message) {
        console.log('Room message: ' + message.body);
    });
});

// Send message
function sendMessage(roomId, content) {
    stompClient.send("/app/chat", {}, JSON.stringify({
        'roomId': roomId,
        'content': content
    }));
}
```

### WebSocket Endpoints

- **Connection**: `ws://localhost:8080/ws`
- **Application Prefix**: `/app`
- **Broker Prefix**: `/topic`, `/user`

---

## 🚢 Deployment

### Production Configuration

1. **Update application.properties for production**

```properties
# Production profile
spring.profiles.active=prod

# Database
spring.datasource.url=jdbc:mysql://prod-db-server:3306/jobhunter
spring.jpa.hibernate.ddl-auto=validate

# Security
hoan.jwt.base64-secret=${JWT_SECRET}

# File upload
hoan.upload-file.base-uri=file:///opt/jobhunter/uploads/
```

2. **Build Production JAR**

```bash
./gradlew clean build -Pprod
```

3. **Run as Service**

Create systemd service file `/etc/systemd/system/jobhunter.service`:

```ini
[Unit]
Description=JobHunter Application
After=syslog.target

[Service]
User=jobhunter
ExecStart=/usr/bin/java -jar /opt/jobhunter/jobhunter.jar
SuccessExitStatus=143
Environment="SPRING_PROFILES_ACTIVE=prod"

[Install]
WantedBy=multi-user.target
```

Enable and start:
```bash
sudo systemctl enable jobhunter
sudo systemctl start jobhunter
```

### Docker Deployment

Create `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:

```bash
# Build image
docker build -t jobhunter:latest .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/jobhunter \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  jobhunter:latest
```

### Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: jobhunter
      MYSQL_ROOT_PASSWORD: rootpassword
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/jobhunter
      SPRING_DATASOURCE_PASSWORD: rootpassword

volumes:
  mysql-data:
```

Run with:
```bash
docker-compose up -d
```

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### How to Contribute

1. **Fork the Repository**
   ```bash
   git clone https://github.com/NguyenBaoHoan/Recruitment.git
   ```

2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make Your Changes**
   - Write clean, documented code
   - Follow existing code style
   - Add tests for new features

4. **Commit Your Changes**
   ```bash
   git commit -m "Add amazing feature"
   ```

5. **Push to Branch**
   ```bash
   git push origin feature/amazing-feature
   ```

6. **Open a Pull Request**

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable names
- Comment complex logic
- Keep methods small and focused
- Write unit tests for new features

### Commit Message Convention

```
type(scope): subject

body

footer
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Example:
```
feat(auth): add OAuth2 Google login

Implement Google OAuth2 authentication flow
- Add Google OAuth2 configuration
- Create OAuth2 success handler
- Update user registration flow

Closes #123
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Database Connection Error

**Problem**: `Communications link failure`

**Solution**:
- Verify MySQL is running: `sudo systemctl status mysql`
- Check connection details in `application.properties`
- Ensure database exists: `CREATE DATABASE jobhunter;`
- Check firewall settings

#### 2. JWT Token Invalid

**Problem**: `JWT signature does not match locally computed signature`

**Solution**:
- Regenerate JWT secret key
- Ensure secret key is properly Base64 encoded
- Check token expiration time

#### 3. File Upload Failed

**Problem**: `The temporary upload location is not valid`

**Solution**:
```bash
# Create upload directory
mkdir -p /var/uploads/portfolio

# Check permissions
chmod 755 /var/uploads
```

#### 4. Port Already in Use

**Problem**: `Port 8080 is already in use`

**Solution**:
```bash
# Find process using port
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080

# Kill process or change port in application.properties
server.port=8081
```

#### 5. Gradle Build Failed

**Problem**: Build errors with Java version

**Solution**:
```bash
# Check Java version
java -version

# Set JAVA_HOME
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

### Logging

Enable debug logging in `application.properties`:

```properties
# Enable debug logging
logging.level.root=INFO
logging.level.com.example.jobhunter=DEBUG
logging.level.org.springframework.security=DEBUG

# Log to file
logging.file.name=logs/application.log
```

---

## 📄 License

This project is licensed under the **MIT License**.

```
MIT License

Copyright (c) 2024 Nguyen Bao Hoan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

See the [LICENSE](LICENSE) file for details.

---

## 📞 Contact

**Project Maintainer**: Nguyen Bao Hoan

- **Email**: hoan.nguyen@gmail.com
- **GitHub**: [@NguyenBaoHoan](https://github.com/NguyenBaoHoan)
- **LinkedIn**: [Hoan Nguyen](https://www.linkedin.com/in/hoannguyen)
- **Website**: [hoannguyen.com](https://hoannguyen.com)

**Project Repository**: [https://github.com/NguyenBaoHoan/Recruitment](https://github.com/NguyenBaoHoan/Recruitment)

### Support

- 🐛 **Bug Reports**: [GitHub Issues](https://github.com/NguyenBaoHoan/Recruitment/issues)
- 💡 **Feature Requests**: [GitHub Discussions](https://github.com/NguyenBaoHoan/Recruitment/discussions)
- 📧 **Email Support**: hoan.nguyen@gmail.com

---

## 🙏 Acknowledgments

Special thanks to:

- **Spring Framework Team** - For the excellent Spring Boot framework
- **MySQL Team** - For the robust database system
- **JWT.io** - For JWT implementation and documentation
- **Lombok Project** - For reducing boilerplate code
- **SpringDoc** - For OpenAPI documentation tools
- **Stack Overflow Community** - For endless problem-solving help

### Resources & Learning Materials

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT Introduction](https://jwt.io/introduction)
- [RESTful API Design Best Practices](https://restfulapi.net/)
- [WebSocket Protocol](https://datatracker.ietf.org/doc/html/rfc6455)

---

## 📊 Project Status

![GitHub last commit](https://img.shields.io/github/last-commit/NguyenBaoHoan/Recruitment)
![GitHub issues](https://img.shields.io/github/issues/NguyenBaoHoan/Recruitment)
![GitHub pull requests](https://img.shields.io/github/issues-pr/NguyenBaoHoan/Recruitment)
![GitHub stars](https://img.shields.io/github/stars/NguyenBaoHoan/Recruitment)
![GitHub forks](https://img.shields.io/github/forks/NguyenBaoHoan/Recruitment)

---

## 🗺 Roadmap

### Version 1.x (Current)
- ✅ Core authentication & authorization
- ✅ Job management CRUD
- ✅ User management
- ✅ Real-time chat system
- ✅ Portfolio management
- ✅ File upload system

### Version 2.0 (Planned)
- [ ] Advanced search with Elasticsearch
- [ ] Email verification system
- [ ] Password reset functionality
- [ ] Two-factor authentication (2FA)
- [ ] Social media login (Google, Facebook)
- [ ] Mobile app API optimization
- [ ] Notification system (in-app & email)
- [ ] Job recommendation algorithm
- [ ] Resume parsing (CV upload)
- [ ] Video interview integration

### Version 3.0 (Future)
- [ ] AI-powered job matching
- [ ] Candidate skill assessment system
- [ ] Company analytics dashboard
- [ ] Multi-language support (i18n)
- [ ] Payment integration for premium features
- [ ] Advanced reporting & analytics
- [ ] GraphQL API support
- [ ] Microservices architecture migration

---

<div align="center">

### ⭐ If you find this project helpful, please consider giving it a star!

**Made with ❤️ by Nguyen Bao Hoan**

[⬆ Back to Top](#-jobhunter---recruitment-platform)

</div>
