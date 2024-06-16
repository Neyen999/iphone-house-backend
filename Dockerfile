FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

ENV TZ=America/Argentina/Buenos_Aires
ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /target/iphone-house-0.0.1-SNAPSHOT.jar iphone-house.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "iphone-house.jar"]