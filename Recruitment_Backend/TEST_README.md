# JobHunter Testing Suite

Bộ test toàn diện cho ứng dụng JobHunter bao gồm Unit Tests, Integration Tests và E2E Tests với Playwright.

## 📋 Tổng quan Test Cases

### JUnit Tests (10 Test Cases)

#### 1. UserServiceTest (10 test cases)
- ✅ Test 1: Tạo user thành công với thông tin hợp lệ
- ✅ Test 2: Kiểm tra email đã tồn tại  
- ✅ Test 3: Fetch user theo ID thành công
- ✅ Test 4: Update user thành công
- ✅ Test 5: Delete user thành công
- ✅ Test 6: Convert User to ResCreateUserDTO
- ✅ Test 7: Convert User to ResUserDTO
- ✅ Test 8: Đổi mật khẩu thành công
- ✅ Test 9: Đổi mật khẩu thất bại - sai mật khẩu cũ
- ✅ Test 10: Đổi mật khẩu thất bại - user không tồn tại

#### 2. JobServiceTest (5 test cases đã có + bổ sung)
- ✅ Test 1: Tạo job thành công với đầy đủ thông tin
- ✅ Test 2: Tạo job thất bại khi tên rỗng
- ✅ Test 3: Tạo job thất bại khi skill không tồn tại
- ✅ Test 4: Fetch job theo ID thành công
- ✅ Test 5: Delete job thành công

#### 3. CompanyServiceTest (5 test cases)
- ✅ Test 1: Tạo company thành công
- ✅ Test 2: Update company thành công
- ✅ Test 3: Delete company thành công
- ✅ Test 4: Fetch all companies với pagination
- ✅ Test 5: Tạo company với tên trống - thất bại

#### 4. FavoriteJobServiceTest (10 test cases)
- ✅ Test 1: Thêm job vào danh sách yêu thích thành công
- ✅ Test 2: Thêm job đã yêu thích - ném exception
- ✅ Test 3: Xóa job khỏi danh sách yêu thích thành công
- ✅ Test 4: Xóa job không tồn tại trong danh sách yêu thích
- ✅ Test 5: Kiểm tra job có được yêu thích không - True
- ✅ Test 6: Kiểm tra job có được yêu thích không - False
- ✅ Test 7: Đếm số job yêu thích của user
- ✅ Test 8: Lấy danh sách job yêu thích của user
- ✅ Test 9: Thêm job với user không tồn tại
- ✅ Test 10: Thêm job với job không tồn tại

### Integration Tests (10 test cases)

#### 1. JobIntegration (4 test cases đã có)
- ✅ Integration Test 1: Tạo job và lưu vào database
- ✅ Integration Test 2: Update job trong database
- ✅ Integration Test 3: Delete job từ database
- ✅ Integration Test 4: Fetch all jobs với pagination

#### 2. UserIntegrationTest (5 test cases)
- ✅ Integration Test 1: Tạo user và lưu vào database
- ✅ Integration Test 2: Update user trong database
- ✅ Integration Test 3: Delete user từ database
- ✅ Integration Test 4: Kiểm tra email đã tồn tại
- ✅ Integration Test 5: Đổi mật khẩu trong database

#### 3. CompanyIntegrationTest (5 test cases)
- ✅ Integration Test 1: Tạo company và lưu vào database
- ✅ Integration Test 2: Update company trong database
- ✅ Integration Test 3: Delete company từ database
- ✅ Integration Test 4: Fetch all companies với pagination
- ✅ Integration Test 5: Company với users relationship

### Playwright E2E Tests (20 test cases)

#### 1. LoginPlaywrightTest (5 test cases)
- ✅ Playwright Test 1: Đăng nhập thành công với thông tin hợp lệ
- ✅ Playwright Test 2: Đăng nhập thất bại với email sai
- ✅ Playwright Test 3: Kiểm tra validation form login
- ✅ Playwright Test 4: Chuyển đến trang register
- ✅ Playwright Test 5: Kiểm tra Google Login button

