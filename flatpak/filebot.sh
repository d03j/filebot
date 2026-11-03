#!/bin/sh
PRG="$0"

# resolve relative symlinks
while [ -h "$PRG" ] ; do
	ls=`ls -ld "$PRG"`
	link=`expr "$ls" : '.*-> \(.*\)$'`
	if expr "$link" : '/.*' > /dev/null; then
		PRG="$link"
	else
		PRG="`dirname "$PRG"`/$link"
	fi
done

# get canonical path
WORKING_DIR=`pwd`
PRG_DIR=`dirname "$PRG"`
APP_ROOT=`cd "$PRG_DIR" && pwd`

# restore original working dir
cd "$WORKING_DIR"

if [ -z "$HOME" ]; then
	echo '$HOME must be set'
	exit 1
fi

# Fix blank window rendering on Wayland and non-reparenting window managers
export _JAVA_AWT_WM_NONREPARENTING=1
export AWT_TOOLKIT="sun.awt.X11.XToolkit"
if [ ! -z "$WAYLAND_DISPLAY" ] || [ ! -z "$SSH_CONNECTION" ] || [ ! -z "$SSH_CLIENT" ]; then
	export GDK_BACKEND=x11
fi

# force JVM language and encoding settings
export LANG="en_US.UTF-8"
export LC_ALL="en_US.UTF-8"

# select application data folder
APP_DATA="$HOME/.filebot"
mkdir -p "$APP_DATA/temp"

# locate jar (Flatpak app path /app/share/filebot or local relative path)
if [ -f "/app/share/filebot/filebot.jar" ]; then
	JAR_PATH="/app/share/filebot/filebot.jar"
elif [ -f "$APP_ROOT/filebot.jar" ]; then
	JAR_PATH="$APP_ROOT/filebot.jar"
elif [ -f "$APP_ROOT/FileBot.jar" ]; then
	JAR_PATH="$APP_ROOT/FileBot.jar"
elif [ -f "$APP_ROOT/build/libs/filebot.jar" ]; then
	JAR_PATH="$APP_ROOT/build/libs/filebot.jar"
else
	JAR_PATH="$APP_ROOT/filebot.jar"
fi

# select java binary (use Flatpak OpenJDK runtime if available, else PATH java)
if [ -x "/app/jdk/bin/java" ]; then
	JAVA="/app/jdk/bin/java"
elif [ -x "/usr/lib/sdk/openjdk21/bin/java" ]; then
	JAVA="/usr/lib/sdk/openjdk21/bin/java"
elif command -v java >/dev/null 2>&1; then
	JAVA="java"
else
	echo "Java runtime not found."
	exit 1
fi

exec "$JAVA" \
	-Dunixfs=false \
	-DuseGVFS=false \
	-DuseExtendedFileAttributes=true \
	-DuseCreationDate=false \
	-Dsun.java2d.xrender=false \
	-Dsun.java2d.opengl=false \
	-Dsun.java2d.pmoffscreen=false \
	-Dprism.order=sw \
	-Dfile.encoding="UTF-8" \
	-Dsun.jnu.encoding="UTF-8" \
	-Djava.net.useSystemProxies=false \
	-Djna.nosys=true \
	-Dapplication.deployment=flatpak \
	"-Dapplication.dir=$APP_DATA" \
	"-Djava.io.tmpdir=$APP_DATA/temp" \
	"-Duser.home=$HOME" \
	$JAVA_OPTS \
	-jar "$JAR_PATH" \
	"$@"
