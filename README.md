# 💼 JobHunter — Recruitment Platform Backend

JobHunter is a **backend recruitment management system** designed to connect candidates with companies, 
featuring real-time communication, secure authentication, and modular service design.  
Built with **Spring Boot 3, PostgreSQL, Redis, and WebSocket**, this project demonstrates scalable, 
secure, and production-ready backend engineering.

---

## 🚀 Tech Stack
- **Java 21** + **Spring Boot 3**
- **Spring Security (JWT Authentication, Role-based Authorization)**
- **Spring Data JPA + PostgreSQL**
- **Redis** (caching, session & room management)
- **WebSocket (STOMP protocol, SockJS)** for real-time chat & event broadcasting
- **Gradle Kotlin DSL** for project build

---

## ✨ Key Features
- 🔑 **Authentication & Security**  
  Secure login/register with JWT, refresh tokens, and role-based access control.
  
- 👤 **User & Profile Management**  
  Manage candidates, recruiters, portfolios, and resumes with file upload support.

- 💼 **Job Management**  
  CRUD operations for job postings, skill tagging, and career expectations.

- ⭐ **Favorite Jobs**  
  Users can save and track favorite jobs with Redis caching to reduce DB load by up to **60%**.

- 💬 **Real-time Chat & Collaboration**  
  Chat rooms, instant messaging, and participant tracking powered by **Spring WebSocket + STOMP**.

- 📊 **Scalable API Design**  
  Clean separation of Controller–Service–Repository layers with DTO mapping for request/response.  
  Includes global exception handling and consistent REST response formatting.

---

## 🏗️ Architecture Overview
- **Controller Layer** → Exposes REST APIs (Auth, User, Job, Skill, Portfolio, Favorite Jobs, Chat).  
- **Service Layer** → Encapsulates business logic with modular, testable design.  
- **Repository Layer** → Database persistence via Spring Data JPA & Redis.  
- **Domain & DTOs** → Structured data models for entities, request/response payloads.  
- **Config & Security** → Global configurations (CORS, Swagger, JWT, WebSocket, Exception Handling).  

---

## 📂 Project Structure