#### 2. JobSearchPlaywrightTest (5 test cases)
- ✅ Playwright Test 6: Tìm kiếm job với từ khóa
- ✅ Playwright Test 7: Lọc job theo địa điểm
- ✅ Playwright Test 8: Xem chi tiết job
- ✅ Playwright Test 9: Thêm job vào danh sách yêu thích
- ✅ Playwright Test 10: Phân trang danh sách job

#### 3. DashboardPlaywrightTest (5 test cases)
- ✅ Playwright Test 11: Kiểm tra dashboard load thành công
- ✅ Playwright Test 12: Kiểm tra sidebar navigation
- ✅ Playwright Test 13: Kiểm tra user profile dropdown
- ✅ Playwright Test 14: Kiểm tra logout functionality
- ✅ Playwright Test 15: Kiểm tra dashboard stats/widgets

#### 4. UserProfilePlaywrightTest (5 test cases)
- ✅ Playwright Test 16: Cập nhật thông tin profile
- ✅ Playwright Test 17: Đổi mật khẩu
- ✅ Playwright Test 18: Upload avatar
- ✅ Playwright Test 19: Cập nhật career expectations
- ✅ Playwright Test 20: Cập nhật notification settings

## 🚀 Cách chạy Tests

### 1. Chạy tất cả tests
```bash
./gradlew test
```

### 2. Chạy riêng Unit Tests
```bash
./gradlew test --tests "com.example.jobhunter.service.*"
```

### 3. Chạy riêng Integration Tests
```bash
./gradlew test --tests "com.example.jobhunter.Integration.*"
```

### 4. Chạy riêng Playwright Tests
```bash
./gradlew playwrightTest
```

### 5. Chạy Playwright Tests với browser hiển thị
```bash
./gradlew playwrightTestHeaded
```

### 6. Chạy test của một class cụ thể
```bash
./gradlew test --tests "com.example.jobhunter.service.UserServiceTest"
```

## 📋 Yêu cầu

### Để chạy Unit & Integration Tests:
- ✅ Java 21
- ✅ Spring Boot 3.4.1
- ✅ H2 Database (cho test)
- ✅ JUnit 5
- ✅ Mockito

### Để chạy Playwright Tests:
- ✅ Frontend server chạy trên `http://localhost:5173`
- ✅ Backend server chạy trên `http://localhost:8080`
- ✅ Playwright browsers được install

### Install Playwright browsers:
```bash
./gradlew playwrightInstall
```

## 🔧 Cấu hình

### Test Properties
File `src/test/resources/application.properties` chứa cấu hình test:
- H2 in-memory database
- JWT test settings
- Playwright URLs

### Gradle Configuration
File `build.gradle.kts` đã được cấu hình với:
- JUnit Platform
- Playwright dependencies
- Test tasks với JVM arguments
- Playwright install task

## 📊 Test Coverage

### Các module được test:
- ✅ **UserService**: CRUD operations, password management, validation
- ✅ **JobService**: Job management, search, skills integration
- ✅ **CompanyService**: Company CRUD, pagination
- ✅ **FavoriteJobService**: Favorite job management
- ✅ **Frontend UI**: Login, job search, dashboard, profile
- ✅ **Database Integration**: Real database operations
- ✅ **API Endpoints**: RESTful API testing

### Test Types:
- ✅ **Unit Tests**: Isolated service testing với Mockito
- ✅ **Integration Tests**: Database integration với Spring Boot
- ✅ **E2E Tests**: Full user journey với Playwright
- ✅ **UI Tests**: Frontend interaction testing
- ✅ **API Tests**: Backend endpoint testing

## 🎯 Best Practices

1. **Page Object Model**: Playwright tests sử dụng POM pattern
2. **Base Test Class**: Shared setup/teardown logic
3. **Test Data**: Isolated test data cho mỗi test
4. **Assertions**: Comprehensive assertions với meaningful messages
5. **Error Handling**: Proper exception testing
6. **Mock Objects**: Clean mocking với Mockito
7. **Test Isolation**: Mỗi test độc lập và có thể chạy riêng lẻ

## 📝 Notes

- Tests được thiết kế để chạy trong CI/CD pipeline
- Playwright tests có thể chạy headless hoặc headed mode
- Database được reset cho mỗi integration test
- Mock objects được sử dụng cho unit tests
- Page Object Model giúp maintain tests dễ dàng hơn