#!/bin/bash

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAVAFX_LIB="$PROJECT_DIR/javafx/lib"
OUT_DIR="$PROJECT_DIR/out"

if [ ! -d "$OUT_DIR" ]; then
    echo "Project not compiled. Running compile.sh first..."
    "$PROJECT_DIR/compile.sh"
fi

java --module-path "$JAVAFX_LIB" \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics \
     -cp "$OUT_DIR" \
     electronicstore.Main
