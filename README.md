<img src="https://github.com/user-attachments/assets/4d25359e-85b0-46ab-8dd1-95655b666baa" alt="app_icon" width="50" align="left"> <h1> Elecriciview</h2>

## Overview
Elecriciview is an Android application designed to view and analyze electricity consumption data stored in JSON format. The app allows users to browse through JSON files in a selected folder, view detailed electricity consumption information, and navigate between file listings and detailed views.

## Features
- **Folder Selection**: Choose a folder containing electricity data JSON files
- **File Browsing**: View a sorted list of JSON files (newest first)
- **Persistent Storage**: Remembers the last selected folder
- **Detailed View**: Examine the contents of individual electricity consumption files
- **User-Friendly Interface**: Material Design 3 components with intuitive navigation

## Technical Details

### Architecture
- **UI Framework**: Jetpack Compose
- **File Access**: DocumentFile API for storage access framework compatibility
- **Data Parsing**: JSON parsing for electricity consumption data
- **State Management**: Compose state management for UI updates

### Key Components
- `MainActivity`: Main entry point containing the app's Compose UI
- `ElectricityDetailsScreen`: Composable for displaying detailed electricity data
- Storage permission handling via ActivityResultContracts

## Getting Started

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or newer
- Minimum SDK: 21 (Android 5.0 Lollipop)
- Target SDK: 33 (Android 13)

### Building the Project
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build the project using:
```bash
./gradlew assembleDebug
```

### Installation
Install the app on your device using:
```bash
./gradlew installDebug
```

## Usage
1. Launch the app
2. Tap "Select Folder" to choose a directory containing electricity JSON files
3. Browse through the list of available JSON files
4. Tap on any file to view its detailed electricity consumption data
5. Use the back button to return to the file list

## File Format
The app expects JSON files with electricity consumption data. The JSON structure should include details about electricity usage, which will be displayed in the details screen.

## Permissions
- `READ_EXTERNAL_STORAGE`: Required to access JSON files

## License
This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Contributing
Contributions are welcome. Please feel free to submit a Pull Request.

## Contact
Author: [NNKtv28](https://github.com/NNKtv28)
For any inquiries, please open an issue in the repository.
![image](https://github.com/user-attachments/assets/46022d72-3f36-4436-aac0-425cbf759f32)
![image](https://github.com/user-attachments/assets/c72055d1-0d64-43bd-a038-08bf02a7c70d)
![image](https://github.com/user-attachments/assets/c331a7fa-8344-492d-97e6-3f4283954122)
![image](https://github.com/user-attachments/assets/cf4a3a8b-6ff6-4c92-9682-256e5a21f3ed)





