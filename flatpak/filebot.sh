#!/bin/sh

# Set application data folder ($HOME/.filebot)
export APP_DATA="${APP_DATA:-$HOME/.filebot}"

# Ensure application data directory exists
mkdir -p "$APP_DATA"

# Configure Wayland and remote SSH X11 rendering defaults
export _JAVA_AWT_WM_NONREPARENTING=1
export AWT_TOOLKIT="sun.awt.X11.XToolkit"

if [ -n "$WAYLAND_DISPLAY" ] || [ -n "$SSH_CONNECTION" ] || [ -n "$SSH_CLIENT" ]; then
    export GDK_BACKEND=x11
fi

export LANG="en_US.UTF-8"
export LC_ALL="en_US.UTF-8"

# Locate runtime jar
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
if [ -f "/app/lib/filebot.jar" ]; then
    JAR_PATH="/app/lib/filebot.jar"
elif [ -f "$HOME/filebot/filebot.jar" ]; then
    JAR_PATH="$HOME/filebot/filebot.jar"
elif [ -f "$SCRIPT_DIR/filebot.jar" ]; then
    JAR_PATH="$SCRIPT_DIR/filebot.jar"
elif [ -f "$SCRIPT_DIR/../build/libs/filebot.jar" ]; then
    JAR_PATH="$SCRIPT_DIR/../build/libs/filebot.jar"
elif [ -f "build/libs/filebot.jar" ]; then
    JAR_PATH="build/libs/filebot.jar"
else
    JAR_PATH="filebot.jar"
fi

exec java \
    -Dunixfs=false \
    -DuseGVFS=false \
    -DuseExtendedFileAttributes=true \
    -DuseCreationDate=false \
    -Dsun.java2d.xrender=false \
    -Dsun.java2d.opengl=false \
    -Dsun.java2d.pmoffscreen=false \
    -Dprism.order=sw \
    -Dawt.useSystemAAFontSettings=on \
    -Dswing.aatext=true \
    -Dapplication.deployment=flatpak \
    "-Dapplication.dir=$APP_DATA" \
    "-Djava.io.tmpdir=$APP_DATA/temp" \
    "-Duser.home=$HOME" \
    -jar "$JAR_PATH" "$@"
