import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Database {

	// The connection to the database
	Connection conn = null;

	// The stament to be excecuted
	Statement stmt = null;

	// The content of the statement
	String sql = null;

	public Database() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:StudioruumDB.sqlite");
			System.out.println("Connected to database");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// Tests selecting from table "flashcards"
	public void testSelect() {
		try {
			this.stmt = conn.createStatement();
			sql = "SELECT * FROM flashcards";

			// The set of rows returned by the query
			ResultSet rs = stmt.executeQuery(sql);

			// Iterate through returned rows and print
			while (rs.next()) {
				int flashcardId = rs.getInt("flashcard_id");
				int resourceId = rs.getInt("resource_id");
				int dictionaryId = rs.getInt("dictionary_id");
				int quizId = rs.getInt("quiz_id");
				String frontContent = rs.getString("front_content");
				String backContent = rs.getString("back_content");

				System.out.println("flashcard_id: " + flashcardId + ", resource_id: "+ resourceId + ", dictionary_id: "+ dictionaryId + ", quiz_id: "+ quizId + ", front_content: "+ frontContent + ", back_content: "+ backContent);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
