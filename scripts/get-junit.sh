#!/usr/bin/env bash
# Télécharge le JAR JUnit Platform Console Standalone dans lib/.
set -euo pipefail
cd "$(dirname "$0")/.."
VERSION="${JUNIT_VERSION:-1.10.2}"
JAR="lib/junit-platform-console-standalone.jar"
if [ -f "$JAR" ]; then echo "JUnit déjà présent : $JAR"; exit 0; fi
mkdir -p lib
curl -fsSL -o "$JAR" \
  "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${VERSION}/junit-platform-console-standalone-${VERSION}.jar"
echo "JUnit téléchargé : $JAR"
