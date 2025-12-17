FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copier le jar renommé dans target
COPY target/eya.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
