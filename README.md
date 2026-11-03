# FileBot (Java 21 Fork)

This is a modern fork and refactor of FileBot 4.7.9 (originally created by rednoah), updated to run cleanly on modern Linux distributions.

## Key Changes & Features

- **Java 21 LTS**: Modernized codebase and build pipeline targeting Java 21.
- **Remote Wayland / SSH Support**: Pre-configured window manager flags and environment variables (`_JAVA_AWT_WM_NONREPARENTING=1`, `GDK_BACKEND=x11`, and safe rendering fallbacks) so the GUI renders seamlessly when running remotely over `ssh -X` from Wayland client sessions (e.g., Hyprland, Sway, GNOME Wayland).
- **TheTVDB Removed**: Stripped out TheTVDB integration in favor of TheMovieDB (TMDB), aligning natively with Jellyfin metadata workflows.
- **No Reminders**: Removed upstream donation prompts and review popups.

## Getting the Flatpak

### Download from Releases
Grab the latest pre-built `.flatpak` bundle directly from the [GitHub Releases](https://github.com/d03j/filebot/releases) page and install it locally:

```bash
flatpak install --user io.github.d03j.filebot-j21.flatpak
```

## Credits & License

- **Original Project**: FileBot by [rednoah](https://www.filebot.net/).
- **License**: Licensed under the terms of the GNU General Public License (GPL) version 2 or later. See the [LICENSE](LICENSE) file for details.
