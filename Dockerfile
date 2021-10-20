FROM openjdk:17-jdk-alpine
RUN mkdir "logs"
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown spring:spring logs
USER spring:spring
ARG DEPENDENCY=build/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","shine.aba.Application"]