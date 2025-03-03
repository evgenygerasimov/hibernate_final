FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/hibernate_final-1.0-SNAPSHOT.jar hibernate_final.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hibernate_final.jar"]