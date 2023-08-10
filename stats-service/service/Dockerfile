FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar service.jar
ENTRYPOINT ["java","-jar","/service.jar"]