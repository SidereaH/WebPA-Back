# Этап 1: Сборка приложения
FROM maven:3.9-eclipse-temurin-21 as builder

WORKDIR /app

# Копируем сначала только pom.xml для кэширования зависимостей
COPY pom.xml .
# Добавляем репозитории снапшотов Spring
RUN mkdir -p ~/.m2 && \
    echo '<settings><profiles><profile><id>spring-snapshots</id><repositories><repository><id>spring-snapshots</id><url>https://repo.spring.io/snapshot</url></repository></repositories></profile></profiles><activeProfiles><activeProfile>spring-snapshots</activeProfile></activeProfiles></settings>' > ~/.m2/settings.xml

# Загружаем зависимости отдельно (будут закэшированы)
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Этап 2: Создание рабочего образа
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Копируем JAR из этапа сборки
COPY --from=builder /app/target/*.jar app.jar

# Внешний порт для приложения
EXPOSE 8080

# Настройки Java для контейнера
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SPRING_PROFILES_ACTIVE=default

# Команда запуска
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
