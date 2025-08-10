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