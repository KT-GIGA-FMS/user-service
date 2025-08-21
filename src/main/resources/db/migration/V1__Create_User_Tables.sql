-- 사용자 테이블
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(100),
    role VARCHAR(50) NOT NULL,
    driver_license VARCHAR(50),
    vehicle_type VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

-- 사용자 권한 테이블
CREATE TABLE user_permissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    permission_name VARCHAR(100) NOT NULL,
    permission_value BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 사용자 활동 로그 테이블
CREATE TABLE user_activity_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_users_department ON users(department);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_vehicle_type ON users(vehicle_type);
CREATE INDEX idx_users_driver_license ON users(driver_license);
CREATE INDEX idx_user_permissions_user_id ON user_permissions(user_id);
CREATE INDEX idx_user_activity_logs_user_id ON user_activity_logs(user_id);
CREATE INDEX idx_user_activity_logs_created_at ON user_activity_logs(created_at);

-- 샘플 데이터 삽입
INSERT INTO users (user_id, name, email, department, role, driver_license, vehicle_type, status, created_at, created_by) VALUES
('u_001', '김사원', 'kim.sawon@company.com', '개발팀', '주임', '1종보통', '법인', 'ACTIVE', '2024-01-15 09:00:00', 'admin'),
('u_002', '이대리', 'lee.daeri@company.com', '마케팅팀', '대리', '2종보통', '개인', 'ACTIVE', '2024-02-20 09:00:00', 'admin'),
('u_003', '박과장', 'park.gwajang@company.com', '영업팀', '과장', '1종보통', '법인', 'INACTIVE', '2024-03-10 09:00:00', 'admin');

-- 샘플 권한 데이터
INSERT INTO user_permissions (user_id, permission_name, permission_value) VALUES
(1, 'USER_READ', true),
(1, 'USER_WRITE', true),
(1, 'USER_DELETE', false),
(2, 'USER_READ', true),
(2, 'USER_WRITE', true),
(2, 'USER_DELETE', false),
(3, 'USER_READ', true),
(3, 'USER_WRITE', false),
(3, 'USER_DELETE', false);

