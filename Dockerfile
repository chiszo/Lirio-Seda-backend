# ===== ETAPA DE COMPILACIÓN =====
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests


# ===== ETAPA DE EJECUCIÓN =====
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8490

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8490"]