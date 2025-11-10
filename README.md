# Third Backend - Spring Boot Application

Swagger/OpenAPI가 통합된 Spring Boot REST API 애플리케이션입니다.

## 요구사항

- Java 17 이상
- Gradle 7.x 이상 (또는 포함된 Gradle Wrapper 사용)

## 실행 방법

### Gradle을 사용한 실행

```bash
gradle bootRun
```

또는 Gradle Wrapper 사용:

```bash
./gradlew bootRun
```

### JAR 파일 빌드 후 실행

```bash
gradle clean build
java -jar build/libs/third-backend-0.0.1-SNAPSHOT.jar
```

## API 문서

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## API 엔드포인트

### Hello API

- `GET /hello` - "Hello Spring Boot!" 메시지를 반환합니다.

### User API

- `GET /api/users` - 모든 사용자 목록 조회
- `GET /api/users/{id}` - 특정 사용자 조회
- `POST /api/users` - 새 사용자 생성
- `PUT /api/users/{id}` - 사용자 정보 수정
- `DELETE /api/users/{id}` - 사용자 삭제

## 사용 예제

### 모든 사용자 조회

```bash
curl http://localhost:8080/api/users
```

### 특정 사용자 조회

```bash
curl http://localhost:8080/api/users/1
```

### 새 사용자 생성

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "박민수",
    "email": "park@example.com",
    "age": 27
  }'
```

### 사용자 정보 수정

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "email": "hong.updated@example.com",
    "age": 26
  }'
```

### 사용자 삭제

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 기술 스택

- Spring Boot 3.2.0
- SpringDoc OpenAPI 2.3.0
- Java 17
