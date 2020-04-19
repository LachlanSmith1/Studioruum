package com.example.studioruum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.*;

public class LocalDB extends SQLiteOpenHelper {
    private String sql = null;
    private SQLiteStatement stmt = null;

    public LocalDB(Context context) {
        super(context, "studioruum_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates resources table
        sql = "CREATE TABLE resources (" +
                "resource_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ")";
        db.execSQL(sql);

        // Creates notes table
        sql = "CREATE TABLE notes (" +
                "note_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "resource_id INTEGER NOT NULL," +
                "note_title VARCHAR(32) NOT NULL," +
                "note_content VARCHAR(255) NOT NULL," +
                "FOREIGN KEY(resource_id) REFERENCES resources(resource_id)" +
                ")";
        db.execSQL(sql);

        // Creates dictionaries table
        sql = "CREATE TABLE dictionaries (" +
                "dictionary_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "resource_id INTEGER NOT NULL," +
                "dictionary_name VARCHAR(32) NOT NULL," +
                "FOREIGN KEY(resource_id) REFERENCES resources(resource_id)" +
                ")";
        db.execSQL(sql);

        // Creates flashcards table
        sql = "CREATE TABLE flashcards (" +
                "flashcard_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "resource_id INTEGER NOT NULL," +
                "dictionary_id INTEGER NOT NULL," +
                "front_content VARCHAR(255) NOT NULL," +
                "back_content VARCHAR(255) NOT NULL," +
                "FOREIGN KEY(dictionary_id) REFERENCES dictionaries(dictionary_id)," +
                "FOREIGN KEY(resource_id) REFERENCES resources(resource_id)" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops resources table
        sql = "DROP TABLE IF EXISTS resources";
        db.execSQL(sql);

        // Drops notes table
        sql = "DROP TABLE IF EXISTS notes";
        db.execSQL(sql);

        // Drops dictionaries table
        sql = "DROP TABLE IF EXISTS dictionaries";
        db.execSQL(sql);

        // Drops flashcards table
        sql = "DROP TABLE IF EXISTS flashcards";
        db.execSQL(sql);

        // Create database from scratch
        onCreate(db);
    }

