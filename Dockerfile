# Java 11 Base Image Layer
FROM openjdk:11

#Copy the jar into the container
COPY ./target/forex-1.0.jar forex-1.0.jar

#expose the tomcat port
EXPOSE 8080

# start the application
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","forex-1.0.jar"]