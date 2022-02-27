FROM openjdk:18-jdk-alpine
WORKDIR /
ADD target/countryapp-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD java -jar -Dspring.profiles.active=prod app.jar