FROM openjdk:17
<<<<<<< HEAD
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} PricePal.jar
ENTRYPOINT ["java","-jar","/PricePal.jar"]
COPY .env .env
=======
COPY build/libs/backend-0.0.1-SNAPSHOT.jar PricePal.jar
ENTRYPOINT ["java", "-jar", "PricePal.jar"]
COPY .env .env
>>>>>>> 96d55b1 (Fix: update Dockerfile and .env for Docker deployment)
