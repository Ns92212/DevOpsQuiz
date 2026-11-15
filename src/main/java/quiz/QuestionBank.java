package quiz;

import java.util.ArrayList;
import java.util.List;

/** Banque de questions : 10 questions par catégorie (50 au total). */
public final class QuestionBank {

    public static final List<String> CATEGORIES = List.of("Linux", "Git", "Jenkins", "Docker", "DevOps");

    private QuestionBank() { }

    public static List<Question> all() {
        List<Question> l = new ArrayList<>();

        // ---------- Linux ----------
        l.add(Question.of("Linux", "Quelle commande affiche le répertoire courant ?", "pwd", "ls", "cd", "dir"));
        l.add(Question.of("Linux", "Quelle commande liste le contenu d'un répertoire ?", "ls", "cat", "mkdir", "touch"));
        l.add(Question.of("Linux", "Quelle commande modifie les permissions d'un fichier ?", "chmod", "chown", "chdir", "mv"));
        l.add(Question.of("Linux", "Quelle commande affiche les dernières lignes d'un fichier ?", "tail", "head", "less", "wc"));
        l.add(Question.of("Linux", "Quelle commande crée un répertoire ?", "mkdir", "rmdir", "touch", "cd"));
        l.add(Question.of("Linux", "Quelle commande recherche un motif dans un fichier ?", "grep", "find", "sed", "ping"));
        l.add(Question.of("Linux", "Quelle commande supprime un fichier ?", "rm", "rd", "cp", "ln"));
        l.add(Question.of("Linux", "Quelle commande exécute une commande avec les droits administrateur ?", "sudo", "su -c", "root", "admin"));
        l.add(Question.of("Linux", "Quelle commande affiche les processus en cours ?", "ps", "pwd", "df", "ls"));
        l.add(Question.of("Linux", "Quel opérateur ajoute la sortie à la fin d'un fichier sans l'écraser ?", ">>", ">", "|", "<"));

        // ---------- Git ----------
        l.add(Question.of("Git", "Quelle commande initialise un dépôt Git ?", "git init", "git start", "git create", "git new"));
        l.add(Question.of("Git", "Quelle commande ajoute des fichiers à la zone de staging ?", "git add", "git commit", "git push", "git stage-all"));
        l.add(Question.of("Git", "Quelle commande enregistre les modifications dans l'historique local ?", "git commit", "git add", "git fetch", "git clone"));
        l.add(Question.of("Git", "Quelle commande envoie les commits vers le dépôt distant ?", "git push", "git pull", "git merge", "git log"));
        l.add(Question.of("Git", "Quelle commande crée une branche nommée dev ?", "git branch dev", "git merge dev", "git clone dev", "git tag dev"));
        l.add(Question.of("Git", "Quelle commande fusionne une branche dans la branche courante ?", "git merge", "git branch", "git status", "git remote"));
        l.add(Question.of("Git", "Quelle commande copie un dépôt distant en local ?", "git clone", "git fork", "git copy", "git init"));
        l.add(Question.of("Git", "Quelle commande crée l'étiquette v1.0.0 ?", "git tag v1.0.0", "git label v1.0.0", "git version v1.0.0", "git release v1.0.0"));
        l.add(Question.of("Git", "Quelle commande affiche l'historique des commits ?", "git log", "git diff", "git show-all", "git history"));
        l.add(Question.of("Git", "À quoi sert le fichier .gitignore ?", "À lister les fichiers que Git ne doit pas suivre",
                "À supprimer des fichiers du disque", "À chiffrer le dépôt", "À configurer le mot de passe GitHub"));

        // ---------- Jenkins ----------
        l.add(Question.of("Jenkins", "Qu'est-ce que Jenkins ?", "Un serveur d'automatisation CI/CD",
                "Un système de gestion de versions", "Un langage de programmation", "Un hyperviseur"));
        l.add(Question.of("Jenkins", "Quel fichier décrit un pipeline Jenkins stocké dans le dépôt ?", "Jenkinsfile", "pipeline.xml", "build.gradle", "Makefile.jenkins"));
        l.add(Question.of("Jenkins", "Quel est le port par défaut de l'interface web de Jenkins ?", "8080", "80", "3000", "5432"));
        l.add(Question.of("Jenkins", "Dans un pipeline déclaratif, quel bloc regroupe les étapes (stage) ?", "stages", "steps-all", "jobs", "tasks"));
        l.add(Question.of("Jenkins", "Quelle instruction exécute une commande shell dans un pipeline ?", "sh", "run", "exec-shell", "cmd-line"));
        l.add(Question.of("Jenkins", "À quoi sert un webhook GitHub avec Jenkins ?", "À déclencher un build automatiquement après un push",
                "À sauvegarder le dépôt", "À créer des branches", "À compiler en local sur le poste du développeur"));
        l.add(Question.of("Jenkins", "Quelle couleur indique généralement un build en échec dans Jenkins ?", "Rouge", "Bleu", "Vert", "Violet"));
        l.add(Question.of("Jenkins", "Que permet le bloc post d'un pipeline ?", "Exécuter des actions selon le résultat (always, success, failure)",
                "Publier le code sur GitHub", "Déclarer les variables d'environnement", "Installer Jenkins"));
        l.add(Question.of("Jenkins", "Que fait la directive when dans un stage ?", "Elle conditionne l'exécution de l'étape",
                "Elle planifie un redémarrage du serveur", "Elle supprime l'espace de travail", "Elle crée un tag Git"));
        l.add(Question.of("Jenkins", "Qu'est-ce qu'un agent Jenkins ?", "Une machine ou un processus qui exécute les builds",
                "Un plugin de messagerie", "Un utilisateur administrateur", "Un type de webhook"));

        // ---------- Docker ----------
        l.add(Question.of("Docker", "Quel fichier décrit la construction d'une image Docker ?", "Dockerfile", "docker.yml", "image.json", "Containerfile.sh"));
        l.add(Question.of("Docker", "Quelle commande construit une image ?", "docker build", "docker make", "docker create-image", "docker compile"));
        l.add(Question.of("Docker", "Quelle commande lance un conteneur à partir d'une image ?", "docker run", "docker start-image", "docker launch", "docker exec-image"));
        l.add(Question.of("Docker", "Quelle commande liste les conteneurs en cours d'exécution ?", "docker ps", "docker images", "docker list-run", "docker top-all"));
        l.add(Question.of("Docker", "Quelle est la différence entre une image et un conteneur ?", "Une image est un modèle en lecture seule, un conteneur en est une instance en cours d'exécution",
                "Aucune, ce sont des synonymes", "Un conteneur est un modèle, une image est son exécution", "Une image n'existe que sous Windows"));
        l.add(Question.of("Docker", "Quel mécanisme Docker conserve les données hors du conteneur ?", "Un volume", "Un tag", "Un registre", "Un alias"));
        l.add(Question.of("Docker", "Que fait l'option -v dans docker run ?", "Elle monte un volume", "Elle affiche la version", "Elle active le mode verbeux uniquement", "Elle supprime le conteneur"));
        l.add(Question.of("Docker", "Quelle commande affiche les journaux d'un conteneur ?", "docker logs", "docker journal", "docker events-file", "docker cat"));
        l.add(Question.of("Docker", "Quelle instruction Dockerfile copie des fichiers dans l'image ?", "COPY", "MOVE", "PUT", "IMPORT"));
        l.add(Question.of("Docker", "Quelle commande arrête un conteneur en cours d'exécution ?", "docker stop", "docker halt-all", "docker pause-image", "docker rm-image"));

        // ---------- DevOps ----------
        l.add(Question.of("DevOps", "Que signifie CI ?", "Continuous Integration", "Code Inspection", "Container Image", "Central Infrastructure"));
        l.add(Question.of("DevOps", "Que signifie CD dans CI/CD ?", "Continuous Delivery / Deployment", "Code Debugging", "Container Deployment Only", "Central Database"));
        l.add(Question.of("DevOps", "Quel est l'objectif principal de la démarche DevOps ?", "Rapprocher le développement et l'exploitation",
                "Supprimer les tests", "Remplacer les développeurs par des serveurs", "Éviter toute automatisation"));
        l.add(Question.of("DevOps", "Quel est l'avantage principal de l'intégration continue ?", "Détecter les erreurs tôt", "Supprimer le besoin de Git", "Éviter d'écrire des tests", "Rendre le code plus long"));
        l.add(Question.of("DevOps", "Quelle étape suit BUILD dans la chaîne DevOps du projet ?", "TEST", "MONITOR", "PLAN", "OPERATE"));
        l.add(Question.of("DevOps", "À quoi sert la supervision (monitoring) ?", "Surveiller l'état et les erreurs de l'application en production",
                "Écrire le code", "Créer les branches Git", "Concevoir l'interface graphique"));
        l.add(Question.of("DevOps", "Pourquoi automatiser le build, les tests et le déploiement ?", "Pour réduire les erreurs manuelles et gagner du temps",
                "Pour éviter de versionner le code", "Pour supprimer les logs", "Pour ne plus utiliser de serveur"));
        l.add(Question.of("DevOps", "Que signifie le versionnement sémantique MAJEUR.MINEUR.CORRECTIF ?", "Un numéro de version comme 1.1.0 qui indique l'ampleur des changements",
                "Le nom du développeur", "La date de compilation", "Le nombre de tests"));
        l.add(Question.of("DevOps", "Pourquoi bloquer le déploiement si un test échoue ?", "Pour empêcher la livraison d'une version défaillante",
                "Pour accélérer le pipeline", "Pour économiser de l'espace disque", "Parce que Docker l'exige"));
        l.add(Question.of("DevOps", "Que signifie « Infrastructure as Code » ?", "Décrire l'infrastructure dans des fichiers versionnés et reproductibles",
                "Écrire du code sans infrastructure", "Louer des serveurs physiques", "Dessiner des schémas réseau à la main"));

        return l;
    }
}
