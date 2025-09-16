# GlamAR React Native Demo App

## Overview

This is a React Native application that integrates a WebView to load the GlamAR SDK. The WebView requests camera permissions and interacts with the SDK for skin analysis and other AR-based functionalities.

## Features

- Loads GlamAR SDK inside a WebView
- Requests camera permissions on Android
- Handles WebView messages to track events
- Sends initialization data to the SDK

## Project Structure

```
root
├── App.tsx  # Main entry point of the app
├── android/
│   ├── app/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/glam/MainActivity.java  # Handles WebView permissions
│   │   │   │   ├── AndroidManifest.xml  # Permissions configuration
│   ├── local.properties  # Android SDK path configuration
├── package.json
└── README.md
```

## Setup and Installation

### Prerequisites

- Node.js
- React Native CLI
- Android Studio (for Android development)

### Installation Steps

1. Install dependencies:
   ```sh
   npm install
   ```
2. Configure the Android SDK path:
   - Open `android/local.properties`
   - Replace the username in the following line with your own:
     ```
     sdk.dir = /users/YOUR_USERNAME/library/android/sdk
     ```
3. Ensure required permissions are added in `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.CAMERA"/>
   <uses-feature android:name="android.hardware.camera" android:required="true"/>
   ```
4. Start the Metro bundler:
   ```sh
   npx react-native start
   ```
5. Build and run the app on Android:
   ```sh
   npx react-native run-android
   ```

## WebView Integration

The WebView loads the GlamAR SDK and communicates via `window.postMessage`. The SDK events are handled in `onMessage`.

### Camera Permission Handling

- On Android, camera permission is requested using `PermissionsAndroid`.
- In `MainActivity.java`, `onPermissionRequest` grants camera access for WebView.

### Initialization Payload

Initialize the SDK:

```js
GlamAr.init({
  apiKey: 'Your_API_Key',
  platform: 'react_native', //required when using react native
});
```

### Events Handling

To Listen the SDK Events.

```js
const photoLoadedSubscription = GlamAr.on('photo-loaded', (data: any) => {
  console.log('Photo loaded:', data);
});

const loadedSubscription = GlamAr.on('loaded', (data: any) => {
  console.log('glamar loaded', data);
});

// 3) Cleanup on unmount
return () => {
  // NativeEventEmitter returns an EmitterSubscription with remove()
  photoLoadedSubscription?.remove?.();
  loadedSubscription?.remove?.();
};
```

---

## 📡 API Reference

| Method                             | Description                                      |
| ---------------------------------- | ------------------------------------------------ |
| `GlamAr.init(config)`              | Initializes the SDK                              |
| `GlamAr.applySku(skuId)`           | Applies a specific SKU                           |
| `GlamAr.applyByCategory(category)` | Applies the first SKU from a category            |
| `GlamAr.snapshot()`                | Captures a snapshot (fires `photo-loaded` event) |
| `GlamAr.reset()`                   | Clears current applied items                     |
| `GlamAr.open()` / `close()`        | Opens or closes the live preview mode            |
| `GlamAr.on(event, cb)`             | Registers event listeners                        |

---

## 🔔 Supported Events

| Event                  | Description                  |
| ---------------------- | ---------------------------- |
| `loaded`               | SDK initialized              |
| `opened`, `closed`     | Widget opened or closed      |
| `photo-loaded`         | Snapshot captured            |
| `camera-opened`        | Camera successfully accessed |
| `camera-closed`        | Camera stopped               |
| `camera-failed`        | Error accessing camera       |
| `subscription-invalid` | API key expired or invalid   |
| `skin-analysis`        | Skin analysis data received  |
| `error`                | Any error from SDK           |

---

Detailed documentation available at https://www.glamar.io/docs/

## Troubleshooting

### WebView Not Loading

- Ensure the device has an active internet connection.

### Camera Permission Issues

- Verify that camera permission is granted in Android settings.
- Ensure `onPermissionRequest` in `MainActivity.java` correctly grants camera permissions.

### WebView Communication Not Working

- Use `console.log(event.nativeEvent.data)` inside `handleMessage` to debug messages received from the WebView.

## Contributors

- **Your Name** (Project Maintainer)

## License

This project is licensed under [Your License].
