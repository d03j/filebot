# FileBot (Java 21 Fork)

This is a modern fork and refactor of FileBot 4.7.9 (originally created by rednoah), updated to run cleanly on modern Linux distributions.

## Key Changes & Features

- **Java 21 LTS**: Modernized codebase and build pipeline targeting Java 21.
- **Remote Wayland / SSH Support**: Pre-configured window manager flags and environment variables (`_JAVA_AWT_WM_NONREPARENTING=1`, `GDK_BACKEND=x11`, and safe rendering fallbacks) so the GUI renders seamlessly when running remotely over `ssh -X` from Wayland client sessions (e.g., Hyprland, Sway, GNOME Wayland).
- **TheTVDB Removed**: Stripped out TheTVDB integration in favor of TheMovieDB (TMDB), aligning natively with Jellyfin metadata workflows.
- **No Reminders**: Removed upstream donation prompts and review popups.

## Disclaimer & Scope

This fork is offered **as is**, without any warranties or guarantees of any kind. It has only been tested against specific personal workflows—specifically the graphical user interface (GUI) media renaming functionality. Other features, command-line automation scripts, subtitle downloaders, and advanced modules have not been validated. Use at your own risk.

## Releases & Packages

Pre-built binaries (`filebot.jar` and `filebot.sh`) are available under the [GitHub Releases](https://github.com/d03j/filebot/releases) page and GitHub Actions package artifacts (`filebot-package`).

## Building & Running

### Requirements
- Java 21 JDK (e.g. Eclipse Temurin 21)

### Build
Build the standalone executable JAR and launch script using Gradle:

```bash
./gradlew build
```

The resulting files will be located at `build/libs/filebot.jar` and `build/libs/filebot.sh`.

### Run
Launch FileBot using the launcher script:

```bash
./build/libs/filebot.sh
```

Or run directly with Java:

```bash
java -jar build/libs/filebot.jar
```

## Credits & License

- **Original Project**: FileBot by [rednoah](https://www.filebot.net/).
- **License**: Licensed under the terms of the GNU General Public License (GPL) version 2 or later. See the [LICENSE](LICENSE) file for details.
