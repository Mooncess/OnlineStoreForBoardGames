FROM openjdk:17-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы приложения в контейнер
COPY server-jwt/target/server-jwt-0.0.1-SNAPSHOT.jar /app

# Открываем порт, на котором будет работать приложение
EXPOSE 8099

# Запускаем приложение при старте контейнера
CMD ["java", "-jar", "server-jwt-0.0.1-SNAPSHOT.jar"]