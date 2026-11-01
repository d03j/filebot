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
When running remotely over SSH (`ssh -X` or `ssh -Y`) from a Wayland client (e.g. Sway, Hyprland, GNOME Wayland) into a remote host (even one running Wayland):
- X11 forwarding runs seamlessly via the client's XWayland server.
- FileBot automatically sets `_JAVA_AWT_WM_NONREPARENTING=1`, `GDK_BACKEND=x11`, and safe software 2D pipeline rendering properties (`sun.java2d.xrender=false`, `sun.java2d.opengl=false`, `prism.order=sw`) on startup.
- You can connect and launch the application directly:
```bash
# On your Wayland client:
ssh -X user@remote-host

# On the remote host:
java -jar build/libs/filebot.jar
```
*(If using the provided launcher scripts like `installer/portable/filebot.sh`, these settings are also pre-configured in the wrapper shell environment).*

Or run via CLI:
```bash
java -jar build/libs/filebot.jar -rename /path/to/media --db TheMovieDB -non-strict
```

## License

This project is licensed under the terms of the GNU General Public License (GPL) version 2 or later. See the [LICENSE](LICENSE) file for details.
