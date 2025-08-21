# User Service Swagger API 문서

## 개요
User Service에 Swagger UI가 통합되어 API 문서를 쉽게 확인하고 테스트할 수 있습니다.

## 접근 방법

### 1. Swagger UI
- **URL**: `http://localhost:8080/swagger-ui.html`
- **설명**: 웹 기반 API 문서 및 테스트 인터페이스

### 2. OpenAPI JSON
- **URL**: `http://localhost:8080/api-docs`
- **설명**: OpenAPI 3.0 스펙에 따른 JSON 형태의 API 문서

## 주요 기능

### API 그룹
- **User Management**: 사용자 관리 관련 API
  - 사용자 검색
  - 사용자 생성/수정/삭제
  - 사용자 통계 조회
  - 사용자 권한 관리
  - 일괄 수정

### 상세 정보
각 API 엔드포인트는 다음 정보를 포함합니다:
- **요약**: API 기능 간단 설명
- **상세 설명**: API 동작 방식 상세 설명
- **요청/응답 스키마**: DTO 클래스 구조
- **응답 코드**: HTTP 상태 코드별 설명
- **예시 값**: 실제 사용 예시

## DTO 문서화

모든 DTO 클래스에 다음 정보가 포함되어 있습니다:
- **필드 설명**: 각 필드의 의미와 용도
- **예시 값**: 실제 데이터 예시
- **허용 값**: enum이나 제한된 값들의 목록
- **필수 여부**: 필수 입력 필드 표시

## 설정

### SwaggerConfig.java
- API 정보 (제목, 설명, 버전, 연락처, 라이선스)
- 서버 정보 (로컬, 프로덕션)

### application.properties
- API 문서 경로 설정
- Swagger UI 커스터마이징 옵션
- 정렬 및 표시 옵션

## 사용 예시

### 1. 사용자 생성
```bash
POST /api/v1/users
Content-Type: application/json

{
  "userId": "user123",
  "name": "홍길동",
  "email": "hong@example.com",
  "role": "DRIVER",
  "status": "ACTIVE"
}
```

### 2. 사용자 검색
```bash
GET /api/v1/users/search?searchKeyword=홍길동&status=ACTIVE&page=0&size=20
```

### 3. 사용자 수정
```bash
PUT /api/v1/users/1
Content-Type: application/json

{
  "name": "홍길동",
  "department": "운영팀",
  "role": "MANAGER"
}
```

## 보안

- 모든 API는 적절한 인증 및 권한 검증이 필요합니다
- Swagger UI는 개발 환경에서만 활성화하는 것을 권장합니다

## 문제 해결

### Swagger UI가 표시되지 않는 경우
1. 애플리케이션이 정상적으로 실행되었는지 확인
2. 포트 8080이 올바르게 설정되었는지 확인
3. 의존성이 올바르게 추가되었는지 확인

### API 문서가 업데이트되지 않는 경우
1. 애플리케이션 재시작
2. 캐시 클리어
3. 브라우저 새로고침

## 추가 정보

- **Spring Boot Version**: 3.5.4
- **SpringDoc OpenAPI Version**: 2.3.0
- **Java Version**: 17
- **OpenAPI Specification**: 3.0
