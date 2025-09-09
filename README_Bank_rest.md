# Система управления банковскими картами

📋 Оглавление
Описание проекта

Функциональность

Технологии

Требования

Установка и запуск

API Документация

База данных

Безопасность

Разработчики

🎯 Описание проекта
RESTful API для управления банковскими картами, пользователями и денежными переводами. Система предоставляет полный цикл работы с банковскими картами включая создание, блокировку, переводы между картами и административное управление.

✨ Функциональность
🔐 Аутентификация и авторизация
Регистрация новых пользователей

JWT аутентификация

Ролевая модель (USER, ADMIN)

Выход из системы с инвалидацией токенов

💳 Управление картами
Создание карт (DEBIT, CREDIT, PREPAID, VIRTUAL)

Просмотр списка карт с фильтрацией и пагинацией

Блокировка/разблокировка карт

Проверка баланса

Автоматическая генерация номеров карт

💸 Денежные переводы
Переводы между собственными картами

История транзакций

Статусы операций (PENDING, COMPLETED, FAILED)

Валидация операций

👨‍💼 Административные функции
Просмотр всех пользователей

Управление картами пользователей

Мониторинг транзакций

🛠 Технологии
Backend
Java 17 - основной язык программирования

Spring Boot 3.2.0 - фреймворк

Spring Security - аутентификация и авторизация

Spring Data JPA - работа с базой данных

JWT - JSON Web Tokens

Lombok - сокращение boilerplate кода

Validation API - валидация данных

База данных
PostgreSQL - production база данных

H2 Database - in-memory база для разработки

Документация
SpringDoc OpenAPI 3 - Swagger документация

RESTful API - стандартные HTTP методы

Безопасность
BCrypt - хеширование паролей

JWT - токены аутентификации

HTTPS Ready - готова к использованию HTTPS

📋 Требования
Системные требования
Java 17 или выше

Maven 3.6+ или Gradle 7+

PostgreSQL 12+ (для production)

512MB RAM минимум

1GB свободного места на диске

Переменные окружения
bash
# Обязательные
JAVA_HOME=/path/to/java17

# Опциональные (для production)
DATABASE_URL=jdbc:postgresql://localhost:5432/bankdb
DATABASE_USERNAME=bankuser
DATABASE_PASSWORD=bankpassword
🚀 Установка и запуск
1. Клонирование репозитория
   bash
   git clone <repository-url>
   cd card-manager
2. Настройка базы данных
   Для разработки (H2):
   Автоматически настраивается, данные хранятся в памяти.

Для production (PostgreSQL):
bash
# Создание базы данных
createdb bankdb

# Создание пользователя
createuser bankuser -P
# Введите пароль при запросе

# Настройка прав
psql -c "GRANT ALL PRIVILEGES ON DATABASE bankdb TO bankuser;"
3. Настройка конфигурации

properties
spring:
config:
activate:
on-profile: dev
datasource:
url: jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
username: sa
password: password
driver-class-name: org.h2.Driver
jpa:
database-platform: org.hibernate.dialect.H2Dialect
hibernate:
ddl-auto: create-drop
h2:
console:
enabled: true
path: /h2-console

encryption:
key: dev-encryption-key-32-bytes-here
4. Сборка проекта
   С помощью Maven:
   bash
   mvn clean package
   С помощью Gradle:
   bash
   ./gradlew build
5. Запуск приложения
   Разработка:
   bash
   mvn spring-boot:run
   Production:
   bash
   java -jar target/card-manager-1.0.0.jar
   С профилем production:
   bash
   java -jar target/card-manager-1.0.0.jar --spring.profiles.active=prod
6. Проверка работоспособности
   После запуска откройте в браузере:

Swagger UI: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console (только для разработки)

Health Check: http://localhost:8080/actuator/health

📖 API Документация
Базовый URL
text
http://localhost:8080/api
Аутентификация
Все запросы (кроме аутентификации) требуют JWT токен в заголовке:

