## WebPA — парсер и анализатор карточек маркетплейсов

Сервис синхронного запуска задач парсинга карточек товаров по поисковым запросам с автоматическим нормированием атрибутов и сохранением результатов в PostgreSQL. Архитектура построена на чистом MVC с отдельными слоями доменных моделей, сервисов и контроллеров, плюс плагинный слой парсеров, чтобы легко добавлять новые площадки.

### Технологии
- Java 21, Spring Boot 3 (Web, Data JPA, Validation, Actuator, Springdoc)
- PostgreSQL + Flyway для миграций схемы
- Jsoup для разбора HTML
- Lombok для снижения бойлерплейта

### Доменные модели
- `ParseTask` — задача парсинга (запрос, список маркетплейсов, глубина пагинации, статус, комментарий, стратегия «добавлять/перезаписывать»).
- `ProductCard` — сохранённая карточка (рынок, URL, имя, цена, фото, рейтинг, продавец, доступность, нормализованные и сырые атрибуты, метаданные запроса и времени).
- `Marketplace` — перечисление площадок (Wildberries, Ozon, Yandex Market, Custom).
- `TaskStatus` — состояние задачи (QUEUED/RUNNING/COMPLETED/FAILED).

### Парсеры и масштабируемость
Все парсеры реализуют интерфейс `MarketplaceParser` и регистрируются автоматически через Spring. Добавление новой площадки:
1) Создайте класс в `com.webpa.service.parser`, реализующий `MarketplaceParser`.
2) Опишите построение URL поиска, выборку карточек и маппинг в `ParsedProduct`.
3) Поместите бины в Spring (аннотация `@Component`) — оркестратор сам подхватит новый парсер.

Встроенные примеры: `WildberriesParser`, `OzonParser`, `YandexMarketParser`. Логику извлечения атрибутов можно расширять через `AttributeNormalizer`, где заданы синонимы («артикул» → `sku`, «страна производства» → `country` и т.п.).

### API (REST)
- `POST /api/tasks?autorun=true` — создать задачу (и по умолчанию сразу выполнить). Тело: `query`, `marketplaces`, `pages`, `pageSize`, `appendToExisting`, `comment`.
- `POST /api/tasks/{id}/run` — выполнить ранее созданную задачу.
- `GET /api/tasks` — список задач.
- `GET /api/tasks/{id}` — детальная информация о задаче.
- `GET /api/products` — поиск сохранённых карточек с фильтром по маркетплейсам и запросу, поддерживает пагинацию.
- `GET /api/products/task/{taskId}` — карточки в рамках конкретной задачи.

Пример запроса создания задачи:
```bash
curl -X POST http://localhost:8080/api/tasks?autorun=true \
  -H "Content-Type: application/json" \
  -d '{
    "query": "витамины",
    "marketplaces": ["WILDBERRIES", "OZON"],
    "pages": 2,
    "pageSize": 40,
    "appendToExisting": false,
    "comment": "тестовый запуск"
  }'
```

### Конфигурация
Основные параметры находятся в `src/main/resources/application.properties` и читают переменные окружения:
- `DB_URL` (по умолчанию `jdbc:postgresql://localhost:5432/webpa`)
- `DB_USER` / `DB_PASSWORD`

### Локальный запуск
1) Поднимите PostgreSQL (пример):
   ```bash
   docker run --name webpa-postgres -e POSTGRES_PASSWORD=webpa -e POSTGRES_USER=webpa -e POSTGRES_DB=webpa -p 5432:5432 -d postgres:15
   ```
2) Примените миграции через Flyway автоматически при старте приложения.
3) Запустите сервис:
   ```bash
   ./mvnw spring-boot:run
   ```
4) Swagger UI доступен по `/swagger-ui.html`.

### Архитектура
- Контроллеры (`web.controller`) принимают REST-запросы.
- Сервисы (`service`) инкапсулируют бизнес-логику парсинга, нормализацию атрибутов и работу с задачами.
- Парсеры (`service.parser`) отвечают за конкретные площадки.
- Репозитории (`repository`) скрывают доступ к базе PostgreSQL через JPA.
- Миграции в `src/main/resources/db/migration`.

### Расширение
- Добавляйте новые синонимы в `AttributeNormalizer` для нормализации параметров.
- Добавляйте экспорт/аналитику поверх `ProductCard` — модель хранит нормализованные и сырые значения для удобства последующей обработки.
