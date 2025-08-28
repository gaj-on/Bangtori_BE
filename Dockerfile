# ===== Build stage =====
FROM gradle:8.7.0-jdk17 AS builder
WORKDIR /app

# Gradle 관련 파일 먼저 복사(캐시 최적화)
COPY settings.gradle build.gradle ./
COPY gradle gradle
COPY gradlew gradlew

# gradlew 실행권한 + (윈도우 개행 제거)
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# 소스 복사
COPY src src

# 빌드 (테스트 제외)
RUN ./gradlew clean bootJar -x test --no-daemon

# ===== Runtime stage =====
FROM eclipse-temurin:17-jre
WORKDIR /app

# healthcheck에 사용할 curl 설치 (선택)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 비루트 사용자
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

# 산출물 복사 (bootJar 결과)
COPY --from=builder /app/build/libs/*.jar app.jar

# Render가 제공하는 포트 사용
ENV PORT=8080
EXPOSE 8080

# 헬스체크 (엔드포인트는 프로젝트에 맞게 조정)
# 예: GET /api/ping -> 200
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -fsS http://localhost:${PORT}/api/ping || exit 1

# 중요: $PORT 치환을 위해 sh -c 사용
ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar app.jar"]
