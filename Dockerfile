# ---- Étape 1 : compilation avec javac (sans Maven) ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY src/main/java ./src/main/java
RUN mkdir -p /out && javac -encoding UTF-8 -d /out $(find src/main/java -name '*.java')

# ---- Étape 2 : image d'exécution légère ----
FROM eclipse-temurin:21-jre
ENV LC_ALL=C.UTF-8 \
    SCORES_FILE=/data/scores.csv
WORKDIR /app
COPY --from=build /out /app

# Utilisateur non-root + dossier de scores (monté en volume)
RUN useradd -r -u 1001 quiz && mkdir -p /data && chown quiz /data
USER quiz
VOLUME /data

# Vérification non interactive de l'application
HEALTHCHECK --interval=60s --timeout=10s --retries=3 \
  CMD java -cp /app quiz.DevOpsQuiz --check || exit 1

ENTRYPOINT ["java", "-cp", "/app", "quiz.DevOpsQuiz"]
