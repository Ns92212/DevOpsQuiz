package quiz;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class QuizTest {

    private static Question sample() {
        return Question.of("Linux", "Quelle commande affiche le répertoire courant ?", "pwd", "ls", "cd", "dir");
    }

    @Test
    void bonneReponseEstReconnue() {
        Question q = sample();
        assertTrue(QuizService.isCorrect(q, q.correctLetter()));
        assertTrue(QuizService.isCorrect(q, Character.toLowerCase(q.correctLetter())));
    }

    @Test
    void mauvaiseReponseEstRefusee() {
        Question q = sample();
        char wrong = q.correctLetter() == 'A' ? 'B' : 'A';
        assertFalse(QuizService.isCorrect(q, wrong));
    }

    @Test
    void calculDuScoreEtDuPourcentage() {
        Question q1 = sample();
        List<Question> qs = List.of(q1, q1, q1, q1);
        char ok = q1.correctLetter();
        char ko = ok == 'A' ? 'B' : 'A';
        int score = QuizService.computeScore(qs, List.of(ok, ok, ko, ok));
        assertEquals(3, score);
        assertEquals(75, QuizService.percentage(score, 4));
        assertEquals(0, QuizService.percentage(0, 0));
    }

    @Test
    void categorieValideEtInvalide() {
        assertTrue(QuizService.isValidCategory("Docker"));
        assertTrue(QuizService.isValidCategory("  git "));
        assertFalse(QuizService.isValidCategory("Cuisine"));
        assertFalse(QuizService.isValidCategory(null));
    }

    @Test
    void saisieInvalideEstRejetee() {
        assertEquals(Optional.of('B'), QuizService.parseAnswer(" b "));
        assertTrue(QuizService.parseAnswer("Z").isEmpty());
        assertTrue(QuizService.parseAnswer("").isEmpty());
        assertTrue(QuizService.parseAnswer("AB").isEmpty());
        assertTrue(QuizService.parseAnswer(null).isEmpty());
        assertTrue(QuizService.parseMenuChoice("abc", 4).isEmpty());
        assertTrue(QuizService.parseMenuChoice("5", 4).isEmpty());
        assertEquals(Optional.of(3), QuizService.parseMenuChoice(" 3 ", 4));
    }

    @Test
    void banqueDeQuestionsEstComplete() {
        List<Question> bank = QuestionBank.all();
        assertTrue(bank.size() >= 20);
        for (String cat : QuestionBank.CATEGORIES) {
            assertTrue(bank.stream().filter(q -> q.category().equals(cat)).count() >= 10,
                    "Il faut au moins 10 questions pour " + cat);
        }
        bank.forEach(q -> assertEquals(4, q.choices().size()));
    }

    @Test
    void selectionDeDixQuestionsDansUneCategorie() {
        List<Question> sel = QuizService.selectQuestions(QuestionBank.all(), "Git", 10, new Random(1));
        assertEquals(10, sel.size());
        assertTrue(sel.stream().allMatch(q -> q.category().equals("Git")));
        assertEquals(10, QuizService.selectQuestions(QuestionBank.all(), "", 10, new Random(1)).size());
    }

    @Test
    void sauvegardeEtLectureDesScores(@TempDir Path dir) {
        ScoreStore store = new ScoreStore(dir.resolve("sous-dossier/scores.csv"));
        assertTrue(store.save("Alice", 8, 10));
        assertTrue(store.save("Bob;x", 5, 10));
        List<ScoreStore.Entry> top = store.top(10);
        assertEquals(2, top.size());
        assertEquals("Alice", top.get(0).pseudo());
        assertEquals("Bob x", top.get(1).pseudo());
    }
}
