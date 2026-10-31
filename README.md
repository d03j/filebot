# FileBot

FileBot is the ultimate tool for organizing and renaming your movies, TV shows, anime, and downloading subtitles.

## Overview

This repository is a modern fork/refactor of FileBot 4.7.9 updated to run on **Java 21+** (Linux).

### Key Features
- **Java 21+ Compatibility**: Modernized source and build configurations targeting Java 21 LTS with Gradle.
- **TV & Movie Metadata**: Automated media renaming with default episode lookup powered by **TheMovieDB** (and AniDB for anime).
- **Subtitles**: Subtitle matching and downloading using OpenSubtitles with complete support for language tags (e.g., `.en.srt`, `.spa.srt`).
- **No Donation Reminders**: Removed donation prompts and review reminders.
- **Clean Builds**: Built with Gradle and automated with GitHub Actions CI.

## Building and Running

### Prerequisites
- JDK 21 or newer

### Build with Gradle
To compile, run tests, and package the standalone executable JAR:

```bash
./gradlew build
```

To build only the fat JAR:
```bash
./gradlew jar
```

The compiled standalone JAR will be located at:
```bash
build/libs/app.jar
```

### Running FileBot

```bash
java -jar build/libs/app.jar
```

Or run via CLI:
```bash
java -jar build/libs/app.jar -rename /path/to/media --db TheMovieDB -non-strict
```

## License

This project is licensed under the terms of the GNU General Public License (GPL) version 2 or later. See the [LICENSE](LICENSE) file for details.
