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
build/libs/filebot.jar
```

*(In GitHub Actions, you can download `filebot.jar` directly from the workflow run's "Artifacts" section).*

### Running FileBot

```bash
java -jar build/libs/filebot.jar
```

#### Running over SSH (X11 / Wayland Forwarding)
When running remotely over SSH (`ssh -X` or `ssh -Y`) into or from a Wayland desktop (such as GNOME Wayland, Sway, or Hyprland), non-reparenting window managers require `_JAVA_AWT_WM_NONREPARENTING=1` to be set in your shell environment prior to launching Java, along with disabling XRender acceleration to prevent blank windows:
```bash
export _JAVA_AWT_WM_NONREPARENTING=1
java -Dsun.java2d.xrender=false -Dsun.java2d.opengl=false -jar build/libs/filebot.jar
```
*(If using the provided launcher scripts like `installer/portable/filebot.sh`, this is configured automatically).*

Or run via CLI:
```bash
java -jar build/libs/filebot.jar -rename /path/to/media --db TheMovieDB -non-strict
```

## License

This project is licensed under the terms of the GNU General Public License (GPL) version 2 or later. See the [LICENSE](LICENSE) file for details.
