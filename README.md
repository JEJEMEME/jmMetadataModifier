# JMMetadataModifier

JMMetadataModifier is an Android library that allows you to easily modify location metadata in MP4 files.

## Features

- Modify location information (latitude/longitude) in MP4 files
- URI-based file access support
- Safe metadata modification using MediaMuxer
- Preserves original files (creates new files)

## Installation

### Step 1. Add the JitPack repository to your build.gradle or settings.gradle

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2. Add the dependency to your app's build.gradle

```gradle
dependencies {
    implementation 'com.github.JEJEMEME:jmMetadataModifier:Tag'
}
```

## Usage

### Basic Usage

```kotlin
// Create a MetadataModifier instance
val modifier = Mp4LocationModifier(context)

// Update location information using URI
val updatedFile = modifier.updateLocation(
    inputUri = uri,          // Input file URI
    latitude = 37.5326f,     // Latitude
    longitude = 127.0246f    // Longitude
)

// Check the modified file
updatedFile?.let { file ->
    // Success: Use the newly created file
} ?: run {
    // Failure: Returns null
}
```

### Using ContentResolver

```kotlin
// Use URI from gallery or file picker
val uri = Uri.parse("content://media/external/video/media/123")
val result = modifier.updateLocation(uri, latitude, longitude)
```

### Direct File Access

```kotlin
// Convert file to URI
val file = File("/path/to/video.mp4")
val uri = Uri.fromFile(file)
val result = modifier.updateLocation(uri, latitude, longitude)
```

## Required Permissions

Add these permissions to your AndroidManifest.xml:

```xml
<!-- File read/write permissions -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

For Android 10 (API level 29) and above, you might also need:
```xml
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```

## Important Notes

1. Modified files are created in your app's external storage directory.
2. Original files are preserved and not modified.
3. Returns null on failure, and temporary files are automatically deleted.
4. Processing large files may take longer.

## Supported Formats

- MP4 (MPEG-4) file format

## Requirements

- Android SDK 24 (Android 7.0) or higher
- Kotlin 1.8.0 or higher

## License

```
MIT License

Copyright (c) 2024 JEJEMEME

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Contributing

Found a bug or want to suggest a new feature? Please create an issue.
Pull requests are welcome!

## Contact

- GitHub: [@JEJEMEME](https://github.com/JEJEMEME) 