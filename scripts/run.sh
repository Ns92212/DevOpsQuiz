#!/usr/bin/env bash
# Compile puis lance l'application.
set -euo pipefail
cd "$(dirname "$0")/.."
./scripts/build.sh
exec java -cp build/classes quiz.DevOpsQuiz "$@"
