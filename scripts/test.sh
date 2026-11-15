#!/usr/bin/env bash
# Compile les tests avec javac (JAR JUnit dans le classpath) puis les exécute
# avec JUnit Platform Console. Code de retour != 0 si un test échoue.
set -euo pipefail
cd "$(dirname "$0")/.."
JUNIT_JAR="${JUNIT_JAR:-lib/junit-platform-console-standalone.jar}"
[ -f "$JUNIT_JAR" ] || ./scripts/get-junit.sh
./scripts/build.sh
rm -rf build/test-classes
mkdir -p build/test-classes build/test-reports
javac -encoding UTF-8 -cp "$JUNIT_JAR:build/classes" -d build/test-classes \
      $(find src/test/java -name '*.java')
java -jar "$JUNIT_JAR" execute \
     --class-path "build/classes:build/test-classes" \
     --scan-class-path build/test-classes \
     --reports-dir build/test-reports \
     --details=tree
