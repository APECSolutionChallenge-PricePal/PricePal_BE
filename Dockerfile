FROM openjdk:17
COPY build/libs/backend-0.0.1-SNAPSHOT.jar PricePal.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "PricePal.jar"]

