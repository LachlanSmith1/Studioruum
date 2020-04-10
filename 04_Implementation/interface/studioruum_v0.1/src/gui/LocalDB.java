package gui;

import java.sql.*;
import java.util.*;

public class LocalDB {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    String sql = null;

    public LocalDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:StudioruumDB.sqlite");
            System.out.println("Connected to database");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List allResources() {
        List<Hashtable> resourceList = new ArrayList<Hashtable>();

        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM resources";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Hashtable record = new Hashtable();

                record.put("resource_id", rs.getInt("resource_id"));

                resourceList.add(record);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return resourceList;
    }

    // Creates record for resource in local db
    // No values to be passed as params, as resource_id auto-increments
    public void saveResource() {
        try {
            stmt = conn.createStatement();
            sql = "INSERT INTO resources VALUES (null)";
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("saveResource Error: " + e.getMessage());
        }
    }

    // Deletes resource record from local db
    // Needs a resource_id as a parameter to locate record to delete
    public void deleteResource(int resourceId) {
        try {
            sql = "DELETE FROM resources WHERE resource_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteResource Error: " + e.getMessage());
        }
    }

    // Returns list of hashtable containing all notes in the local db
    public List allNotes() {
        List<Hashtable> noteList = new ArrayList<Hashtable>();

        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM notes";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Hashtable record = new Hashtable();

                record.put("note_id", rs.getInt("note_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("note_title", rs.getString("note_title"));
                record.put("note_content", rs.getString("note_content"));

                noteList.add(record);
            }
        } catch (Exception e) {
            System.out.println("allNotes Error: " + e.getMessage());
        }

        return noteList;
    }

    // Saves note record in local db
    public void saveNote(String noteTitle, String noteContent) {
        try {
            // Save as resource and retrieve automatically generated id value
            saveResource();
            stmt = conn.createStatement();
            sql = "SELECT MAX(resource_id) AS last_id FROM resources";
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("last_id");
            }

            // Save as note using retrieved resource_id
            sql = "INSERT INTO notes(note_id, resource_id, note_title, note_content) VALUES(null, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.setString(2, noteTitle);
            pstmt.setString(3, noteContent);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("saveNote Error: " + e.getMessage());
        }
    }

    // Deletes note record from local db
    public void deleteNote(int noteId) {
        try {
            // Retrieve corresponding resource_id for note to delete
            sql = "SELECT resource_id FROM notes WHERE note_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, noteId);
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("resource_id");
            }

            // Delete record from resources table
            sql = "DELETE FROM resources WHERE resource_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.executeUpdate();

            // Delete record from notes table
            sql = "DELETE FROM notes WHERE note_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, noteId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteNote Error: " + e.getMessage());
        }
    }

    // Retrieves note record from local db
    // Returns hash table (similar to a dictionary) with content of selected record
    public Hashtable retrieveNote(int noteId) {
        Hashtable record = new Hashtable();

        try {
            sql = "SELECT * FROM notes WHERE note_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, noteId);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                record.put("note_id", rs.getInt("note_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("note_title", rs.getString("note_title"));
                record.put("note_content", rs.getString("note_content"));
            }
        } catch (Exception e) {
            System.out.println("retrieveNote Error: " + e.getMessage());
        }

        return record;
    }

    // Returns list of hashtable containing all quizzes in the local db
    public List allQuizzes() {
        List<Hashtable> quizList = new ArrayList<Hashtable>();

        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM quizzes";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Hashtable record = new Hashtable();

                record.put("quiz_id", rs.getInt("quiz_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("quiz_name", rs.getString("quiz_name"));
                record.put("quiz_topic", rs.getString("quiz_topic"));

                quizList.add(record);
            }
        } catch (Exception e) {
            System.out.println("allQuizzes Error: " + e.getMessage());
        }

        return quizList;
    }

    public void saveQuiz(String quizName, String quizTopic) {
        try {
            // Save as resource and retrieve automatically generated id value
            saveResource();
            stmt = conn.createStatement();
            sql = "SELECT MAX(resource_id) AS last_id FROM resources";
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("last_id");
            }

            // Save as quiz using retrieved resource_id
            sql = "INSERT INTO quizzes(resource_id, quiz_name, quiz_topic) VALUES(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.setString(2, quizName);
            pstmt.setString(3, quizTopic);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("saveQuiz Error: " + e.getMessage());
        }
    }

    public void deleteQuiz(int quizId) {
        try {
            // Retrieve corresponding resource_id for quiz to delete
            sql = "SELECT resource_id FROM quizzes WHERE quiz_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("resource_id");
            }

            // Delete record from resources table
            sql = "DELETE FROM resources WHERE resource_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.executeUpdate();

            // Remove reference to quiz in flashcards
            sql = "UPDATE flashcards SET quiz_id = NULL WHERE quiz_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            pstmt.executeUpdate();

            // Delete the quiz from local db
            sql = "DELETE FROM quizzes WHERE quiz_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteQuiz Error: " + e.getMessage());
        }
    }

    public Hashtable retrieveQuiz(int quizId) {
        Hashtable record = new Hashtable();

        try {
            sql = "SELECT * FROM quizzes WHERE quiz_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                record.put("quiz_id", rs.getInt("quiz_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("quiz_name", rs.getString("quiz_name"));
                record.put("quiz_topic", rs.getString("quiz_topic"));
            }
        } catch (Exception e) {
            System.out.println("retrieveQuiz Error: " + e.getMessage());
        }

        return record;
    }

    // Returns list of hashtable containing all dictionaries in the local db
    public List allDictionaries() {
        List<Hashtable> dictionaryList = new ArrayList<Hashtable>();

        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM dictionaries";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Hashtable record = new Hashtable();

                record.put("dictionary_id", rs.getInt("dictionary_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("dictionary_name", rs.getString("dictionary_name"));

                dictionaryList.add(record);
            }
        } catch (Exception e) {
            System.out.println("allDictionaries Error: " + e.getMessage());
        }

        return dictionaryList;
    }

    public void saveDictionary(String dictionaryName) {
        try {
            // Save as resource and retrieve automatically generated id value
            saveResource();
            stmt = conn.createStatement();
            sql = "SELECT MAX(resource_id) AS last_id FROM resources";
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("last_id");
            }

            // Save as dictionary using retrieved resource_id
            sql = "INSERT INTO dictionaries(resource_id, dictionary_name) VALUES(?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.setString(2, dictionaryName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("saveDictionary Error: " + e.getMessage());
        }
    }

    public void deleteDictionary(int dictionaryId) {
        try {
            // Retrieve corresponding resource_id for dictionary to delete
            sql = "SELECT resource_id FROM dictionaries WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictionaryId);
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("resource_id");
            }

            // Delete record from resources table
            sql = "DELETE FROM resources WHERE resource_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.executeUpdate();

            // Delete all flashcards contained in dictionary
            sql = "DELETE FROM flashcards WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictionaryId);
            pstmt.executeUpdate();

            // Delete dictionary
            sql = "DELETE FROM dictionaries WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictionaryId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteDictionary Error: " + e.getMessage());
        }
    }

    public Hashtable retrieveDictionary(int dictionaryId) {
        Hashtable record = new Hashtable();

        try {
            sql = "SELECT * FROM dictionaries WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictionaryId);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                record.put("dictionary_id", rs.getInt("dictionary_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("dictionary_name", rs.getString("dictionary_name"));
            }
        } catch (Exception e) {
            System.out.println("retrieveDictionary Error: " + e.getMessage());
        }

        return record;
    }

    // Returns list of hashtable containing all flashcards in the local db
    public List allFlashcards() {
        List<Hashtable> flashcardList = new ArrayList<Hashtable>();

        try {
            stmt = conn.createStatement();
            sql = "SELECT * FROM dictionaries";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Hashtable record = new Hashtable();

                record.put("flashcard_id", rs.getInt("flashcard_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("dictionary_id", rs.getInt("dictionary_id"));
                record.put("quiz_id", rs.getInt("quiz_id"));
                record.put("front_content", rs.getString("front_content"));
                record.put("back_content", rs.getString("back_content"));

                flashcardList.add(record);
            }
        } catch (Exception e) {
            System.out.println("allFlashcards Error: " + e.getMessage());
        }

        return flashcardList;
    }

    public void saveFlashcard(int dictionaryId, int quizId, String frontContent, String backContent) {
        try {
            // Save as resource and retrieve automatically generated id value
            saveResource();
            stmt = conn.createStatement();
            sql = "SELECT MAX(resource_id) AS last_id FROM resources";
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("last_id");
            }

            // Save as dictionary using retrieved resource_id
            sql = "INSERT INTO flashcards(resource_id, dictionary_id, quiz_id, front_content, back_content) VALUES(?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.setInt(2, dictionaryId);
            pstmt.setInt(3, quizId);
            pstmt.setString(4, frontContent);
            pstmt.setString(5, backContent);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("saveFlashcard Error: " + e.getMessage());
        }
    }

    public void deleteFlashcard(int flashcardId) {
        try {
            // Retrieve corresponding resource_id for flashcard to delete
            sql = "SELECT resource_id FROM flashcards WHERE flashcard_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flashcardId);
            ResultSet rs = stmt.executeQuery(sql);
            int resourceId = 0;

            while (rs.next()) {
                resourceId = rs.getInt("resource_id");
            }

            // Delete record from resources table
            sql = "DELETE FROM resources WHERE resource_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.executeUpdate();

            // Delete flashcard from local db
            sql = "DELETE FROM flashcards WHERE flashcard_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flashcardId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteFlashcard Error: " + e.getMessage());
        }
    }

    public Hashtable retrieveFlashcard(int flashcardId) {
        Hashtable record = new Hashtable();

        try {
            sql = "SELECT * FROM flashcards WHERE flashcard_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flashcardId);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                record.put("flashcard_id", rs.getInt("flashcard_id"));
                record.put("resource_id", rs.getInt("resource_id"));
                record.put("dictionary_id", rs.getInt("dictionary_id"));
                record.put("quiz_id", rs.getInt("quiz_id"));
                record.put("front_content", rs.getString("front_content"));
                record.put("back_content", rs.getString("back_content"));
            }
        } catch (Exception e) {
            System.out.println("retrieveFlashcard Error: " + e.getMessage());
        }

        return record;
    }
}
