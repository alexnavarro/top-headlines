# Top Headlines

An Android application that displays top news headlines from different news providers. The app supports multiple news sources through product flavors (BBC News and CNN) and includes biometric authentication when available on the device.

## Architecture

The project follows **Clean Architecture** with a multi-module structure:

```
:app        UI layer (Compose screens, ViewModels, navigation, DI wiring)
:domain     Business logic (use cases, repository interfaces, domain models)
:data       Data layer (API service, DTOs, mappers, repository implementation)
:core       Shared utilities (dispatchers, result wrapper)
```

### Key patterns

- **MVVM** with `StateFlow` and `collectAsStateWithLifecycle`
- **Sealed interfaces** for UI state (`Loading`, `Success`, `Error`)
- **Hilt** for dependency injection across all modules
- **Type-safe navigation** with `kotlinx.serialization` routes
- **Fakes over mocks** in unit tests for readability and reliability

## Tech stack

| Category | Library |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Navigation | Navigation Compose (type-safe) |
| DI | Hilt |
| Networking | Retrofit, OkHttp, Moshi |
| Image loading | Coil 3 |
| Async | Kotlin Coroutines, StateFlow |
| Auth | AndroidX Biometric |
| Testing | JUnit 4, Turbine, Coroutines Test |

## Prerequisites

- Android Studio Hedgehog or later
- JDK 11+
- A [News API](https://newsapi.org/) key

## Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd top-headlines
   ```

2. Add your News API key to `local.properties` (this file is gitignored):
   ```properties
   NEWS_API_KEY=your_api_key_here
   ```

3. Open the project in Android Studio.

## Build

The project has two product flavors:

| Flavor | News source | Application ID suffix |
|---|---|---|
| `bbc` | BBC News | `.bbc` |
| `cnn` | CNN | `.cnn` |

Build from the command line:

```bash
# BBC flavor
./gradlew :app:assembleBbcDebug

# CNN flavor
./gradlew :app:assembleCnnDebug
```

Or select the desired build variant in Android Studio via **Build > Select Build Variant**.

## Run tests

```bash
# All unit tests
./gradlew :app:testBbcDebugUnitTest :data:testDebugUnitTest :domain:testDebugUnitTest
```

## Project structure

```
app/
  di/                          Hilt modules (ConfigModule, BiometricModule)
  ui/
    auth/                      Biometric authentication (ViewModel, locked screen)
    detail/                    Article detail screen
    headlines/                 Headlines list screen
    navigation/                NavHost and route definitions
    theme/                     Material 3 theme

core/
  dispatcher/                  Coroutine dispatcher qualifiers and module
  result/                      AppResult<T> sealed interface with extensions

data/
  di/                          Network and repository Hilt modules
  mapper/                      DTO to domain model mapping
  model/                       API response DTOs
  remote/                      NewsApiService, data source interface and implementation
  repository/                  NewsRepository implementation with in-memory cache

domain/
  model/                       Article domain model
  repository/                  NewsRepository interface
  usecase/                     GetTopHeadlinesUseCase (sorting by date)
```
