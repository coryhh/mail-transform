FROM maven:3-openjdk-8-slim as builder
COPY . /usr/src/app/
WORKDIR /usr/src/app
RUN mvn clean package -DskipTests=true -Pjar

FROM eclipse-temurin:8-jre
COPY --from=builder /usr/src/app/mail/mail.jar mail.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /mail.jar"]
