# RescuEat Backend

Spring Boot backend for RescuEat (food deals API). Uses in-memory data by default; no database required to run.

## Requirements

- **Java 17** or later
- **Gradle** (optional if you use the wrapper)

## Run the application

From the project root:

```bash
./gradlew bootRun
```

(On Windows: `gradlew.bat bootRun`.)

The wrapper is included (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, `gradle-wrapper.properties`), so you don’t need Gradle installed.

Alternatively, if you have Gradle installed: `gradle bootRun`.

The API will be available at **http://localhost:8080**.

- **All deals:** `GET http://localhost:8080/api/deals`
- **One deal by id:** `GET http://localhost:8080/api/deals/1`

## Build

```bash
./gradlew build
```

## Regenerating the Gradle wrapper

If `gradle/wrapper/gradle-wrapper.jar` is missing or you want to upgrade the wrapper (e.g. newer Gradle), run from the project root:

```bash
gradle wrapper
```

You need Gradle installed (e.g. via SDKMAN, Homebrew, or your OS package manager). This recreates `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, and updates `gradle/wrapper/gradle-wrapper.properties`. Then use `./gradlew` as usual.

## Project structure

- `src/main/java/com/rescureat/` — application and packages (config, controller, model, repository, service)
- `src/main/resources/application.properties` — configuration (app name, server port, DB placeholders, JPA settings)
