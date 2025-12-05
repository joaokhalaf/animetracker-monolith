FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN if [ -f package.json ]; then npm install && npm run build:css; fi

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/AniTracker-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
