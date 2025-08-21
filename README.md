# KT Giga FMS - User Service

사용자 관리 서비스는 KT Giga FMS의 MSA 아키텍처에서 사용자 정보를 관리하는 마이크로서비스입니다.

## 🚀 주요 기능

### 사용자 관리 대시보드
- **사용자 검색**: 이름, 사용자ID, 부서별 검색
- **필터링**: 상태, 역할, 부서, 차량구분, 면허별 필터링
- **사용자 등록**: 새로운 사용자 생성
- **일괄 수정**: 다수 사용자 동시 수정
- **권한 관리**: 사용자별 권한 설정 및 관리

### 통계 대시보드
- 전체 사용자 수
- 활성 사용자 수
- 법인 차량 사용자 수
- 1종 면허 보유자 수

## 🏗️ 기술 스택

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Flyway (데이터베이스 마이그레이션)**
- **Lombok**
- **Swagger/OpenAPI 3**

## 📋 API 엔드포인트

### 1. 사용자 검색 (대시보드 메인)
```
GET /api/v1/users/search
```
**Query Parameters:**
- `searchKeyword`: 이름, 사용자ID, 부서 검색
- `status`: 전체 상태 필터
- `role`: 역할 필터
- `department`: 부서 필터
- `vehicleType`: 차량구분 필터
- `driverLicense`: 면허 필터
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 20)
- `sortBy`: 정렬 기준 (기본값: createdAt)
- `sortDirection`: 정렬 방향 (기본값: DESC)

**응답 예시:**
```json
{
  "users": [
    {
      "id": 1,
      "userId": "u_001",
      "name": "김사원",
      "role": "주임",
      "department": "개발팀",
      "driverLicense": "1종보통",
      "vehicleType": "법인",
      "status": "ACTIVE",
      "createdAt": "2024-01-15T09:00:00"
    }
  ],
  "totalElements": 3,
  "totalPages": 1,
  "currentPage": 0,
  "size": 20,
  "statistics": {
    "totalUsers": 3,
    "activeUsers": 2,
    "corporateVehicleUsers": 2,
    "class1LicenseHolders": 2
  }
}
```

### 2. 사용자 통계 조회
```
GET /api/v1/users/statistics
```

### 3. 사용자 생성
```
POST /api/v1/users
```
**Request Body:**
```json
{
  "userId": "u_004",
  "name": "최사원",
  "email": "choi.sawon@company.com",
  "department": "인사팀",
  "role": "주임",
  "driverLicense": "2종보통",
  "vehicleType": "개인",
  "status": "ACTIVE",
  "permissions": ["USER_READ", "USER_WRITE"]
}
```

### 4. 사용자 수정
```
PUT /api/v1/users/{id}
```

### 5. 사용자 삭제
```
DELETE /api/v1/users/{id}
```

### 6. 일괄 수정
```
PUT /api/v1/users/batch
```
**Request Body:**
```json
{
  "userIds": [1, 2, 3],
  "status": "ACTIVE",
  "department": "개발팀"
}
```

### 7. 사용자 권한 조회
```
GET /api/v1/users/{id}/permissions
```

## 🗄️ 데이터베이스 스키마

### Users 테이블
- `id`: 기본키
- `user_id`: 사용자 ID (고유)
- `name`: 이름
- `email`: 이메일
- `phone`: 전화번호
- `department`: 부서
- `role`: 역할
- `driver_license`: 운전면허
- `vehicle_type`: 차량구분
- `status`: 상태 (ACTIVE, INACTIVE, SUSPENDED, DELETED)
- `created_at`: 생성일시
- `updated_at`: 수정일시
- `created_by`: 생성자
- `updated_by`: 수정자

### User_Permissions 테이블
- `id`: 기본키
- `user_id`: 사용자 ID (외래키)
- `permission_name`: 권한명
- `permission_value`: 권한값

### User_Activity_Logs 테이블
- `id`: 기본키
- `user_id`: 사용자 ID (외래키)
- `action`: 액션
- `details`: 상세내용
- `ip_address`: IP 주소
- `user_agent`: 사용자 에이전트
- `created_at`: 생성일시

## 🚀 실행 방법

### 1. 데이터베이스 설정
PostgreSQL 데이터베이스를 생성하고 `application.properties`의 설정을 수정합니다.

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. API 문서 확인
- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs

## 🔧 설정

### application.properties 주요 설정
```properties
spring.application.name=user-service
server.port=8081

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/user_service_db
spring.datasource.username=postgres
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Security
jwt.secret=kt-giga-fms-user-service-secret-key-2024
jwt.expiration=86400000
```

## 📊 샘플 데이터

프로젝트 실행 시 자동으로 다음 샘플 데이터가 생성됩니다:

1. **김사원** (u_001) - 개발팀 주임, 1종보통, 법인차량
2. **이대리** (u_002) - 마케팅팀 대리, 2종보통, 개인차량  
3. **박과장** (u_003) - 영업팀 과장, 1종보통, 법인차량

## 🔐 보안

- Spring Security를 통한 인증/인가
- JWT 토큰 기반 인증
- CORS 설정으로 프론트엔드 연동 지원
- 사용자 활동 로그 기록

## 📝 개발 가이드

### 새로운 기능 추가 시
1. Entity 클래스 생성
2. DTO 클래스 생성
3. Repository 인터페이스 생성
4. Service 클래스에 비즈니스 로직 구현
5. Controller에 API 엔드포인트 추가
6. 테스트 코드 작성

### 코드 컨벤션
- Lombok 어노테이션 사용
- Builder 패턴으로 객체 생성
- 트랜잭션 관리 (@Transactional)
- 로깅 (SLF4J)

## 🤝 MSA 연동

이 서비스는 APIM(API Management)을 통해 다른 서비스와 연동됩니다:

- **API Gateway**: 통합 API 엔드포인트 제공
- **Service Discovery**: 서비스 등록 및 발견
- **Load Balancing**: 로드 밸런싱
- **Circuit Breaker**: 장애 격리
- **Monitoring**: 서비스 모니터링

## 📞 문의

- **개발팀**: dev@kt-giga-fms.com
- **기술지원**: support@kt-giga-fms.com
- **프로젝트**: https://github.com/kt-giga-fms/user-service

