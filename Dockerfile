FROM openjdk:17
COPY build/libs/backend-0.0.1-SNAPSHOT.jar PricePal.jar
COPY src/main/resources/application.yml ./application.yml
ENTRYPOINT ["java", "-jar", "PricePal.jar"]

