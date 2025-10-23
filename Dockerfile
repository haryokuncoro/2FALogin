FROM gradle:8.10.2-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon || true

COPY src ./src

# Build aplikasi (tanpa menjalankan test)
RUN gradle clean bootJar --no-daemon -x test

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/app.jar app.jar

ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