    public List allResources() {
        SQLiteDatabase localDB = this.getWritableDatabase();
        List<Hashtable> resourceList = new ArrayList<Hashtable>();

        try {
            sql = "SELECT * FROM resources";
            Cursor rs = localDB.rawQuery(sql, null);

            while (rs.moveToNext()) {
                Hashtable record = new Hashtable();

                record.put("resource_id", rs.getInt(rs.getColumnIndexOrThrow("resource_id")));

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
        SQLiteDatabase localDB = this.getWritableDatabase();

        try {
            sql = "INSERT INTO resources VALUES (null)";
            localDB.execSQL(sql);
        } catch (Exception e) {
            System.out.println("saveResource Error: " + e.getMessage());
        }
    }

    // Deletes resource record from local db
    // Needs a resource_id as a parameter to locate record to delete
    public void deleteResource(int resourceId) {
        SQLiteDatabase localDB = this.getWritableDatabase();

        try {
            stmt = localDB.compileStatement("DELETE FROM resources WHERE resource_id = (?)");
            stmt.bindString(1, String.valueOf(resourceId));
            stmt.executeUpdateDelete();
        } catch (Exception e) {
            System.out.println("deleteResource Error: " + e.getMessage());
        }
    }

    // Now returns list of com.example.studioruum.Note objects containing all notes in the local db
    public List allNotes() {
        SQLiteDatabase localDB = this.getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        try {
            sql = "SELECT * FROM notes";
            Cursor rs = localDB.rawQuery(sql, null);

            while (rs.moveToNext()) {
                // Fetch all values of current record
                int noteId = rs.getInt(rs.getColumnIndexOrThrow("note_id"));
                int resourceId = rs.getInt(rs.getColumnIndexOrThrow("resource_id"));
                String noteTitle = rs.getString(rs.getColumnIndexOrThrow("note_title"));
                String noteContent = rs.getString(rs.getColumnIndexOrThrow("note_content"));

                // Create com.example.studioruum.Note object and add to list
                Note record = new Note(noteId, resourceId, noteTitle, noteContent);
                noteList.add(record);
            }

        } catch (Exception e) {
            System.out.println("allNotes Error: " + e.getMessage());
        }

        return noteList;
    }

    // Saves note record in local db
    public void saveNote(String noteTitle, String noteContent) {
        SQLiteDatabase localDB = this.getWritableDatabase();

        try {
            // Save as resource and retrieve automatically generated id value
            saveResource();
            sql = "SELECT MAX(resource_id) AS last_id FROM resources";
            Cursor rs = localDB.rawQuery(sql, null);
            int resourceId = 0;

            while (rs.moveToNext()) {
                resourceId = rs.getInt(rs.getColumnIndexOrThrow("last_id"));
            }

            // Save as note using retrieved resource_id
            stmt = localDB.compileStatement("INSERT INTO notes(note_id, resource_id, note_title, note_content) VALUES(null, ?, ?, ?)");
            stmt.bindString(1, String.valueOf(resourceId));
            stmt.bindString(2, noteTitle);
            stmt.bindString(3, noteContent);
            stmt.executeInsert();
        } catch (Exception e) {
            System.out.println("saveNote Error: " + e.getMessage());
        }
    }

    // Deletes note record from local db
    public void deleteNote(int noteId) {
        SQLiteDatabase localDB = this.getWritableDatabase();

        try {
            // Retrieve corresponding resource_id for note to delete
            String[] columns = {"resource_id"};
            Cursor rs = localDB.query("notes", columns, "note_id = '" + noteId + "'", null, null, null, null);
            int resourceId = 0;

            while (rs.moveToNext()) {
                resourceId = rs.getInt(rs.getColumnIndexOrThrow("resource_id"));
            }

            // Delete record from resources table
            deleteResource(resourceId);

            // Delete record from notes table
            stmt = localDB.compileStatement("DELETE FROM notes WHERE note_id = (?)");
            stmt.bindString(1, String.valueOf(noteId));
            stmt.executeUpdateDelete();
        } catch (Exception e) {
            System.out.println("deleteNote Error: " + e.getMessage());
        }
    }

    // Updates a note record from local db
    public void updateNote(int noteId, String noteTitle, String noteContent) {
        SQLiteDatabase localDB = this.getWritableDatabase();

        try {
            stmt = localDB.compileStatement("UPDATE notes SET note_title = ?, note_content = ? WHERE note_id = (?)");
            stmt.bindString(1, noteTitle);
            stmt.bindString(2, noteContent);
            stmt.bindString(3, String.valueOf(noteId));
            stmt.executeUpdateDelete();
        } catch (Exception e) {
            System.out.println("updateNote Error: " + e.getMessage());
        }
    }

    // Retrieves note record from local db
    // Returns hash table (similar to a dictionary) with content of selected record
    public Note retrieveNote(int noteId) {
        SQLiteDatabase localDB = this.getWritableDatabase();
        Note record = new Note(noteId, 0, "", "");

        try {
            String[] columns = {"*"};
            Cursor rs = localDB.query("notes", columns, "note_id = '" + noteId + "'", null, null, null, null);

            while (rs.moveToNext()) {
                int resourceId = rs.getInt(rs.getColumnIndexOrThrow("resource_id"));
                String noteTitle = rs.getString(rs.getColumnIndexOrThrow("note_title"));
                String noteContent = rs.getString(rs.getColumnIndexOrThrow("note_content"));

                record.resourceID = resourceId;
                record.setTitle(noteTitle);
                record.setContent(noteContent);
            }
        } catch (Exception e) {
            System.out.println("retrieveNote Error: " + e.getMessage());
        }

        return record;
    }
/*
    // Returns list of hashtable containing all quizzes in the local db
    public List allQuizzes() {
        List<Hashtable> quizList = new ArrayList<Hashtable>();

        try {
            refreshConnection();
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
            refreshConnection();

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
            refreshConnection();

            // Retrieve corresponding resource_id for quiz to delete
            sql = "SELECT resource_id AS id_to_delete FROM quizzes WHERE quiz_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();
            int resourceId = rs.getInt("id_to_delete");

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
            refreshConnection();

            sql = "SELECT * FROM quizzes WHERE quiz_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

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

    // Now returns list of com.example.studioruum.Dictionary containing all dictionaries in the local db
    public List allDictionaries() {
        List<com.example.studioruum.Dictionary> dictionaryList = new ArrayList<com.example.studioruum.Dictionary>();

        try {
            refreshConnection();
            stmt = conn.createStatement();
            sql = "SELECT * FROM dictionaries";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Fetch all values of current record
                int dictionaryId = rs.getInt("dictionary_id");
                int resourceId = rs.getInt("resource_id");
                String dictionaryName = rs.getString("dictionary_name");

                // Create com.example.studioruum.Dictionary object and add to list
                com.example.studioruum.Dictionary record = new com.example.studioruum.Dictionary(dictionaryId, resourceId, dictionaryName);
                dictionaryList.add(record);
            }
        } catch (Exception e) {
            System.out.println("allDictionaries Error: " + e.getMessage());
        }

        return dictionaryList;
    }

    public void saveDictionary(String dictionaryName) {
        try {
            refreshConnection();

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
            refreshConnection();

            // Retrieve corresponding resource_id for dictionary to delete
            sql = "SELECT resource_id AS id_to_delete FROM dictionaries WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictionaryId);
            ResultSet rs = pstmt.executeQuery();
            int resourceId = rs.getInt("id_to_delete");

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
            refreshConnection();
            sql = "SELECT * FROM dictionaries WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictionaryId);
            ResultSet rs = pstmt.executeQuery();

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

    // Updates a dictionary record from local db
    public void updateDictionary(int dictId, String dictName) {
        try {
            refreshConnection();
            sql = "UPDATE dictionaries SET dictionary_name = ? WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dictName);
            pstmt.setInt(2, dictId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("updateNote Error: " + e.getMessage());
        }
    }

    // Returns list of com.example.studioruum.Flashcard containing all flashcards in dictionary passed as param
    public List allFlashcards(int dictID) {
        List<com.example.studioruum.Flashcard> flashcardList = new ArrayList<com.example.studioruum.Flashcard>();

        try {
            refreshConnection();
            sql = "SELECT * FROM flashcards WHERE dictionary_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dictID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Fetch all values of current record
                int flashcardId = rs.getInt("flashcard_id");
                int resourceId = rs.getInt("resource_id");
                int dictionaryId = rs.getInt("dictionary_id");
                int quizId = rs.getInt("quiz_id");
                String frontContent = rs.getString("front_content");
                String backContent = rs.getString("back_content");

                // Create com.example.studioruum.Flashcard object and add to list
                com.example.studioruum.Flashcard record;

                if (quizId != 0) {
                    record = new com.example.studioruum.Flashcard(flashcardId, resourceId, dictionaryId, quizId, frontContent, backContent);
                    flashcardList.add(record);
                }

                else {
                    record = new com.example.studioruum.Flashcard(flashcardId, resourceId, dictionaryId, frontContent, backContent);
                    flashcardList.add(record);
                }
            }
        } catch (Exception e) {
            System.out.println("allFlashcards Error: " + e.getMessage());
        }

        return flashcardList;
    }

    // Saves flashcard WITH quiz_id in local db
    public void saveFlashcard(int dictionaryId, int quizId, String frontContent, String backContent) {
        try {
            refreshConnection();

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

    // Saves flashcard W/O quiz_id in local_db
    public void saveFlashcard(int dictionaryId, String frontContent, String backContent) {
        try {
            refreshConnection();

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
            sql = "INSERT INTO flashcards(resource_id, dictionary_id, front_content, back_content) VALUES(?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resourceId);
            pstmt.setInt(2, dictionaryId);
            pstmt.setString(3, frontContent);
            pstmt.setString(4, backContent);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("saveFlashcard (w/o quiz_id) Error: " + e.getMessage());
        }
    }

    public void deleteFlashcard(int flashcardId) {
        try {
            refreshConnection();

            // Retrieve corresponding resource_id for flashcard to delete
            sql = "SELECT resource_id AS id_to_delete FROM flashcards WHERE flashcard_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flashcardId);
            ResultSet rs = pstmt.executeQuery();
            int resourceId = rs.getInt("id_to_delete");

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

    // Updates a flashcard record from local db
    public void updateFlashcard(int flashcardId, String frontContent, String backContent) {
        try {
            refreshConnection();
            sql = "UPDATE flashcards SET front_content = ?, back_content = ? WHERE flashcard_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, frontContent);
            pstmt.setString(2, backContent);
            pstmt.setInt(3, flashcardId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("updateFlashcard Error: " + e.getMessage());
        }
    }

    // Now returns object of com.example.studioruum.Flashcard
    public com.example.studioruum.Flashcard retrieveFlashcard(int flashcardId) {
        com.example.studioruum.Flashcard record = new com.example.studioruum.Flashcard(0, 0, 0, 0, "", "");

        try {
            refreshConnection();
            sql = "SELECT * FROM flashcards WHERE flashcard_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, flashcardId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int resourceId = rs.getInt("resource_id");
                int dictionaryId = rs.getInt("dictionary_id");
                int quizId = rs.getInt("quiz_id");
                String frontContent = rs.getString("front_content");
                String backContent = rs.getString("back_content");

                if (quizId != 0) {
                    record.setFID(flashcardId);
                    record.resourceID = resourceId;
                    record.setDict(dictionaryId);
                    record.setQuiz(quizId);
                    record.setFront(frontContent);
                    record.setBack(backContent);
                }

                else {
                    record.setFID(flashcardId);
                    record.resourceID = resourceId;
                    record.setDict(dictionaryId);
                    record.setFront(frontContent);
                    record.setBack(backContent);
                }
            }
        } catch (Exception e) {
            System.out.println("retrieveFlashcard Error: " + e.getMessage());
        }

        return record;
    }
*/
}
