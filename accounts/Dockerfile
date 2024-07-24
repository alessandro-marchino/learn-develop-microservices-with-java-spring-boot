# Start with a base image containing Java runtime
FROM eclipse-temurin:21-jre-alpine

# Information about who maintains this image
MAINTAINER eazybytes.com

# Add the application's JAR to the image'
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
