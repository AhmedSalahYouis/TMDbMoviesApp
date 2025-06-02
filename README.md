# 🎬 TMDb Movies App

An Android demo app that uses TMDb API to display popular movies, view movie details, and manage favorites with offline support.

## 📱 Features
- List of popular movies with pagination
- View full movie details (overview, runtime, genres)
- Mark/unmark movies as favorites
- View favorite movies offline
- Offline cache support for last loaded data
- Toggle list/grid layout

## 🧱 Tech Stack
- **Kotlin** + **Coroutines** + **Flow**
- **MVI Architecture**
- **Paging 3**
- **Room Database**
- **Retrofit2 + Gson**
- **Navigation Components**
- **Hilt (Dependency Injection)**

## 🧪 Testing
- Unit tests for:
  - ViewModels
  - Repository
- Test libraries used:
  - MockK
  - Turbine
  - JUnit4

## 🛠 Setup
1. Clone the repo
2. Replace `sdk.dir` in `local.properties` with your Android SDK path
3. Add your TMDb API Key in `gradle.kts (:data)`
4. Sync Gradle and Run

## 📸 Screenshots
_coming soon_

---
Built for learning and demonstration purposes.