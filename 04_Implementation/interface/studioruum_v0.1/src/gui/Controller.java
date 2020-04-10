package gui;

import java.io.IOException;
import java.util.*;

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
    
    public String accountType="";
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
            goHome(event);
        }

    }

    // navigation buttons
    public void goHome(ActionEvent event) throws IOException{
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
