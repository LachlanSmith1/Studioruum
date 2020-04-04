public class DBTest {
    public static void main(String[] args) {
        LocalDB db = new LocalDB();

        db.saveNote("java test", "java test");
        db.saveDictionary("java test");
        db.saveQuiz("java test", "java test");
    }
}
