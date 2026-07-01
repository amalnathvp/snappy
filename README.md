# Snappy

**Snappy** is a native Android application built with Kotlin and Jetpack Compose. It serves as a fully offline, private screenshot organizer designed specifically for Samsung devices. 

Rather than moving, renaming, or copying your original screenshot files, Snappy reads the Samsung screenshot filename patterns, parses the source application name, and lists them dynamically inside virtual folders. It stores only metadata locally using a Room database and relies entirely on Android's `MediaStore` system to display the original images.

---

## 🌟 Key Features

*   **Smart App-Name Parser:** Extracts the source app name from Samsung files (e.g. `Instagram` from `Screenshot_20260701_120530_Instagram.jpg`). If filenames don't match the strict date/time prefix but contain a keyword (like *Snapchat* or *Twitter*), Snappy will map it correctly. Anything unrecognized is placed in **Others**.
*   **Custom Virtual Collections:** Create custom folders (e.g., *Receipts*, *Recipes*, *Ideas*) and add/remove screenshots to/from them dynamically using the viewer controls.
*   **OLED Pitch-Black Dark Theme:** Modern Material 3 interface optimized with a true black theme (`#000000`) for power savings on OLED/AMOLED screens.
*   **Calendar Date Filtering:** Toggle a Date Picker from the toolbar to instantly filter screenshots by the day they were captured.
*   **Instant Search & Sort:** Search screenshots by filename or app name instantly, and sort collections by *Newest First* or *Oldest First*.
*   **Fluid Image Viewer:** Horizontal pager supporting:
    *   Isolated double-tap and pinch-to-zoom gestures.
    *   Swipe navigation between screenshots (enabled when zoom is at `1x`).
    *   Direct image sharing (via standard system share sheet).
    *   Launching external native gallery viewers.
    *   OLED favoriting indicators and secure system-mediated deletion via `MediaStore`.
*   **Database Stats:** Review total screenshot counts, unique category folder counts, and storage space occupied on disk.
*   **100% Private & Offline:** Zero internet connections, no backend trackers, ads, analytics, AI scanning, or OCR engines. All computation remains local on the device.

---

## 🛠️ Technology Stack

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Database:** Room (SQLite) with KSP annotation compilation
*   **Image Loading:** Coil-Compose
*   **Navigation:** Compose Navigation
*   **Threading/Concurrency:** Kotlin Coroutines & Flows
*   **Build Tool:** Gradle (Kotlin DSL) with Java 21

---

## 📂 Project Structure

```text
app/src/main/java/com/snappy/samsung/
├── SnappyApplication.kt         # Application class; manages DB/Repository singletons
├── MainActivity.kt              # Entry point; requests permissions and configures Navigation
├── model/
│   └── Screenshot.kt            # Core Domain metadata model
├── database/
│   ├── ScreenshotEntity.kt      # Room screenshot table mapping
│   ├── CustomCollectionEntity.kt # Room table storing user custom folder names
│   ├── ScreenshotCustomCollectionCrossRef.kt # Junction table for many-to-many custom maps
│   ├── ScreenshotDao.kt         # DB operations, queries, and SQL-side aggregations
│   └── ScreenshotDatabase.kt    # Room database entry point
├── repository/
│   └── ScreenshotRepository.kt  # Coordinates MediaStore scans and Room sync execution
├── utils/
│   ├── FilenameParser.kt        # Parses app keywords and Samsung display formats
│   └── MediaStoreScanner.kt     # Scans DCIM and Pictures system storage folders
├── viewmodel/
│   ├── HomeViewModel.kt         # Exposes app folders and custom collections states
│   ├── CollectionViewModel.kt   # Filters collection grids (search, date, favorites, sorting)
│   ├── ViewerViewModel.kt       # Exposes pager items lists and coordinates database edits
│   └── SettingsViewModel.kt     # Manages stats, manual rescans, and database wipes
└── ui/
    ├── theme/
    │   ├── Color.kt             # Slate & Indigo premium palettes, OLED black colors
    │   ├── Type.kt              # Typography rules
    │   └── Theme.kt             # Material Theme wrapper (pitch black in Dark Mode)
    ├── home/
    │   └── HomeScreen.kt        # Renders collection cards and custom folder creation dialogs
    ├── collection/
    │   └── CollectionScreen.kt  # Responsive image grid, layout selectors, and Date Pickers
    ├── viewer/
    │   └── FullScreenViewerScreen.kt # Paged gesture viewer, sharing, and folder manager dialog
    └── settings/
        └── SettingsScreen.kt    # Stats graphs panel, rescan buttons, and manual dark switch
```

---

## 🚀 How to Build and Run the App

You can compile, install, and run this project using either terminal commands or Android Studio.

### Prerequisites
*   Ensure a phone or emulator is connected with **USB Debugging** enabled in Developer Options.
*   Run `adb devices` in your command prompt/terminal to check that the status is listed as `device` (authorized).

### Option 1: Using the Terminal (Gradle Wrapper)

Navigate to the project root directory in your command line:
*   **Compile the Debug APK:**
    *   *Windows (PowerShell):* `.\gradlew assembleDebug`
    *   *Mac/Linux:* `./gradlew assembleDebug`
    The compiled installer will be saved at `app/build/outputs/apk/debug/app-debug.apk`. (A copy is also saved directly to the root workspace folder as `snappy-app-debug.apk`).
*   **Install the APK on your device:**
    *   *Windows:* `.\gradlew installDebug`
    *   *Mac/Linux:* `./gradlew installDebug`
*   **Launch the application:**
    ```bash
    adb shell am start -n com.snappy.samsung/.MainActivity
    ```

### Option 2: Using Android Studio

1.  Open **Android Studio**.
2.  Click **Open** and select this directory: `c:\Users\amaln\OneDrive\Desktop\JOB\Organizer`.
3.  Let the build process sync dependencies and configure.
4.  Select your connected phone from the device dropdown menu in the top toolbar.
5.  Click the green **Run** button (play icon), or press `Shift + F10` (Windows) / `Control + R` (macOS). The app will automatically build, install, and open on your device screen.

---

## 🔒 Permissions

Snappy targets Android 11 (API 30) up to Android 14 (API 34). It handles storage permissions dynamically based on the OS version:
*   **Android 13+ (API 33+):** Uses `READ_MEDIA_IMAGES` to scan photos.
*   **Android 11 & 12 (API 30-32):** Uses `READ_EXTERNAL_STORAGE` and `WRITE_EXTERNAL_STORAGE`.
*   **Image Deletion:** Uses standard `MediaStore.createDeleteRequest(contentResolver, ...)` on API 30+ to prompt a secure system dialog before deleting physical files.
