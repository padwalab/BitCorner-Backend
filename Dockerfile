FROM openjdk:11-jdk-slim

WORKDIR /BitCorner-Backend

# ADD . . 

EXPOSE 8080

CMD [ "./mvnw", "spring-boot:run" ]
