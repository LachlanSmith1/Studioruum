package gui;

import java.util.*;

public class DBTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDB db = new LocalDB();

        /*for (int i = 0; i <= 5; i++) {
            System.out.println("Input the dictionary name: ");
            String dictName = scanner.nextLine();
            db.saveDictionary(dictName);
        }*/

        /*for (int i = 1; i <= 5; i++) {
            System.out.println("Input front content: ");
            String frontContent = scanner.nextLine();
            System.out.println("Input back content: ");
            String backContent = scanner.nextLine();
            db.saveFlashcard(1, frontContent, backContent);
        }*/

        /*System.out.println("Input front content: ");
        String frontContent = scanner.nextLine();
        System.out.println("Input back content: ");
        String backContent = scanner.nextLine();
        db.saveFlashcard(1, 1, frontContent, backContent);*/

        /*List<Note> notes = db.allNotes();
        for (Note note : notes) {
            System.out.println("Note ID: " + note.getDict());
            System.out.println("Resource ID: " + note.resourceID);
            System.out.println("Note Title: " + note.getTitle());
            System.out.println("Note Content: " + note.getContent());
        }*/

        /*System.out.println("Input the note_id to delete: ");
        int noteID = scanner.nextInt();
        db.deleteNote(noteID);*/

        List<Flashcard> flashcards = db.allFlashcards(1);
        for (Flashcard flashcard : flashcards) {
            System.out.println("Flashcard ID: " + flashcard.getFID());
            System.out.println("Resource ID: " + flashcard.resourceID);
            if (flashcard.getQuiz() != 0) {
                System.out.println("Quiz ID: " + flashcard.getQuiz());
            }
            System.out.println("Front: " + flashcard.frontProperty().getValue());
            System.out.println("Back: " + flashcard.backProperty().getValue());
        }

        /*System.out.println("Input the dictionary name: ");
        String dictionaryName = scanner.nextLine();
        db.saveDictionary(dictionaryName);

        System.out.println("Input the quiz name: ");
        String quizName = scanner.nextLine();
        System.out.println("Input the quiz topic: ");
        String quizTopic = scanner.nextLine();
        db.saveQuiz(quizName, quizTopic);*/
    }
}
