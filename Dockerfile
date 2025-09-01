FROM eclipse-temurin:17-jdk AS build
COPY target/quarkus-app/lib/ /deployments/lib/
COPY target/quarkus-app/*.jar /deployments/
COPY target/quarkus-app/app/ /deployments/app/
COPY target/quarkus-app/quarkus/ /deployments/quarkus/
WORKDIR /deployments
CMD ["java", "-jar", "quarkus-run.jar"]