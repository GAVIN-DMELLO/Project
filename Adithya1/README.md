# 🚗 CarGrasp - AI Vehicle Make, Model & Colour Identifier

**CarGrasp** is a modern, student-friendly Android mobile application built with **Kotlin**, **Jetpack Compose**, **CameraX**, and **Google Gemini Vision AI**. It allows users to take a photo of any vehicle using their camera or select an image from their gallery to instantly identify its **manufacturer (make)**, **model**, and **primary exterior colour** with high-confidence structured AI recognition.

---

## 🌟 Key Features

1. **AI Vehicle Identification**:
   - 🚘 **Make / Manufacturer**: Detects brands such as BMW, Toyota, Tesla, Honda, Mercedes-Benz, Hyundai, Tata, Ford, Audi, etc.
   - 🏎️ **Vehicle Model**: Identifies models (e.g., 3 Series, Corolla, Model Y, Civic, Nexon) or indicates `Unknown/Uncertain` without guessing.
   - 🎨 **Exterior Colour Swatch**: Identifies primary paint colors (e.g., Pearl White, Metallic Blue, Gloss Black, Silver, Crimson Red) and renders a live color swatch circle.
   - 📊 **AI Confidence Score**: Visual confidence meter (High, Medium, Low, Uncertain).
   - 🔍 **AI Vision Insights**: Highlights distinctive styling cues recognized (grille shape, daytime running light profile, badges).
   - 🚫 **Car Not Detected Handling**: Clearly alerts the user if the image does not contain an automobile.

2. **Clean Jetpack Compose UI (Material 3)**:
   - **Home Screen**: Hero automotive branding card, feature pills, "Take Photo", "Choose from Gallery", and API key status indicator.
   - **Image Preview Screen**: High-tech animated laser scanning viewfinder overlay during AI analysis.
   - **Results Screen**: Structured vehicle specifications, color swatch, confidence meter, share button, and "Analyze Another Car" CTA.

3. **Modern Camera & Gallery Integration**:
   - **CameraX**: Real-time viewfinder with live capture, lens switching (back/front), and flash/torch toggle.
   - **Android Photo Picker**: Modern, privacy-first image selection (`PickVisualMedia`).

4. **Security & Clean Architecture**:
   - **No Hard-coded API Keys**: Injected via `BuildConfig` from `local.properties` (git-ignored) or entered securely via the in-app setup modal.
   - **MVVM Pattern**: `CarGraspViewModel`, `StateFlow`, `AnalysisUiState` sealed hierarchy, `GeminiCarRepository`.
   - **Robust Error Handling**: Friendly offline / no-internet banners, API quota handling, and fallback JSON regex parsing.

---

## 🏗️ Project Architecture

```
CarGrasp/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/cargrasp/
│       │   │   ├── MainActivity.kt                      # Main Activity & Navigation
│       │   │   ├── data/
│       │   │   │   ├── model/
│       │   │   │   │   ├── CarAnalysisResult.kt         # Structured AI Output Model
│       │   │   │   │   └── AnalysisUiState.kt           # Compose UI States
│       │   │   │   └── repository/
│       │   │   │       ├── ApiKeyStorage.kt             # Secure Key Manager
│       │   │   │       └── GeminiCarRepository.kt       # Gemini Vision API Client
│       │   │   ├── ui/
│       │   │   │   ├── components/
│       │   │   │   │   ├── CarScannerAnimation.kt       # Laser Scanner Animation
│       │   │   │   │   └── CommonComponents.kt          # Buttons, Cards, Badges
│       │   │   │   ├── screens/
│       │   │   │   │   ├── HomeScreen.kt                # Home Dashboard Screen
│       │   │   │   │   ├── CameraCaptureScreen.kt       # CameraX Viewfinder Screen
│       │   │   │   │   ├── ImagePreviewScreen.kt        # Image Preview & Scan Screen
│       │   │   │   │   ├── ResultsScreen.kt             # Vehicle Results Breakdown
│       │   │   │   │   └── ApiKeyDialog.kt              # API Key Config Modal
│       │   │   │   ├── theme/
│       │   │   │   │   ├── Color.kt                     # Automotive Palette
│       │   │   │   │   ├── Theme.kt                     # Material 3 Theme
│       │   │   │   │   └── Type.kt                      # Typography
│       │   │   │   └── viewmodel/
│       │   │   │       └── CarGraspViewModel.kt         # Main ViewModel
│       │   │   └── utils/
│       │   │       ├── BitmapUtils.kt                   # Downsampling & EXIF
│       │   │       └── NetworkUtils.kt                  # Connectivity Check
│       │   └── res/
│       └── test/java/com/example/cargrasp/
│           └── CarAnalysisResultTest.kt                 # Unit Tests
├── gradle/
│   ├── libs.versions.toml                               # Dependency Catalog
│   └── wrapper/gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── local.properties.example
└── README.md
```

---

## 🔑 Secure Gemini API Key Setup

### Option 1: Via `local.properties` (Recommended)
1. Get a free API key from [Google AI Studio](https://aistudio.google.com/app/apikey).
2. Open or create the `local.properties` file in the root directory of the project.
3. Add your key:
   ```properties
   GEMINI_API_KEY=AIzaSyYourActualApiKeyHere
   ```
4. Build and run the app. `BuildConfig.GEMINI_API_KEY` will automatically inject it securely at compile time.

### Option 2: Via In-App Dialog (For live demo / testing)
1. Open the CarGrasp app.
2. Tap the **"Setup Key"** / Key icon at the top right of the Home Screen.
3. Paste your Gemini API key and tap **"Save Key"**.

---

## 🚀 How to Build & Run in Android Studio

1. Open **Android Studio** (Koala / Ladybug or newer recommended).
2. Select **File > Open...** and navigate to this folder (`Adithya1`).
3. Allow Gradle to sync dependencies automatically.
4. Ensure your Android SDK is installed (Compile SDK: 35, Min SDK: 24).
5. Add your `GEMINI_API_KEY` to `local.properties`.
6. Select an Android device or emulator running Android 7.0+ (API 24+) and click **Run (Shift + F10)**.

---

## 📡 AI Vision JSON Schema

Gemini Vision analyzes the car image and returns structured JSON:

```json
{
  "carDetected": true,
  "make": "BMW",
  "model": "3 Series",
  "colour": "Metallic Blue",
  "confidence": "High",
  "bodyType": "Sedan",
  "estimatedYearRange": "2020-2024",
  "notes": "Identified signature kidney grille, front bumper intake layout, and laser LED headlights."
}
```

---

## 🛠️ Technology Stack
- **Language**: Kotlin 2.0
- **UI Toolkit**: Jetpack Compose with Material 3
- **AI / Vision**: Google Generative AI SDK (`com.google.ai.client.generativeai:generativeai:0.9.0`)
- **Camera**: AndroidX CameraX (`camera-core`, `camera-camera2`, `camera-view`)
- **Gallery**: Android Photo Picker (`ActivityResultContracts.PickVisualMedia`)
- **Image Processing**: Coil Compose & Android BitmapUtils with EXIF rotation
- **Architecture**: MVVM with Android ViewModel, StateFlow & Coroutines
- **JSON Serialization**: Google Gson
