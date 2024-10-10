FROM openjdk:21-jdk-slim as builder

WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

COPY --from=builder /app/target/*.jar /calendar-booking.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/calendar-booking.jar"]