http
Authorization: Bearer <your-jwt-token>
Основные endpoints
🔐 Аутентификация
http
POST /auth/register
POST /auth/login
POST /auth/logout
💳 Карты
http
GET  /cards
POST /cards
PATCH /cards/{id}/block
💸 Переводы
http
POST /transfers
GET  /transfers/{id}
👨‍💼 Администрирование
http
GET /admin/users
Примеры запросов
Регистрация пользователя
bash
curl -X POST "http://localhost:8080/api/auth/register" -H "Content-Type: application/json" -d "{ \"username\": \"john_doe\", \"password\": \"password123\", \"email\": \"test@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\" }"
Вход в систему
bash
curl -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d "{ \"username\": \"john_doe\", \"password\": \"password123\" }"
Аутентификация
Logout
bash
curl -X POST "http://localhost:8080/api/auth/logout" \
-H "Authorization: Bearer <jwt-token>"
Создание карты (требует админ права)
bash
curl -X POST "http://localhost:8080/api/cards" \
-H "Authorization: Bearer <jwt-token>" \
-H "Content-Type: application/json" \
-d '{
"cardHolderName": "JOHN DOE",
"type": "DEBIT",
"currency": "USD",
"initialBalance": 1000.00
}'
Получить детали карты
bash
curl -X GET "http://localhost:8080/api/cards" \
-H "Authorization: Bearer <jwt-token>"
Переводы
Создать перевод между своими картами
bash
curl -X POST "http://localhost:8080/api/transfers" \
-H "Authorization: Bearer <jwt-token>" \
-H "Content-Type: application/json" \
-d '{
"fromCardId": 1,
"toCardId": 2,
"amount": 500.00,
"currency": "USD",
"description": "Monthly savings transfer"
}'
Получить детали перевода
bash
curl -X GET "http://localhost:8080/api/transfers/1" \
-H "Authorization: Bearer <jwt-token>"
Заблокировать карту
bash
curl -X PATCH "http://localhost:8080/api/cards/1/block" \
-H "Authorization: Bearer <jwt-token>"
Администрирование
Получить всех пользователей (только для админов)
bash
curl -X GET "http://localhost:8080/api/admin/users" \
-H "Authorization: Bearer <admin-jwt-token>"
🗄️ База данных
Диаграмма базы данных
text
users
├── id (PK)
├── username (UK)
├── password
├── email (UK)
├── first_name
├── last_name
├── role
├── is_active
├── created_at
└── updated_at

cards
├── id (PK)
├── card_number (UK)
├── card_holder_name
├── expiration_date
├── cvv (encrypted)
├── balance
├── currency
├── status
├── card_type
├── user_id (FK → users)
├── created_at
└── updated_at

transactions
├── id (PK)
├── transaction_id (UK)
├── from_card_id (FK → cards)
├── to_card_id (FK → cards)
├── amount
├── currency
├── status
├── description
├── fee
├── merchant_name
├── created_at
└── updated_at
Миграции базы данных
Приложение использует автоматическое создание схемы через Hibernate. Для production рекомендуется использовать Liquibase или Flyway.

🔒 Безопасность
Меры безопасности
Хеширование паролей - BCrypt с salt

JWT токены - срок действия 24 часа

HTTPS - готовность к использованию SSL/TLS

CORS - настройка запросов

SQL инъекции - защита через JPA

XSS - валидация входных данных

Ролевая модель
USER - базовые операции с картами и переводами

ADMIN - полный доступ ко всем функциям

Best Practices
Никогда не храните чувствительные данные в логах

Используйте HTTPS в production

Регулярно обновляйте зависимости

Мониторинг и логирование security событий

🧪 Тестирование
Запуск тестов
bash
mvn test
Тестовое покрытие
Unit тесты сервисов

Интеграционные тесты контроллеров

Тесты безопасности

Тесты валидации

📊 Мониторинг
Health check
http
GET /actuator/health
Metrics
http
GET /actuator/metrics
Интеграция с Prometheus/Grafana
Готова к настройке метрик для мониторинга.

🚀 Deployment
Docker контейнеризация
dockerfile
FROM openjdk:17-jdk-slim
COPY target/card-manager-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
Kubernetes развертывание
Готовы манифесты для развертывания в Kubernetes кластере.

🐛 Troubleshooting
Common issues
Port already in use

bash
netstat -tulpn | grep :8080
kill <process-id>
Database connection issues

Проверьте настройки базы данных

Убедитесь, что PostgreSQL запущен

JWT errors

Проверьте secret key в настройках

Убедитесь в правильности формата токена

Логирование
Логи находятся в logs/application.log и выводятся в консоль.

📞 Поддержка
Документация
Spring Boot Documentation

JWT Documentation

PostgreSQL Documentation

Сообщество
Stack Overflow с тегами java, spring-boot, jwt

GitHub Issues для багрепортов

👥 Разработчики
Backend Developer - Ажеу

Security Specialist - Ажеу

Database Administrator - Ажеу

Вклад в проект
Мы приветствуем contributions! Пожалуйста, читайте CONTRIBUTING.md для деталей.

📄 Лицензия
Этот проект лицензирован под MIT License - смотрите LICENSE.md файл для деталей.

Примечание: Это production-ready система, но перед развертыванием в продакшене обязательно:

Настройте SSL/TLS сертификаты

Измените дефолтные пароли и secret keys

Настройте мониторинг и alerting

Протестируйте под нагрузкой

Настройте backup стратегию для базы данных

Для вопросов и поддержки обращайтесь через GitHub Issues или email: ageu@mail.ru.
