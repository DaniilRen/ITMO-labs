#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

JAR="$ROOT/client/target/client-1.0-SNAPSHOT.jar"
LIB="$ROOT/client/target/lib"

if [[ ! -f "$JAR" ]]; then
  echo "Client jar not found. Run ./build.sh first." >&2
  exit 1
fi

if [[ ! -d "$LIB" ]]; then
  echo "JavaFX libraries not found in $LIB. Run ./build.sh first." >&2
  exit 1
fi

case "$(uname -s)" in
  MINGW*|MSYS*|CYGWIN*|Windows*)
    MODULE_PATH_SEP=';'
    to_java_path() {
      cygpath -w "$1" 2>/dev/null || echo "$1"
    }
    ;;
  *)
    MODULE_PATH_SEP=':'
    to_java_path() {
      echo "$1"
    }
    ;;
esac

MODULE_PATH=""
for fx_jar in "$LIB"/javafx-*.jar; do
  [[ -f "$fx_jar" ]] || continue
  java_jar="$(to_java_path "$fx_jar")"
  if [[ -z "$MODULE_PATH" ]]; then
    MODULE_PATH="$java_jar"
  else
    MODULE_PATH="${MODULE_PATH}${MODULE_PATH_SEP}${java_jar}"
  fi
done

if [[ -z "$MODULE_PATH" ]]; then
  echo "No JavaFX jars found in $LIB. Run ./build.sh first." >&2
  exit 1
fi

JAVA_JAR="$(to_java_path "$JAR")"

exec java \
  --enable-native-access=javafx.graphics \
  --module-path "$MODULE_PATH" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics \
  -jar "$JAVA_JAR"
