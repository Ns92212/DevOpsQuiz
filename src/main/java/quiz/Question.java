package quiz;

import java.util.List;

/** Une question à choix multiple : 4 choix (A, B, C, D), une seule bonne réponse. */
public record Question(String category, String text, List<String> choices, int correctIndex) {

    public Question {
        if (choices == null || choices.size() != 4) {
            throw new IllegalArgumentException("Une question doit avoir exactement 4 choix");
        }
        if (correctIndex < 0 || correctIndex > 3) {
            throw new IllegalArgumentException("Index de bonne réponse invalide : " + correctIndex);
        }
        choices = List.copyOf(choices);
    }

    /**
     * Fabrique pratique. La position de la bonne réponse est déterministe
     * (longueur du texte modulo 4) afin que ce ne soit pas toujours la lettre A.
     */
    public static Question of(String category, String text, String correct,
                              String wrong1, String wrong2, String wrong3) {
        int idx = Math.floorMod(text.length(), 4);
        String[] wrong = {wrong1, wrong2, wrong3};
        String[] arr = new String[4];
        int w = 0;
        for (int i = 0; i < 4; i++) {
            arr[i] = (i == idx) ? correct : wrong[w++];
        }
        return new Question(category, text, List.of(arr), idx);
    }

    public char correctLetter() {
        return (char) ('A' + correctIndex);
    }
}
