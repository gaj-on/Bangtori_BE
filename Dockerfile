# ===== Build stage =====
FROM gradle:8.7.0-jdk17 AS builder
WORKDIR /app

# Gradle 캐시 최적화: 먼저 스크립트/래퍼 복사
COPY settings.gradle build.gradle ./
COPY gradle gradle
COPY gradlew gradlew

# 소스 복사
COPY src src

# 빌드 (테스트 제외)
RUN ./gradlew clean bootJar -x test --no-daemon

# ===== Runtime stage =====
FROM eclipse-temurin:17-jre

# 비루트 사용자
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

WORKDIR /app

# 빌드 산출물 가져오기 (Spring Boot bootJar)
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# Render가 주는 포트를 사용하도록
# (Render는 $PORT 환경변수를 제공)
ENV PORT=8080

# 헬스체크: 프로젝트에 있는 /api/ping 사용
# (HealthController: GET /api/ping -> "pong")
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://localhost:${PORT}/api/ping || exit 1

EXPOSE 8080

# 실행: Render의 $PORT로 서버 포트 지정
ENTRYPOINT ["java","-Dserver.port=${PORT}","-jar","app.jar"]
