# 🌿 Sanctuary

[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com/)
[![Build](https://img.shields.io/badge/Build-Gradle_Kotlin_DSL-02303A?style=for-the-badge&logo=gradle)](https://gradle.org/)

> A highly aesthetic journaling application for Android, designed to provide a peaceful, visual, and deeply personal space for your daily reflections.

Sanctuary elevates standard digital note-taking into a mindful journaling experience. Built using a template from the Google AI Studio ecosystem, it seamlessly integrates conversational AI capabilities to offer contextual prompts, creative inspiration, and thoughtful reflection helpers.

---

## ✨ Features

* **Aesthetic Minimalist UI:** Crafted with a clean, high-fidelity layout to minimize distractions and promote reflective writing.
* **AI-Assisted Journaling:** Powered by the Gemini SDK via Google AI Studio to help you break through writer's block with context-aware prompts.
* **Privacy Focused:** Your thoughts belong to you. Designed to manage localized configurations securely on your Android device.

---

## 🛠️ Tech Stack & Requirements

* **Language:** [Kotlin](https://kotlinlang.org/) (100%)
* **Build System:** Gradle with Kotlin DSL (`.gradle.kts`)
* **IDE:** [Android Studio](https://developer.android.com/studio)
* **AI Engine:** Google Gemini SDK

---

## 🚀 Getting Started

### 1. Clone the Project
```bash
git clone [https://github.com/piyush-1803/Sanctuary.git](https://github.com/piyush-1803/Sanctuary.git)
cd Sanctuary

```

### 2. Import into Android Studio

1. Launch **Android Studio**.
2. Click **Open** and select the root directory of the cloned project.
3. Wait for the IDE to finish indexing and resolving project-wide dependencies.

### 3. Add Your Environment Variables

Duplicate the example environment template to set up your keys:

```bash
cp .env.example .env

```

Open the `.env` file in your root folder and replace the placeholder with your actual Gemini API key:

```env
GEMINI_API_KEY=your_actual_gemini_api_key_here

```

### 4. Local Run Configuration (Workaround)

To build and deploy the app directly onto a local emulator or physical test device without signature conflicts, open the app-level `build.gradle.kts` file (`app/build.gradle.kts`) and remove or comment out the following line:

```kotlin
signingConfig = signingConfigs.getByName("debugConfig")

```

### 5. Build and Run

Select your target virtual or physical device in Android Studio and press **Run (Shift + F10)**.

---

## 📂 Project Structure

```text
├── .build-outputs/      # Cached artifacts and local build outputs
├── app/                 # Main Android module containing source code (Kotlin) and assets
├── gradle/              # Gradle wrapper configuration files
├── .env.example         # Template for required environment keys
├── build.gradle.kts     # Project-level build script
├── settings.gradle.kts  # Project-wide repository and module declarations
└── metadata.json        # Google AI Studio configurations

```

---

## 🤖 AI Studio Integration

This repository is linked to a Google AI Studio workspace for seamless prompt testing and model management:

* **View Workspace Dashboard:** [Sanctuary on AI Studio](https://ai.studio/apps/fbd9ddb8-92f1-4e08-bb66-97e640d68a36)

---

## 📄 License

Maintained with care by [piyush-1803](https://github.com/piyush-1803). Check the repository details for localized contribution and licensing agreements.

```

```
