import java.util.*;

public class DBTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDB db = new LocalDB();

        System.out.println("Input the note title: ");
        String noteTitle = scanner.nextLine();
        System.out.println("Input the note content: ");
        String noteContent = scanner.nextLine();
        db.saveNote(noteTitle, noteContent);

        List<Hashtable> notes = db.allNotes();
        for (Hashtable note : notes) {
            System.out.println("Note ID: " + note.get("note_id"));
            System.out.println("Resource ID: " + note.get("resource_id"));
            System.out.println("Note Title: " + note.get("note_title"));
            System.out.println("Note Content: " + note.get("note_content"));
        }

        System.out.println("Input the note_id to delete: ");
        int noteID = scanner.nextInt();
        db.deleteNote(noteID);

        System.out.println("Input the dictionary name: ");
        String dictionaryName = scanner.nextLine();
        db.saveDictionary(dictionaryName);

        System.out.println("Input the quiz name: ");
        String quizName = scanner.nextLine();
        System.out.println("Input the quiz topic: ");
        String quizTopic = scanner.nextLine();
        db.saveQuiz(quizName, quizTopic);
    }
}
