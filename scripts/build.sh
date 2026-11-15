#!/usr/bin/env bash
# Compile les sources Java avec javac (sans Maven) vers build/classes.
set -euo pipefail
cd "$(dirname "$0")/.."
rm -rf build/classes
mkdir -p build/classes
javac -encoding UTF-8 -d build/classes $(find src/main/java -name '*.java')
echo "Compilation OK -> build/classes"
