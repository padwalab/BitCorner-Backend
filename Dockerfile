FROM openjdk:11-jdk-slim

ADD . . 

CMD [ "./mvnw", "spring-boot:run" ] 