package quiz;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/** Application console DevOpsQuiz. */
public class DevOpsQuiz {

    public static final String VERSION = "1.0.0";
    static final int QUESTIONS_PER_GAME = 10;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--version")) {
            System.out.println("DevOpsQuiz v" + VERSION);
            return;
        }
        if (args.length > 0 && args[0].equals("--check")) {
            System.exit(selfCheck());
        }
        run();
    }

    /** Vérification non interactive (utilisée par le pipeline) : 0 = OK, 1 = KO. */
    static int selfCheck() {
        List<Question> bank = QuestionBank.all();
        if (bank.size() < 20) {
            System.err.println("HEALTHCHECK KO - moins de 20 questions : " + bank.size());
            return 1;
        }
        for (String cat : QuestionBank.CATEGORIES) {
            long n = bank.stream().filter(q -> q.category().equals(cat)).count();
            if (n < QUESTIONS_PER_GAME) {
                System.err.println("HEALTHCHECK KO - catégorie " + cat + " : " + n + " questions");
                return 1;
            }
        }
        System.out.println("HEALTHCHECK OK - v" + VERSION + " - " + bank.size() + " questions, "
                + QuestionBank.CATEGORIES.size() + " catégories");
        return 0;
    }

    static void run() {
        Scanner in = new Scanner(System.in);
        Path scoresPath = Path.of(System.getenv().getOrDefault("SCORES_FILE", "data/scores.csv"));
        ScoreStore store = new ScoreStore(scoresPath);
        List<Question> bank = QuestionBank.all();

        AppLog.info("Démarrage de DevOpsQuiz v" + VERSION + " (" + bank.size()
                + " questions, fichier de scores : " + scoresPath + ")");

        boolean running = true;
        while (running) {
            printMenu();
            String line = readLine(in);
            if (line == null) {
                AppLog.warn("Fin de l'entrée standard : arrêt de l'application");
                break;
            }
            Optional<Integer> choice = QuizService.parseMenuChoice(line, 4);
            if (choice.isEmpty()) {
                System.out.println("Choix invalide : entrez un nombre entre 1 et 4.");
                AppLog.warn("Saisie de menu invalide : '" + line + "'");
                continue;
            }
            switch (choice.get()) {
                case 1 -> running = playQuiz(in, bank, store);
                case 2 -> printRules();
                case 3 -> printScores(store);
                default -> running = false;
            }
        }
        AppLog.info("Arrêt de l'application");
    }

    // ---------------------------------------------------------------- affichage

    static void printMenu() {
        System.out.println();
        System.out.println("DEVOPS QUIZ");
        System.out.println("1. Commencer un quiz");
        System.out.println("2. Consulter les règles");
        System.out.println("3. Afficher les meilleurs scores");
        System.out.println("4. Quitter");
        System.out.print("Votre choix : ");
    }

    static void printRules() {
        System.out.println();
        System.out.println("=== RÈGLES ===");
        System.out.println("- Choisissez une catégorie ou le quiz général.");
        System.out.println("- Vous répondez à " + QUESTIONS_PER_GAME + " questions.");
        System.out.println("- Chaque question propose 4 choix (A, B, C, D) et une seule bonne réponse.");
        System.out.println("- Une bonne réponse = 1 point. Votre note est sur " + QUESTIONS_PER_GAME + ".");
        System.out.println("- Votre pseudonyme, votre score et la date sont enregistrés.");
    }

    static void printScores(ScoreStore store) {
        List<ScoreStore.Entry> top = store.top(10);
        System.out.println();
        System.out.println("=== MEILLEURS SCORES ===");
        if (top.isEmpty()) {
            System.out.println("Aucun score enregistré pour le moment.");
            return;
        }
        int rank = 1;
        for (ScoreStore.Entry e : top) {
            System.out.printf("%2d. %-20s %2d/%d  (%s)%n", rank++, e.pseudo(), e.score(), e.total(),
                    e.date().toString().replace('T', ' '));
        }
    }

    // ------------------------------------------------------------------- partie

    /** @return false si l'entrée standard est fermée (l'application doit s'arrêter). */
    static boolean playQuiz(Scanner in, List<Question> bank, ScoreStore store) {
        String category = askCategory(in);
        if (category == null) return false;

        String pseudo = askPseudo(in);
        if (pseudo == null) return false;

        List<Question> questions = QuizService.selectQuestions(bank, category, QUESTIONS_PER_GAME, new Random());
        String label = category.isEmpty() ? "Général" : category;
        AppLog.info("Début de partie : pseudo=" + pseudo + ", catégorie=" + label);

        List<Character> answers = new ArrayList<>();
        int number = 1;
        for (Question q : questions) {
            Character answer = askAnswer(in, q, number++, questions.size());
            if (answer == null) return false;
            answers.add(answer);
            if (QuizService.isCorrect(q, answer)) {
                System.out.println("Bonne réponse !");
            } else {
                System.out.println("Mauvaise réponse. La bonne réponse était : " + q.correctLetter()
                        + ". " + q.choices().get(q.correctIndex()));
            }
        }

        int score = QuizService.computeScore(questions, answers);
        int total = questions.size();
        System.out.println();
        System.out.println("=== RÉSULTAT ===");
        System.out.println("Note : " + score + "/" + total + " (" + QuizService.percentage(score, total) + " %)");
        System.out.println(QuizService.finalMessage(score, total));

        if (store.save(pseudo, score, total)) {
            System.out.println("Score enregistré.");
        } else {
            System.out.println("Attention : le score n'a pas pu être enregistré (voir les logs).");
        }
        AppLog.info("Fin de partie : pseudo=" + pseudo + ", catégorie=" + label
                + ", score=" + score + "/" + total);
        return true;
    }

    /** @return "" pour le quiz général, un nom de catégorie, ou null si EOF. */
    static String askCategory(Scanner in) {
        List<String> cats = QuestionBank.CATEGORIES;
        while (true) {
            System.out.println();
            System.out.println("Choisissez un quiz :");
            System.out.println("1. Quiz général");
            for (int i = 0; i < cats.size(); i++) {
                System.out.println((i + 2) + ". " + cats.get(i));
            }
            System.out.print("Votre choix : ");
            String line = readLine(in);
            if (line == null) return null;
            Optional<Integer> c = QuizService.parseMenuChoice(line, cats.size() + 1);
            if (c.isPresent()) {
                return c.get() == 1 ? "" : cats.get(c.get() - 2);
            }
            System.out.println("Choix invalide : entrez un nombre entre 1 et " + (cats.size() + 1) + ".");
            AppLog.warn("Saisie de catégorie invalide : '" + line + "'");
        }
    }

    static String askPseudo(Scanner in) {
        while (true) {
            System.out.print("Votre pseudonyme (1 à 20 caractères) : ");
            String line = readLine(in);
            if (line == null) return null;
            String p = ScoreStore.sanitize(line);
            if (!p.isEmpty() && p.length() <= 20) {
                return p;
            }
            System.out.println("Pseudonyme invalide.");
            AppLog.warn("Pseudonyme invalide saisi");
        }
    }

    static Character askAnswer(Scanner in, Question q, int number, int total) {
        System.out.println();
        System.out.println("Question " + number + "/" + total + " [" + q.category() + "]");
        System.out.println(q.text());
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + (char) ('A' + i) + ". " + q.choices().get(i));
        }
        while (true) {
            System.out.print("Votre réponse (A, B, C ou D) : ");
            String line = readLine(in);
            if (line == null) return null;
            Optional<Character> a = QuizService.parseAnswer(line);
            if (a.isPresent()) return a.get();
            System.out.println("Saisie invalide : tapez A, B, C ou D.");
            AppLog.warn("Réponse invalide : '" + line + "'");
        }
    }

    /** Lit une ligne ; retourne null si l'entrée est fermée (pas d'exception). */
    static String readLine(Scanner in) {
        return in.hasNextLine() ? in.nextLine() : null;
    }
}
