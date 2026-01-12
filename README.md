# Penguin Care App

A simple Android app where you take care of a virtual penguin by feeding, playing, and managing its sleep.

## Features

- **Animated Penguin**: The penguin moves and shows different expressions based on its state
- Feed your penguin to maintain hunger levels (triggers happy animation)
- Play with your penguin to keep it happy (triggers bouncing animation)
- Manage energy levels through sleep (triggers sleep animation)
- Visual progress bars for hunger, happiness, and energy
- **Dynamic States**: Penguin shows sad animation when stats are low
- **Auto-decay**: Stats gradually decrease over time to keep gameplay engaging

## Animations

- **Idle**: Gentle swaying and rotation when content
- **Happy**: Scaling and spinning when fed
- **Bounce**: Jumping animation when playing
- **Sleep**: Fading and tilting when resting
- **Sad**: Shaking and dimming when neglected

## Build Instructions

```bash
./gradlew assembleDebug
```

## Installation

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Requirements

- Android SDK 24+
- Gradle 8.5+
- Java 21

## Project Structure

- `app/src/main/java/com/penguin/care/MainActivity.kt` - Main activity
- `app/src/main/res/layout/activity_main.xml` - UI layout
- `app/src/main/res/drawable/penguin.xml` - Penguin drawable
