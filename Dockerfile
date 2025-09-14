FROM gradle:8.10.2-jdk21-alpine AS build

WORKDIR /app
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
COPY gradlew ./
RUN gradle dependencies --no-daemon

COPY src ./src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -G appuser -u 1001

WORKDIR /app

COPY --chown=appuser:appuser --from=build /app/build/libs/*.jar app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]