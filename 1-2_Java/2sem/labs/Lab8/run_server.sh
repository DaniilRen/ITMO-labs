#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

export DB_URL="${DB_URL:-jdbc:postgresql://localhost:5432/lab7}"
export DB_USER="${DB_USER:-postgres}"
export DB_PASSWORD="${DB_PASSWORD:-12345}"
export PEPPER="${PEPPER:-2r98h9phqf4chasgc0a}"

JAR="server/target/server-1.0-SNAPSHOT.jar"
if [[ ! -f "$JAR" ]]; then
  echo "Server jar not found. Run ./build.sh first." >&2
  exit 1
fi

exec java -jar "$JAR"
