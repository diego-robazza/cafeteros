FROM maven:3-openjdk-8-slim as builder
ADD ./ /app
WORKDIR /app
RUN mvn package -Pdev,api-docs -DskipTests

FROM openjdk:8-jre-slim as runner

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

# Add a jhipster user to run our application so that it doesn't need to run as root
RUN adduser --shell /bin/sh --disabled-password --gecos "" jhipster
WORKDIR /home/jhipster

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown jhipster:jhipster entrypoint.sh
USER jhipster

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

COPY --from=builder /app/target/*.jar app.jar
