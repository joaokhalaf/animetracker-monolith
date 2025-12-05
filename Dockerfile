FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

COPY . .

RUN npm install && npm run build:css

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/AniTracker-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
