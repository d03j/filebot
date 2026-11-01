#!/bin/sh
APP_ROOT="/usr/share/filebot"

if [ -z "$HOME" ]; then
	echo '$HOME must be set'
	exit 1
fi

# add APP_ROOT to LD_LIBRARY_PATH
if [ ! -z "$LD_LIBRARY_PATH" ]; then
	export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$APP_ROOT"
else
	export LD_LIBRARY_PATH="$APP_ROOT"
fi

# Fix blank window rendering on Wayland and non-reparenting window managers
export _JAVA_AWT_WM_NONREPARENTING=1
export AWT_TOOLKIT="sun.awt.X11.XToolkit"
if [ ! -z "$WAYLAND_DISPLAY" ] || [ ! -z "$SSH_CONNECTION" ] || [ ! -z "$SSH_CLIENT" ]; then
	export GDK_BACKEND=x11
fi

# select application data folder
APP_DATA="$HOME/.filebot"

java -Dunixfs=false -DuseGVFS=false -DuseExtendedFileAttributes=true -DuseCreationDate=false -Dsun.java2d.xrender=false -Dsun.java2d.opengl=false -Dsun.java2d.pmoffscreen=false -Djava.net.useSystemProxies=false -Djna.nosys=true -Dapplication.deployment=deb "-Dapplication.dir=$APP_DATA" "-Djava.io.tmpdir=$APP_DATA/temp" "-Dnet.filebot.AcoustID.fpcalc=$APP_ROOT/fpcalc" $JAVA_OPTS -jar "$APP_ROOT/FileBot.jar" "$@"
