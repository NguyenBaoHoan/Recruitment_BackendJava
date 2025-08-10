# Module Favorite Jobs

Module này cung cấp các chức năng quản lý job yêu thích cho người dùng.

## Cấu trúc Module

```
src/main/java/com/example/jobhunter/
├── domain/
│   └── FavoriteJob.java              # Entity lưu trữ job yêu thích
├── repository/
│   └── FavoriteJobRepository.java     # Repository để truy vấn dữ liệu
├── service/
│   └── FavoriteJobService.java       # Service xử lý logic nghiệp vụ
├── controller/
│   └── FavoriteJobController.java    # Controller expose API
└── dto/response/
    └── ResFavoriteJobDTO.java        # DTO trả về thông tin job yêu thích
```

## API Endpoints

### 1. Lấy danh sách job yêu thích
```
GET /api/favorite-jobs?userId={userId}
```

**Response:**
```json
{
  "status": "success",
  "message": "Lấy danh sách job yêu thích thành công",
  "data": [
    {
      "id": 1,
      "name": "Java Developer",
      "location": "Hà Nội",
      "salary": "20-30 triệu",
      "educationLevel": "BACHELOR",
      "jobType": "Full-time",
      "description": "Mô tả công việc...",
      "requirements": "Yêu cầu công việc...",
      "benefits": "Phúc lợi...",
      "workAddress": "Địa chỉ làm việc",
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00Z",
      "skills": [...],
      "favoritedAt": "2024-01-01T10:00:00Z"
    }
  ]
}
```

### 2. Thêm job vào danh sách yêu thích
```
POST /api/favorite-jobs/add?userId={userId}&jobId={jobId}
```

**Response:**
```json
{
  "status": "success",
  "message": "Thêm job vào danh sách yêu thích thành công",
  "data": null
}
```

### 3. Xóa job khỏi danh sách yêu thích
```
DELETE /api/favorite-jobs/remove?userId={userId}&jobId={jobId}
```

**Response:**
```json
{
  "status": "success",
  "message": "Xóa job khỏi danh sách yêu thích thành công",
  "data": null
}
```

### 4. Kiểm tra job có được yêu thích không
```
GET /api/favorite-jobs/check?userId={userId}&jobId={jobId}
```

**Response:**
```json
{
  "status": "success",
  "message": "Kiểm tra trạng thái yêu thích thành công",
  "data": true
}
```

### 5. Đếm số job yêu thích
```
GET /api/favorite-jobs/count?userId={userId}
```

**Response:**
```json
{
  "status": "success",
  "message": "Đếm số job yêu thích thành công",
  "data": 5
}
```

## Cài đặt Database

Chạy script SQL để tạo bảng:

```sql
-- Tạo bảng favorite_jobs
CREATE TABLE IF NOT EXISTS favorite_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_job (user_id, job_id)
);

-- Tạo index để tối ưu truy vấn
CREATE INDEX idx_favorite_jobs_user_id ON favorite_jobs(user_id);
CREATE INDEX idx_favorite_jobs_job_id ON favorite_jobs(job_id);
```

## Test API

Sử dụng script test có sẵn:

```bash
# Chạy test script
./test_favorite_jobs_api.sh
```

Hoặc test thủ công:

```bash
# Lấy danh sách job yêu thích
curl -X GET "http://localhost:8080/api/favorite-jobs?userId=1"

# Thêm job vào yêu thích
curl -X POST "http://localhost:8080/api/favorite-jobs/add?userId=1&jobId=1"

# Xóa job khỏi yêu thích
curl -X DELETE "http://localhost:8080/api/favorite-jobs/remove?userId=1&jobId=1"
```

## Tính năng

- ✅ Lấy danh sách job yêu thích của user
- ✅ Thêm job vào danh sách yêu thích
- ✅ Xóa job khỏi danh sách yêu thích
- ✅ Kiểm tra trạng thái yêu thích
- ✅ Đếm số job yêu thích
- ✅ Validation và error handling
- ✅ Clean Architecture pattern
- ✅ RESTful API design

## Lưu ý

1. Module này tuân theo Clean Architecture pattern hiện tại của dự án
2. Sử dụng JPA/Hibernate để quản lý dữ liệu
3. Có validation và error handling đầy đủ
4. API trả về response format chuẩn của dự án
5. Có unique constraint để tránh duplicate favorite
6. Có cascade delete khi user hoặc job bị xóa 