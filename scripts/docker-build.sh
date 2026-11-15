#!/usr/bin/env bash
# Construit l'image Docker puis vérifie qu'elle fonctionne (mode non interactif).
set -euo pipefail
cd "$(dirname "$0")/.."
IMAGE="${IMAGE_NAME:-devopsquiz}"
VERSION="${APP_VERSION:-$(git describe --tags --always 2>/dev/null || echo dev)}"

docker build -t "${IMAGE}:${VERSION}" -t "${IMAGE}:latest" .
echo "--- Vérification non interactive de l'image ---"
docker run --rm "${IMAGE}:${VERSION}" --check
echo "Image ${IMAGE}:${VERSION} construite et vérifiée."
