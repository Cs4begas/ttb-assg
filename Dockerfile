FROM openjdk:21-jdk
WORKDIR /app
COPY target/assg-0.0.1-SNAPSHOT.jar assg.jar
ENTRYPOINT ["java","-jar","assg.jar"]