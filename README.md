# Project Structure

The project follows a clean architecture MVVM, with the following primary packages:

### 1. `di` - Dependency Injection

- **Purpose**: Contains classes and objects to configure Hilt for dependency injection.
- **Main Classes**:
  - `MyApplication.kt`
  - `AppModule.kt`

### 2. `helper`

- **Purpose**: Provides utility functions to manage network state, UI interactions, and single live events that observe view lifecycle.
- **Main Classes**:
  - `Utility.kt`
  - `EndPoints.kt`

### 3. `model`

- **Purpose**: Maps JSON responses from the API into Kotlin data classes.
- **Main Classes**:
  - `ApiResponse.kt`

### 4. `network`

- **Purpose**: Manages API interactions, including HTTP requests and response handling.
- **Main Classes**:
  - `ApiService.kt`
  - `ApiDataSource.kt`
  - `BaseDataSource.kt`

### 5. `view`

- **Purpose**: Contains UI classes such as activities and main views.
- **Main Classes**:
  - `MainActivity.kt`

### 6. `viewmodel`

- **Purpose**: Contains ViewModel and Repository classes to handle app logic and data processing, based on the MVVM architecture.
- **Main Classes**:
  - `MainRepo.kt`
  - `MainViewModel.kt`

# Setup Instructions

## Prerequisites

- Ensure you have android studio, if not [click here](https://developer.android.com/studio?gclid=Cj0KCQiAlsv_BRDtARIsAHMGVSaAQQ9M4x_DtOVZosS5_4xa2i4RYCHuqzjFqQbAwHurV7m0RG1iPGcaApI4EALw_wcB&gclsrc=aw.ds)

## Step 1: Clone the Repository

Clone the `Currency-Converter-` repository:

```bash
git clone git@github.com:hoanghuybu/Currency-Converter-.git
```

## Step 2: Open in Android Studio

Open the project in Android Studio and let it sync the Gradle files.

## Step 3: Set up Dependencies

Ensure all required dependencies are installed by adding them to build.gradle.

## Step 4: Set up Dependencies

Once the setup is complete, connect an Android device or emulator and run the app.

# Note

- This is the first time I learn and apply a clean MVVM architecture to an Android application project, so I consulted many sources but mainly through the article [How I Built a Simple Currency Converter App Using Recommended Android Pattern and Architecture](https://medium.com/swlh/how-i-built-a-simple-currency-converter-app-using-recommended-android-pattern-and-architecture-204a3bbfc142). There are also many difficulties when trying to debug in carsh app cases, as well as not having a deep understanding of this MVVM model.

- Video demo: [Click here](https://drive.google.com/file/d/1R88TfB1G2O_ZTxRQLLWyo_KO_-5EQf44/view?usp=sharing)
