FROM bellsoft/liberica-openjdk-alpine:21 AS builder

WORKDIR /application
COPY . .
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests -Dmaven.repo.local=/tmp/m2-repository

FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app
COPY --from=builder /application/target/*.jar /app/app.jar
VOLUME /tmp

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/NotificationSystem3728
ENV app.sounds.path=/data/sounds

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
