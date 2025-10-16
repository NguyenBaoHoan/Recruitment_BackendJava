# 💼 JobHunter — Recruitment Platform Backend

JobHunter is a **backend recruitment management system** built with Spring Boot,  
providing secure authentication, job management, and real-time communication for recruiters and candidates.

---

## 🚀 Tech Stack
- **Java 21** + **Spring Boot 3**
- **Spring Security (JWT Authentication)**
- **Spring Data JPA + MySQL**
- **Spring WebSocket (STOMP protocol, SockJS)**
- **Gradle Kotlin DSL**

---

## ✨ Key Features
- 🔑 **Authentication & Security**  
  JWT-based authentication with role-based access control.

- 👤 **User & Profile Management**  
  Candidate profiles, portfolios, skills, and file upload (resume, portfolio images).

- 💼 **Job Management**  
  CRUD operations for job postings with skill & career expectation tagging.

- ⭐ **Favorite Jobs**  
  Save and track job opportunities for quick access.

- 💬 **Real-time Chat**  
  Chat rooms, instant messaging, and participant tracking using WebSocket.

---

## 📂 Project Structure
src/main/java/com/example/jobhunter/
├─ config/ # Security, WebSocket, CORS, OpenAPI
├─ controller/ # REST endpoints (Auth, User, Job, Chat, etc.)
├─ domain/ # Entities (User, Job, Message, Portfolio…)
├─ dto/ # Request/Response DTOs
├─ repository/ # Spring Data JPA repositories
├─ service/ # Business logic services
└─ JobhunterApplication.java

---

## Access API Docs
👉 http://localhost:8080/swagger-ui.html
