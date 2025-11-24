FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests -Dmaven.wagon.http.retryHandler.count=5 package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV PORT=10000
EXPOSE 10000
ENTRYPOINT ["java","-jar","/app/app.jar"]
