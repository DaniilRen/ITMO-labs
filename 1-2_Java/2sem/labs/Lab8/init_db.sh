#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

export DB_URL="${DB_URL:-jdbc:postgresql://localhost:5432/postgres}"
export DB_USER="${DB_USER:-postgres}"
export DB_PASSWORD="${DB_PASSWORD:-12345}"

DB_HOST="$(echo "$DB_URL" | sed -E 's#jdbc:postgresql://([^:/]+).*#\1#')"
DB_PORT="$(echo "$DB_URL" | sed -E 's#jdbc:postgresql://[^:/]+:([0-9]+)/.*#\1#')"
DB_NAME="$(echo "$DB_URL" | sed -E 's#jdbc:postgresql://[^/]+/([^?]+).*#\1#')"
DB_PORT="${DB_PORT:-5432}"

PSQL=(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -v ON_ERROR_STOP=1)
export PGPASSWORD="$DB_PASSWORD"

echo "Applying database schema migration..."
"${PSQL[@]}" -f "$ROOT/server/src/main/resources/sql/schema.sql"

echo "Database schema is up to date."
