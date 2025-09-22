🎯 JobHunter - Enterprise Recruitment Platform
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![JWT](https://img.shields.io/badge/JWT-Security-red.svg)
![WebSocket](https://img.shields.io/badge/WebSocket-Realtime-yellow.svg)
📋 Tổng Quan
JobHunter là một nền tảng tuyển dụng doanh nghiệp được xây dựng với Spring Boot, cung cấp các tính năng quản lý việc làm, ứng viên, công ty và hệ thống chat realtime. Hệ thống được thiết kế với kiến trúc microservices, bảo mật JWT và hỗ trợ đa người dùng.
🚀 Tính Năng Chính
🔐 Authentication & Authorization
JWT Authentication với Access Token và Refresh Token
Role-based Access Control (USER, ADMIN, RECRUITER)
Spring Security 6 với OAuth2 Resource Server
Password Encryption với BCrypt
�� User Management
Đăng ký và đăng nhập người dùng
Quản lý profile và portfolio
Upload CV và hình ảnh
Trạng thái tìm việc (Ready Now, Within Month, etc.)
🏢 Company Management
Quản lý thông tin công ty
Liên kết người dùng với công ty
CRUD operations cho companies
💼 Job Management
Đăng tin tuyển dụng chi tiết
Quản lý kỹ năng yêu cầu
Tìm kiếm và lọc việc làm
Trạng thái công việc (Active/Inactive)
⭐ Favorite Jobs
Lưu việc làm yêu thích
Quản lý danh sách ứng tuyển
Tracking ứng viên
💬 Real-time Chat System
WebSocket chat realtime
Tin nhắn đa định dạng (TEXT, IMAGE, AUDIO, VIDEO, FILE)
Phòng chat riêng tư và công khai
Lịch sử tin nhắn
📁 File Management
Upload và quản lý file
Hỗ trợ nhiều định dạng (PDF, DOC, Images)
Lưu trữ CV và portfolio
🏗️ Kiến Trúc Hệ Thống
<img width="642" height="423" alt="image" src="https://github.com/user-attachments/assets/47546c76-3c6e-4ed7-873c-564037dc1458" />
📊 Database Schema
Core Entities
Users - Thông tin người dùng và ứng viên
Companies - Thông tin công ty
Jobs - Tin tuyển dụng
Skills - Kỹ năng và yêu cầu
Resumes - CV và hồ sơ ứng tuyển
Chat System
ChatRooms - Phòng chat
Messages - Tin nhắn
Participants - Thành viên phòng chat
Additional Features
FavoriteJobs - Việc làm yêu thích
UserPortfolio - Portfolio người dùng
CareerExpectation - Mong muốn nghề nghiệp
🛠️ Công Nghệ Sử Dụng
Backend
Spring Boot 3.4.1 - Framework chính
Spring Security 6 - Bảo mật và authentication
Spring Data JPA - ORM và database access
Spring WebSocket - Real-time communication
MySQL 8.0 - Database chính
JWT (jjwt 0.11.5) - Token-based authentication
Development Tools
Lombok - Giảm boilerplate code
Gradle - Build tool
SpringDoc OpenAPI - API documentation
Spring Boot DevTools - Development support
Testing
JUnit 5 - Unit testing
Spring Boot Test - Integration testing
H2 Database - Test database
                                        **📦 Cài Đặt và Chạy**
Yêu Cầu Hệ Thống
Java 17+
MySQL 8.0+
Gradle 8.0+
**Clone Repository**
git clone https://github.com/your-username/jobhunter.git
cd jobhunter
**Cấu Hình Application**
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/jobhunter
spring.datasource.username=jobhunter_user
spring.datasource.password=StrongPassword123!

**Cấu Hình Application**

# Build project
./gradlew build

# Chạy application
./gradlew bootRun

# Hoặc sử dụng JAR
java -jar build/libs/jobhunter-0.0.1-SNAPSHOT.jar

**Kiểm Tra**

API Documentation: http://localhost:8080/swagger-ui.html
Health Check: http://localhost:8080/actuator/health
WebSocket: ws://localhost:8080/ws
⭐ Nếu project này hữu ích, hãy cho chúng tôi một star! ⭐

