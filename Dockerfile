FROM openjdk:17-jdk-slim
COPY target/urlefy.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
