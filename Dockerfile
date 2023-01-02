FROM adoptopenjdk:14-jre-hotspot

COPY build/libs/irrigation-system-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

