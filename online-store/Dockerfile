FROM openjdk:17-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы первого приложения в контейнер
COPY online-store/target/online-store-0.0.1-SNAPSHOT.jar /app/online-store.jar

# Открываем порт для первого приложения
EXPOSE 8080

# Копируем файлы второго приложения в контейнер
COPY server-jwt/target/server-jwt-0.0.1-SNAPSHOT.jar /app/server-jwt.jar

# Открываем порт для второго приложения
EXPOSE 8099

# Запускаем приложения при старте контейнера
CMD java -jar /app/online-store.jar & java -jar /app/server-jwt.jar; wait