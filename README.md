Hibernate Final Project

Этот проект представляет собой приложение на Java, использующее Hibernate для работы с базами данных MySQL и Redis. 
Цель работы приложения - эксперимент по выявлению разницы времени выполнения кэшированных и не кэшированных запросов.
Он включает в себя Docker-образы для контейнеризации приложения и зависимостей, таких как база данных и кеш Redis.

Требования:

Java 21 или выше.

Docker и Docker Compose.

Maven для сборки проекта. 

Установка и запуск
1. Клонирование репозитория
   
Для начала, клонируй репозиторий на свою машину:

git clone https://github.com/evgenygerasimov/hibernate_final.git

cd hibernate_final

3. Сборка приложения
   
Перед запуском убедись, что приложение собрано. Используй Maven для сборки JAR файла:

mvn clean install

После успешной сборки JAR файл будет находиться в папке target/.

5. Запуск с использованием Docker Compose
   
Для запуска контейнеров, включая базу данных и Redis, а также самого приложения, используем Docker Compose. Просто выполните команду:

docker-compose up -d

Docker Compose автоматически поднимет контейнеры для базы данных, Redis и приложение.

7. Прекращение работы
   
Для остановки контейнеров выполните команду:

docker-compose down

Настройка базы данных:

Для инициализации базы данных используется скрипт init.sql. Он будет автоматически выполнен при старте контейнера с MySQL.

Используемые технологии:

Hibernate ORM — для работы с базой данных.

MySQL — для хранения данных.

Redis — для кеширования.

Docker — для контейнеризации приложения и зависимостей.

Java 21 — основной язык программирования.

Примечания:

При первом запуске контейнеров может потребоваться немного времени, чтобы MySQL был готов к подключению.
Контейнеры можно настроить для автоматического восстановления данных при рестарте с помощью дополнительных опций Docker.
