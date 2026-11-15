#!/usr/bin/env bash
# Déploie la dernière image : remplace le conteneur, conserve les scores dans un volume.
set -euo pipefail
IMAGE="${IMAGE_NAME:-devopsquiz}"
NAME="${CONTAINER_NAME:-devopsquiz-app}"
VOLUME="${VOLUME_NAME:-devopsquiz-data}"

docker rm -f "$NAME" >/dev/null 2>&1 || true
# -d -i -t : le conteneur reste actif (le menu attend une saisie) ; -v : scores persistants
docker run -dit --name "$NAME" \
  -v "${VOLUME}:/data" \
  --restart unless-stopped \
  "${IMAGE}:latest"
echo "Conteneur ${NAME} démarré."
docker ps --filter "name=${NAME}"
echo "Jouer : docker exec -it ${NAME} java -cp /app quiz.DevOpsQuiz"
