# DevOpsQuiz

Quiz console en Java (Linux, Git, Jenkins, Docker, DevOps) — 50 questions, parties de 10 questions.
Compilation et tests **sans Maven** (javac + JUnit Platform Console Standalone).

## Prérequis (Ubuntu / WSL)
```bash
sudo apt update && sudo apt install -y openjdk-21-jdk-headless git curl
```

## Commandes
```bash
./scripts/build.sh      # javac -> build/classes
./scripts/run.sh        # compile puis lance le jeu
./scripts/test.sh       # télécharge JUnit (lib/), compile et exécute les tests
java -cp build/classes quiz.DevOpsQuiz --check     # vérification non interactive (pipeline)
java -cp build/classes quiz.DevOpsQuiz --version
```

Variable d'environnement : `SCORES_FILE` (défaut `data/scores.csv`).

> Les sections Jenkins, Docker, release et supervision seront ajoutées aux étapes suivantes.
