# 🌤️ Discover the Weather

**Discover the Weather** is an advanced, premium-tier Android application built with **Jetpack Compose** and **Modern Android Development (MAD)** practices. Designed with a focus on high-end aesthetics (Dribbble-worthy UI) and sophisticated feature integration, this project serves as a comprehensive showcase of modern mobile engineering.

![Premium UI Showcase](img.png)

## 🚀 Key Features

### 💎 Premium Experience
- **Glassmorphism Design**: Extensive use of frosted glass effects, translucent borders, and high-fidelity typography.
- **Dynamic Cinematic Gradients**: Backgrounds that dynamically shift based on real-time weather conditions and time of day.
- **Animated Onboarding**: A custom cinematic splash screen with overshoot legacy animations for a premium first impression.

### 📊 Advanced Weather Intelligence
- **3-Day Detailed Forecast**: High-resolution forecast cards providing deep insights into daily weather conditions.
- **Live Air Quality Index (AQI)**: 
    - Real-time indexing (US-EPA standard).
    - **Pollutant Breakdown**: Detailed metrics for CO, NO₂, O₃, SO₂, PM2.5, and PM10.
    - **Status-Based Styling**: UI elements that adapt color based on air safety levels.
- **Atmospheric Analytics**: A dense metrics grid for UV Index, Visibility, Pressure, Humidity, and astronomical data (Sunrise/Sunset).

### 💾 Robust Persistence & Search
- **Persistent Favorites**: Full **Room DB** integration allowing users to toggle and save favorite cities with a single tap.
- **Global Search**: Search for any city worldwide with instant API synchronization.

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Dependency Injection**: [Koin](https://insert-koin.io/) (Modules-based)
- **Networking**: [Ktor](https://ktor.io/) (Android Engine)
- **Serialization**: [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Local Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Data Provider**: [WeatherAPI.com](https://www.weatherapi.com/)

## 🏗️ Project Structure

```text
app/src/main/java/com/discoverthe/weatherforecastapp/
├── data/           # Room Database, DAO, and local data types
├── di/             # Koin Dependency Injection modules
├── model/          # API & Local Data Models (Kotlin Serialization)
├── navigation/     # Jetpack Compose Navigation Graph
├── repositary/     # Unified Repository for Network & DB
├── screens/        # UI Layers
│   ├── main/       # Hero screen with AQI & Forecast
│   ├── favorites/  # Room-backed collections
│   ├── search/     # City discovery
│   ├── settings/   # Unit preferences (C/F)
│   └── splash/     # Cinematic animations
├── ui/             # Design System (Theme, Type, Color)
└── utils/          # Formatting & Constants
```

## ⚙️ Getting Started

### Prerequisites
- Android Studio Iguana+
- Android SDK 24+
- JDK 17

### Installation
1. **Clone the Repo**
   ```bash
   git clone https://github.com/vardhanyadav1714/WeatherForecastApp
   ```
2. **API Key Setup**
   The app uses **WeatherAPI.com**. Get your free key and add it to `local.properties`:
   ```properties
   API_KEY=your_api_key_here
   ```
3. **Run**
   Sync Gradle and run on a physical device or emulator for the best experience with dynamic gradients.

## 📄 License
Licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
Created with ❤️ for a specialized developer portfolio.
