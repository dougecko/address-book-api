FROM openjdk:17-jdk-alpine
RUN mkdir "logs"
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown spring:spring logs
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]