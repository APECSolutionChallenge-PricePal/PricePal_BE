FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} PricePal.jar
ENTRYPOINT ["java","-jar","/PricePal.jar"]