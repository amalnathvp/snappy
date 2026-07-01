# Snappy

An offline, private Samsung screenshot organizer built with Jetpack Compose. It reads app names from Samsung screenshot filenames and groups them into virtual folders, keeping all data private and on your device without duplicating files.

---

## 💾 Download APK
👉 **[Download snappy-app-debug.apk (Outside Project Folder)](file:///c:/Users/amaln/OneDrive/Desktop/JOB/snappy-app-debug.apk)**

---

## 🚀 Quick Run Guide

### Option 1: Using the Terminal
With your USB debugging phone connected, run:

```powershell
# Install the app on your device
.\gradlew installDebug

# Launch the app
adb shell am start -n com.snappy.samsung/.MainActivity
```

### Option 2: Using Android Studio
1. Open **Android Studio**.
2. Select **Open** and select this directory (`Organizer`).
3. Select your connected phone from the device dropdown and click **Run** (green play icon).
