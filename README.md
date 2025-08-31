# Calculator

A simple calculator application built using Kotlin Multiplatform and Compose Multiplatform.

This project demonstrates basic arithmetic operations and a user interface built with Jetpack Compose, shared across Android and potentially other platforms.

## Project Structure

This is a Kotlin Multiplatform project targeting Android and iOS.

*   `./composeApp/src` is for code that will be shared across your Compose Multiplatform applications.
    *   `commonMain` is for code that’s common for all targets (like the calculator logic and UI in `App.kt`).
    *   Other folders like `androidMain`, `iosMain` are for platform-specific Kotlin code.
*   `./iosApp` would contain the iOS application entry point (if fully configured).

## How to Run (Android)

1.  Open the project in Android Studio.
2.  Ensure you have an Android emulator set up or a physical device connected.
3.  Select the `composeApp` run configuration from the dropdown menu in the toolbar.
4.  Click the "Run" button (green play icon).

## Features

*   Basic arithmetic operations: Addition, Subtraction, Multiplication, Division, Percentage.
*   Clear (C) and Delete (DEL) functionality.
*   Responsive UI that adapts to input.

*   Android studio view
*   <img width="1919" height="1008" alt="Screenshot 2025-08-31 122552" src="https://github.com/user-attachments/assets/1822fdff-8d83-4588-a921-f56dc832fd49" />

