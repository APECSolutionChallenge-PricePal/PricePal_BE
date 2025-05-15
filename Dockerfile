FROM openjdk:17
COPY build/libs/backend-0.0.1-SNAPSHOT.jar PricePal.jar
COPY .env .env
COPY src/main/resources/application.yml ./src/main/resources/application.yml
ENTRYPOINT ["java", "-jar", "PricePal.jar"]

