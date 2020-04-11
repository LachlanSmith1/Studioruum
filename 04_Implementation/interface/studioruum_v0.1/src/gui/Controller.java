package gui;

import java.io.IOException;
import java.util.*;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Controller {

    //Global Variables For All Resources
    Vector<Vector<String>> flashcards = new Vector<Vector<String>>();
    Vector<Vector<String>> notes = new Vector<Vector<String>>();
    Vector<Vector<String>> dictionaries = new Vector<Vector<String>>();
    Vector<Vector<String>> quizzes = new Vector<Vector<String>>();

    public String accountType="";
    public String currentUser="";

    LocalDB locDB = new LocalDB();

    //checks if the username exists in the database
    public Boolean isUnique(String username){
        Boolean unique=true;
        //go through user table
        //if username==anything in database
        //unique=false
        return unique;
    }

    //checks if the username and password combo used for sign up is valid
    public void validSignUp(ActionEvent event) throws IOException{
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();
        TextField uname = (TextField) scene.lookup("#logintxt");
        TextField psswrd = (TextField) scene.lookup("#passwordtxt");
        TextField Repsswrd = (TextField) scene.lookup("#Repasswordtxt");
        String username = uname.getText();
        String password = psswrd.getText();
        String Repassword = Repsswrd.getText();

        if (isUnique(username)==true&&password.equals(Repassword)&&password.length()>5&&accountType!=""){
            currentUser = username;
            goHome(event);
            //create new record and add to database
        }
    }
    
    

// hyperlink on the log in page that allows user to register
    public void signUplink(ActionEvent event) throws IOException{
        Parent signUp = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Scene signUpScene = new Scene(signUp);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(signUpScene);
        window.show();
    }

    // validate user account info
    public void validNamePassword(ActionEvent event) throws IOException{
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // users input
        TextField uname = (TextField) scene.lookup("logintxt");
        TextField psswrd = (TextField) scene.lookup("passwordtxt");
        String username = uname.getText();
        String password = psswrd.getText();

        if(username.length()>5&&password.length()>5){
            currentUser = username;
            goHome(event);
        }

    }

    //Used to Download All Resources Needed For a User to Access the System
    public void loginSync(ActionEvent event) throws IOException
    {

        //Establishes a Connection
        Connection studConnect = null;

        //The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
        String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

        //Default Master Username and Password From AWS
        String user = "group40";
        String password = "zitozito";

        //Attempting to Connect
        try
        {

            studConnect = DriverManager.getConnection(host, user, password);

            if(studConnect != null)
            {

                //Preparing a Statement to Download All Resources of a User
                PreparedStatement resourceStatement = null;

                try
                {

                    //Creating a Prepared Statement
                    resourceStatement = studConnect.prepareStatement("SELECT resource_id, time_updated FROM resources WHERE username = ?;");
                    resourceStatement.setString(1, currentUser);

                    //Gather the Results of the Select
                    ResultSet resourceResults = resourceStatement.executeQuery();

                    //Preparing Statements For All Tables of Resources
                    PreparedStatement flashcardStatement = null;
                    PreparedStatement noteStatement = null;
                    PreparedStatement dictionaryStatement = null;
                    PreparedStatement quizStatement = null;

                    while(resourceResults.next())
                    {

                        String resourceID = resourceResults.getString("resource_id");

                        //Gathering All Resourced With That ID
                        flashcardStatement = studConnect.prepareStatement("SELECT * FROM flashcards WHERE resource_id = ?;");
                        flashcardStatement.setString(1, resourceID);

                        noteStatement = studConnect.prepareStatement("SELECT * FROM notes WHERE resource_id = ?;");
                        noteStatement.setString(1, resourceID);

                        dictionaryStatement = studConnect.prepareStatement("SELECT * FROM dictionaries WHERE resource_id = ?;");
                        dictionaryStatement.setString(1, resourceID);

                        quizStatement = studConnect.prepareStatement("SELECT * FROM quizzes WHERE resource_id = ?;");
                        quizStatement.setString(1, resourceID);

                        //Result Sets For All Where There is a Match
                        ResultSet flashcardResults = flashcardStatement.executeQuery();
                        ResultSet noteResults = noteStatement.executeQuery();
                        ResultSet dictionaryResults = dictionaryStatement.executeQuery();
                        ResultSet quizResults = quizStatement.executeQuery();

                        if(flashcardResults.next() != false)
                        {
                            do
                            {

                                //ADD STUFF TO THE 2D VECTOR

                            }
                            while (flashcardResults.next());
                        }

                        if(noteResults.next() != false)
                        {
                            do
                            {

                                //ADD STUFF TO THE 2D VECTOR

                            }
                            while (noteResults.next());
                        }

                        if(dictionaryResults.next() != false)
                        {
                            do
                            {

                                //ADD STUFF TO THE 2D VECTOR

                            }
                            while (dictionaryResults.next());
                        }

                        if(quizResults.next() != false)
                        {
                            do
                            {

                                //ADD STUFF TO THE 2D VECTOR

                            }
                            while (quizResults.next());
                        }

                    }




                }
                catch (SQLException ex)
                {

                    System.out.println("Error Connecting: " + ex);

                }
                finally
                {

                    try
                    {

                        resourceStatement.close();

                    }
                    catch (SQLException ex)
                    {

                        System.out.println("Error Closing: " + ex);

                    }

                }

            }

        }
        catch (SQLException ex)
        {

            System.out.println("An Error Occured When Connecting to the Database.");
            ex.printStackTrace();

        }
        finally
        {

            //Close The Connection When Finished
            if (studConnect != null)
            {

                try
                {

                    studConnect.close();

                }
                catch (SQLException ex)
                {

                    ex.printStackTrace();

                }

            }

        }


    }

    // navigation buttons
    public void goHome(ActionEvent event) throws IOException
    {

        //Used to Gather All Resources
        loginSync(event);

        Parent dest = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();

    }

    public void goFlashcard(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("view_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }

    public void goDictionary(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("view_dictionary.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }

    // Called to prepare Notes page
    public void goNotes(ActionEvent event) throws IOException {
        // Get Stage info and set destination Scene
        Parent dest = FXMLLoader.load(getClass().getResource("view_notes.fxml"));
        Scene destScene = new Scene(dest);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);

        // Lookup in the Scene for ComboBox, fetch items from local DB and add them
        ComboBox noteDropDown = (ComboBox) destScene.lookup("#noteDrpDwn");
        List<Note> notes = locDB.allNotes();
        ObservableList<Note> observableNotes = FXCollections.observableList(notes);
        noteDropDown.setItems(observableNotes);

        // Updates item in ComboBox to show only their title instead of their full instance
        Callback<ListView<Note>, ListCell<Note>> factory = lv -> new ListCell<Note>() {

            @Override
            protected void updateItem(Note item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getTitle());
            }

        };

        noteDropDown.setCellFactory(factory);
        noteDropDown.setButtonCell(factory.call(null));

        // Finally display window
        window.show();
    }

    // Displays selected Note in TextField and TextArea of the notes page
    public void displayNote(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for ComboBox, get selected item
        ComboBox noteDropDown = (ComboBox) scene.lookup("#noteDrpDwn");
        Note selected = (Note) noteDropDown.getSelectionModel().getSelectedItem();

        // Set displayed title to selected note's title
        TextField noteTitle = (TextField) scene.lookup("#noteTitle");
        noteTitle.setText(selected.getTitle());

        // Set displayed content to selected note's content
        TextArea noteContent = (TextArea) scene.lookup("#noteContent");
        noteContent.setText(selected.getContent());

        // Set "Save" button (alter note) to visible
        Button updateNote = (Button) scene.lookup("#updateNote");
        updateNote.setVisible(true);
    }

    // Makes the note title and the note content editable when "Edit" button clicked
    public void editNote(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for TextField (note title) and make it editable
        TextField noteTitle = (TextField) scene.lookup("#noteTitle");
        noteTitle.setEditable(true);

        // Lookup in the Scene for TextArea (note content) and make it editable
        TextArea noteContent = (TextArea) scene.lookup("#noteContent");
        noteContent.setEditable(true);
    }

    // Deletes a note from local DB (if user previously selected one
    public void deleteNote(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for ComboBox, get selected item
        ComboBox noteDropDown = (ComboBox) scene.lookup("#noteDrpDwn");
        Note selected = (Note) noteDropDown.getSelectionModel().getSelectedItem();

        // Try to delete note if user selected one
        if (selected != null) {
            // Create and show deletion alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Note Deletion");
            alert.setHeaderText("Trying to delete a note...");
            alert.setContentText("Are you sure you want to delete this note? You will not be able to recover it in case of deletion");
            Optional<ButtonType> result = alert.showAndWait();

            // Delete note in case user clicks "OK", reload window
            if (result.get() == ButtonType.OK) {
                locDB.deleteNote(selected.getDict());
                goNotes(event);
            }
        }

        // Show Error alert in case no note selected
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setHeaderText("No note to delete!");
            alert.setContentText("You have to select a note to delete it!");

            alert.showAndWait();
        }
    }

    // Saves the note as a new note in local DB
    public void saveNote(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for note title and retrieve it
        TextField noteTitle = (TextField) scene.lookup("#noteTitle");
        String title = noteTitle.getText();

        // Lookup in the Scene for note content and retrieve it
        TextArea noteContent = (TextArea) scene.lookup("#noteContent");
        String content = noteContent.getText();

        // Save note and reload window
        locDB.saveNote(title, content);
        goNotes(event);
    }

    // Updates record of user select note in local db
    public void updateNote(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for ComboBox, get selected item's note_id
        ComboBox noteDropDown = (ComboBox) scene.lookup("#noteDrpDwn");
        Note selected = (Note) noteDropDown.getSelectionModel().getSelectedItem();
        int noteId = selected.getDict();

        // Lookup in the Scene for note title and retrieve it
        TextField noteTitle = (TextField) scene.lookup("#noteTitle");
        String title = noteTitle.getText();

        // Lookup in the Scene for note content and retrieve it
        TextArea noteContent = (TextArea) scene.lookup("#noteContent");
        String content = noteContent.getText();

        // Create and show overwrite alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Overwrite");
        alert.setHeaderText("Trying to overwrite an existing note...");
        alert.setContentText("Are you sure you want to overwrite this note? Its previous content will be completely replaced");
        Optional<ButtonType> result = alert.showAndWait();

        // Delete note in case user clicks "OK", reload window
        if (result.get() == ButtonType.OK) {
            locDB.updateNote(noteId, title, content);
            goNotes(event);
        }
    }

    public void goBack(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("view_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }

    public void goCreateFlashcard(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("create_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }

    public void goForuum(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("foruum.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }
    public void goClassruumScholar(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("classruum_scholar.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }

    public void goClassruumEducator(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("classruum_educator.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
    }

    public void Scholarselected(MouseEvent event) throws IOException{
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        ImageView scholar = (ImageView) scene.lookup("#scholarunselect");
        ImageView educator = (ImageView) scene.lookup("#educatorunselect");
        Label scholartxt = (Label) scene.lookup("#scholartxt");
        Label educatortxt = (Label) scene.lookup("#educatortxt");

        if(scholar.getOpacity()==0){
            scholar.setOpacity(1);
            scholartxt.setStyle("-fx-font-weight: normal");
            accountType="";
        }
        else{
            scholar.setOpacity(0);
            scholartxt.setStyle("-fx-font-weight: bold");
            accountType="Scholar";
            if (educator.getOpacity()==0) {
                educator.setOpacity(1);
                educatortxt.setStyle("-fx-font-weight: normal");
            }
        }
    }

    public void Educatorselected(MouseEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        ImageView educator = (ImageView) scene.lookup("#educatorunselect");
        ImageView scholar = (ImageView) scene.lookup("#scholarunselect");
        Label educatortxt = (Label) scene.lookup("#educatortxt");
        Label scholartxt = (Label) scene.lookup("#scholartxt");

        if (educator.getOpacity()==0) {
            educator.setOpacity(1);
            educatortxt.setStyle("-fx-font-weight: normal");
            accountType="";
        }
        else {
            educator.setOpacity(0);
            educatortxt.setStyle("-fx-font-weight: bold");
            accountType="Educator";
            if(scholar.getOpacity()==0){
                scholar.setOpacity(1);
                scholartxt.setStyle("-fx-font-weight: normal");
            }
        }
    }

}
