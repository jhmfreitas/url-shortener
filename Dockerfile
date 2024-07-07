# Use the official Maven image to create a build artifact
FROM maven:3.8.4-openjdk-17 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:17-jdk-slim
COPY --from=build /usr/src/app/target/url-shortener-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
