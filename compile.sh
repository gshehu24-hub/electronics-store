#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAVAFX_LIB="$PROJECT_DIR/javafx/lib"
SRC_DIR="$PROJECT_DIR/src/main/java"
RESOURCES_DIR="$PROJECT_DIR/src/main/resources"
OUT_DIR="$PROJECT_DIR/out"

rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

echo "Compiling Java files..."
find "$SRC_DIR" -name "*.java" > "$PROJECT_DIR/sources.txt"

javac --module-path "$JAVAFX_LIB" \
      --add-modules javafx.controls,javafx.fxml,javafx.graphics \
      -d "$OUT_DIR" \
      @"$PROJECT_DIR/sources.txt"

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    cat "$RESOURCES_DIR/styles.css" > "$OUT_DIR/styles.css"
    rm "$PROJECT_DIR/sources.txt"
else
    echo "Compilation failed!"
    rm "$PROJECT_DIR/sources.txt"
    exit 1
fi
