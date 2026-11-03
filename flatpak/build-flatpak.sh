#!/bin/sh
set -e

REPO_DIR="build/flatpak-repo"
BUILD_DIR="build/flatpak-build"
BUNDLE_DIR="packages"
APP_ID="net.filebot.FileBot"

mkdir -p "$BUNDLE_DIR"

if command -v flatpak-builder >/dev/null 2>&1; then
    echo "Building Flatpak package..."
    flatpak-builder --force-clean --repo="$REPO_DIR" "$BUILD_DIR" flatpak/net.filebot.FileBot.json
    flatpak build-bundle "$REPO_DIR" "$BUNDLE_DIR/$APP_ID.flatpak" "$APP_ID"
    echo "Flatpak bundle created at $BUNDLE_DIR/$APP_ID.flatpak"
else
    echo "flatpak-builder not installed; packaging flatpak staging assets to $BUNDLE_DIR/flatpak"
    mkdir -p "$BUNDLE_DIR/flatpak"
    cp -r flatpak/* "$BUNDLE_DIR/flatpak/"
fi
