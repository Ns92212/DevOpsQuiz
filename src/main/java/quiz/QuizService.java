package quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/** Logique métier pure (sans entrée/sortie), facile à tester. */
public final class QuizService {

    private QuizService() { }

    /** Convertit une saisie en lettre A-D (insensible à la casse), sinon vide. */
    public static Optional<Character> parseAnswer(String input) {
        if (input == null) {
            return Optional.empty();
        }
        String s = input.trim().toUpperCase();
        if (s.length() == 1 && s.charAt(0) >= 'A' && s.charAt(0) <= 'D') {
            return Optional.of(s.charAt(0));
        }
        return Optional.empty();
    }

    /** Convertit une saisie en entier compris entre 1 et max, sinon vide. */
    public static Optional<Integer> parseMenuChoice(String input, int max) {
        if (input == null) {
            return Optional.empty();
        }
        try {
            int n = Integer.parseInt(input.trim());
            return (n >= 1 && n <= max) ? Optional.of(n) : Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static boolean isCorrect(Question q, char answer) {
        return Character.toUpperCase(answer) == q.correctLetter();
    }

    /** Un point par bonne réponse. */
    public static int computeScore(List<Question> questions, List<Character> answers) {
        int score = 0;
        for (int i = 0; i < questions.size() && i < answers.size(); i++) {
            if (isCorrect(questions.get(i), answers.get(i))) {
                score++;
            }
        }
        return score;
    }

    public static int percentage(int score, int total) {
        return total <= 0 ? 0 : (int) Math.round(100.0 * score / total);
    }

    public static String finalMessage(int score, int total) {
        int pct = percentage(score, total);
        if (pct >= 90) return "Excellent ! Vous maîtrisez le sujet.";
        if (pct >= 70) return "Très bien ! Encore un petit effort pour la perfection.";
        if (pct >= 50) return "Pas mal ! Révisez quelques notions et réessayez.";
        return "Continuez à vous entraîner, vous allez progresser !";
    }

    public static boolean isValidCategory(String name) {
        if (name == null) {
            return false;
        }
        return QuestionBank.CATEGORIES.stream().anyMatch(c -> c.equalsIgnoreCase(name.trim()));
    }

    /**
     * Sélectionne {@code n} questions au hasard.
     * Catégorie nulle ou vide = quiz général (toutes les catégories).
     */
    public static List<Question> selectQuestions(List<Question> bank, String category, int n, Random rnd) {
        List<Question> pool = new ArrayList<>();
        for (Question q : bank) {
            if (category == null || category.isBlank() || q.category().equalsIgnoreCase(category.trim())) {
                pool.add(q);
            }
        }
        Collections.shuffle(pool, rnd);
        return new ArrayList<>(pool.subList(0, Math.min(n, pool.size())));
    }
}
