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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
        // Get Stage info and set destination Scene
        Parent dest = FXMLLoader.load(getClass().getResource("view_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);

        // Lookup in the Scene for ComboBox, fetch items from local DB and add them
        ComboBox dictDropDown = (ComboBox) destScene.lookup("#dictDrpDwn");
        List<Dictionary> dictionaries = locDB.allDictionaries();
        ObservableList<Dictionary> observableDicts = FXCollections.observableList(dictionaries);
        dictDropDown.setItems(observableDicts);

        // Updates item in ComboBox to show only their title instead of their full instance
        Callback<ListView<Dictionary>, ListCell<Dictionary>> factory = lv -> new ListCell<Dictionary>() {

            @Override
            protected void updateItem(Dictionary item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getTitle());
            }

        };

        dictDropDown.setCellFactory(factory);
        dictDropDown.setButtonCell(factory.call(null));

        // Finally display window
        window.show();
    }

    // Populates flashcard combo box after user selects dictionary
    public void populateFlash(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for dictionary ComboBox, get selected item's dictionary_id
        ComboBox dictDropDown = (ComboBox) scene.lookup("#dictDrpDwn");
        Dictionary selected = (Dictionary) dictDropDown.getSelectionModel().getSelectedItem();
        int dictId = selected.getDict();

        // Lookup in Scene for flashcard content text area
        TextArea flashContent = (TextArea) scene.lookup("#flashContent");

        // Lookup in Scene for "New Flashcard" button, make visible
        Button newFlash = (Button) scene.lookup("#newFlash");
        newFlash.setVisible(true);

        // Lookup in the Scene for flashcard ComboBox, fetch items from local DB
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");
        List<Flashcard> flashcards = locDB.allFlashcards(dictId);

        // Proceed with operations if flashcards exist for current dictionary
        if (!flashcards.isEmpty()) {
            ObservableList<Flashcard> observableFlashs = FXCollections.observableList(flashcards);
            flashDropDown.setItems(observableFlashs);
            flashDropDown.setVisible(true);

            // Updates item in ComboBox to show only their title instead of their full instance
            Callback<ListView<Flashcard>, ListCell<Flashcard>> factory = lv -> new ListCell<Flashcard>() {

                @Override
                protected void updateItem(Flashcard item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : item.frontProperty().getValue());
                }

            };

            flashDropDown.setCellFactory(factory);
            flashDropDown.setButtonCell(factory.call(null));

            // Lookup in Scene for buttons to interact with flashcard, make visible
            Button editFlash = (Button) scene.lookup("#editFlash");
            editFlash.setVisible(true);
            Button deleteFlash = (Button) scene.lookup("#deleteFlash");
            deleteFlash.setVisible(true);

            // Set displayed flashcard to first flashcard in dictionary
            Flashcard firstFlash = flashcards.get(0);
            flashContent.setText(firstFlash.frontProperty().getValue());
            flashDropDown.getSelectionModel().selectFirst();

            // Lookup in Scene for "Next" and "Flip", make enabled
            Button nextFlash = (Button) scene.lookup("#nextFlash");
            nextFlash.setDisable(false);
            Button flipFlash = (Button) scene.lookup("#flipFlash");
            flipFlash.setDisable(false);
        }

        // If no flashcards for current dictionary, alert user
        else {
            flashContent.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Flashcards");
            alert.setHeaderText("No flashcards in current dictionary...");
            alert.setContentText("There are no flashcards in the current dictionary. You can add some by clicking the 'New Flashcard' button");

            alert.showAndWait();
        }
    }

    // Displays flashcard front content after user selects flashcard
    public void displayFlash(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for flashcard ComboBox, get selected item
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");
        Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();

        // Set displayed flashcard to selected one
        TextArea flashContent = (TextArea) scene.lookup("#flashContent");
        flashContent.setText(selected.frontProperty().getValue());

        // Lookup in Scene for "Flip" button, make enabled
        Button flipFlash = (Button) scene.lookup("#flipFlash");
        flipFlash.setDisable(false);

        // Make "Prev" button enabled if not first flashcard
        if (flashDropDown.getSelectionModel().getSelectedIndex() != 0) {
            Button prevFlash = (Button) scene.lookup("#prevFlash");
            prevFlash.setDisable(false);
        } else {
            Button prevFlash = (Button) scene.lookup("#prevFlash");
            prevFlash.setDisable(true);
        }

        // Make "Next" button enabled if not last flashcard
        if (flashDropDown.getSelectionModel().getSelectedIndex() != flashDropDown.getItems().size() - 1) {
            Button nextFlash = (Button) scene.lookup("#nextFlash");
            nextFlash.setDisable(false);
        } else {
            Button nextFlash = (Button) scene.lookup("#nextFlash");
            nextFlash.setDisable(true);
        }
    }

    // Flips flashcard content
    public void flipFlash(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in scene for flashcard content text area
        TextArea flashContent = (TextArea) scene.lookup("#flashContent");

        // Lookup in the Scene for flashcard ComboBox, get selected item
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");
        Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();

        // Flip to back content if front and vice-versa
        if (flashContent.getText().equals(selected.frontProperty().getValue())) {
            flashContent.setText(selected.backProperty().getValue());
        }

        else if (flashContent.getText().equals(selected.backProperty().getValue())) {
            flashContent.setText(selected.frontProperty().getValue());
        }
    }

    // Adds functionality to "Prev" button in flashcards page
    public void prevFlash(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for flashcard ComboBox
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");

        // Lookup in scene for flashcard content text area
        TextArea flashContent = (TextArea) scene.lookup("#flashContent");

        // Set selected flashcard and its content to prev flashcard if not second to first
        if (flashDropDown.getSelectionModel().getSelectedIndex() > 1) {
            flashDropDown.getSelectionModel().select(flashDropDown.getSelectionModel().getSelectedIndex() - 1);
            Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
            flashContent.setText(selected.frontProperty().getValue());
        }

        // Otherwise, set selected flashcard and its content to prev flashcard, disable "Prev" button
        else if (flashDropDown.getSelectionModel().getSelectedIndex() == 1) {
            flashDropDown.getSelectionModel().selectFirst();
            Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
            flashContent.setText(selected.frontProperty().getValue());
            Button prevFlash = (Button) scene.lookup("#prevFlash");
            prevFlash.setDisable(true);
        }
    }

    // Adds functionality to "Next" button in flashcards page
    public void nextFlash(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for flashcard ComboBox
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");

        // Lookup in scene for flashcard content text area
        TextArea flashContent = (TextArea) scene.lookup("#flashContent");

        // Set selected flashcard and its content to next flashcard if not second to last
        if (flashDropDown.getSelectionModel().getSelectedIndex() < flashDropDown.getItems().size() - 2) {
            flashDropDown.getSelectionModel().select(flashDropDown.getSelectionModel().getSelectedIndex() + 1);
            Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
            flashContent.setText(selected.frontProperty().getValue());
        }

        // Otherwise, set selected flashcard and its content to next flashcard, disable "Next" button
        else if (flashDropDown.getSelectionModel().getSelectedIndex() == flashDropDown.getItems().size() - 2) {
            flashDropDown.getSelectionModel().selectLast();
            Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
            flashContent.setText(selected.frontProperty().getValue());
            Button nextFlash = (Button) scene.lookup("#nextFlash");
            nextFlash.setDisable(true);
        }
    }

    // Deletes currently selected Flashcard from local DB
    public void deleteFlash(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for flashcard ComboBox, get selected item
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");
        Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();

        // Lookup in scene for flashcard content text area
        TextArea flashContent = (TextArea) scene.lookup("#flashContent");

        // Create and show deletion alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Flashcard Deletion");
        alert.setHeaderText("Trying to delete a flashcard...");
        alert.setContentText("Are you sure you want to delete this flashcard? You will not be able to recover its content in case of deletion");
        Optional<ButtonType> result = alert.showAndWait();

        // Delete flashcard in case user clicks "OK", skip to next flashcard
        if (result.get() == ButtonType.OK) {
            // Delete flashcard from local db
            locDB.deleteFlashcard(selected.getFID());

            // Remove deleted flashcard from combo box
            int selectedIndex = flashDropDown.getSelectionModel().getSelectedIndex();
            flashDropDown.getItems().remove(selectedIndex);

            // If there are still flashcards in dictionary, skip to next and refresh items, else alert user
            if (flashDropDown.getItems().size() > 0) {


                // Set selected flashcard and its content to next flashcard if not second to last
                if (selectedIndex < flashDropDown.getItems().size() - 2) {
                    flashDropDown.getSelectionModel().select(selectedIndex + 1);
                    selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
                    flashContent.setText(selected.frontProperty().getValue());
                }

                // If second to last, set selected flashcard and its content to next flashcard, disable "Next" button
                else if (selectedIndex == flashDropDown.getItems().size() - 2) {
                    flashDropDown.getSelectionModel().selectLast();
                    selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
                    flashContent.setText(selected.frontProperty().getValue());
                    Button nextFlash = (Button) scene.lookup("#nextFlash");
                    nextFlash.setDisable(true);
                }

                // If last, set selected flashcard and its content to next flashcard, disable "Next" button
                else if (selectedIndex == flashDropDown.getItems().size() - 1) {
                    flashDropDown.getSelectionModel().select(selectedIndex - 1);
                    selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
                    flashContent.setText(selected.frontProperty().getValue());
                    Button nextFlash = (Button) scene.lookup("#nextFlash");
                    nextFlash.setDisable(true);
                }
            } else {
                flashContent.setText("");
                flashDropDown.setVisible(false);
                Button editFlash = (Button) scene.lookup("#editFlash");
                editFlash.setVisible(false);
                Button deleteFlash = (Button) scene.lookup("#deleteFlash");
                deleteFlash.setVisible(false);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Flashcards");
                alert.setHeaderText("No flashcards in current dictionary...");
                alert.setContentText("There are no flashcards in the current dictionary. You can add some by clicking the 'New Flashcard' button");

                alert.showAndWait();
            }
        }
    }

    // Takes user to create_flashcard page, specifies operation to be executed
    public void newFlash(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for dictionary ComboBox, get selected item's dictionary_id
        ComboBox dictDropDown = (ComboBox) scene.lookup("#dictDrpDwn");
        Dictionary selected = (Dictionary) dictDropDown.getSelectionModel().getSelectedItem();
        int dictId = selected.getDict();

        // Get Stage info and set destination Scene
        Parent dest = FXMLLoader.load(getClass().getResource("create_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        window.setScene(destScene);

        // Lookup in destination scene for labels and save params
        Label dictLabel = (Label) destScene.lookup("#dictId");
        dictLabel.setText(dictId + "");
        Label opLabel = (Label) destScene.lookup("#flashOp");
        opLabel.setText("new");

        // Finally display window
        window.show();
    }

    // Takes user to create_flashcard page, specifies operation to be executed and populates text areas
    public void alterFlash(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for flashcard ComboBox, get selected item's flashcard_id & dictionary_id
        ComboBox flashDropDown = (ComboBox) scene.lookup("#flashDrpDwn");
        Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
        int flashId = selected.getFID();
        int dictId = selected.getDict();

        // Get Stage info and set destination Scene
        Parent dest = FXMLLoader.load(getClass().getResource("create_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        window.setScene(destScene);

        // Set front and back of flashcard
        TextArea frontArea = (TextArea) destScene.lookup("#frontArea");
        frontArea.setText(selected.frontProperty().getValue());
        TextArea backArea = (TextArea) destScene.lookup("#backArea");
        backArea.setText(selected.backProperty().getValue());

        // Lookup in destination scene for labels and save params
        Label flashLabel = (Label) destScene.lookup("#flashId");
        flashLabel.setText(flashId + "");
        Label dictLabel = (Label) destScene.lookup("#dictId");
        dictLabel.setText(dictId + "");
        Label opLabel = (Label) destScene.lookup("#flashOp");
        opLabel.setText("alter");

        // Finally display window
        window.show();
    }

    // Adds functionality to "Save" button in create_flashcard page
    public void saveFlash(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        int dictId = 0;
        int flashId = 0;

        // Look for operation label, get operation
        Label opLabel = (Label) scene.lookup("#flashOp");
        String operation = opLabel.getText();

        if (operation.equals("new")) {
            // Get dictionary_id of flashcard to save
            Label dictLabel = (Label) scene.lookup("#dictId");
            dictId = Integer.parseInt(dictLabel.getText());

            // Get front and back of flashcard
            TextArea frontArea = (TextArea) scene.lookup("#frontArea");
            String frontContent = frontArea.getText();
            TextArea backArea = (TextArea) scene.lookup("#backArea");
            String backContent = backArea.getText();

            // Save flashcard
            locDB.saveFlashcard(dictId, frontContent, backContent);
        }


        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Overwriting Flashcard");
            alert.setHeaderText("Trying to overwrite a flashcard...");
            alert.setContentText("Are you sure you want to overwrite this flashcard? You will not be able to recover its previous content");
            Optional<ButtonType> result = alert.showAndWait();

            // Alter flashcard in case user clicks "OK"
            if (result.get() == ButtonType.OK) {
                // Get flashcard_id and dictionary_id of flashcard to save
                Label flashLabel = (Label) scene.lookup("#flashId");
                flashId = Integer.parseInt(flashLabel.getText());
                Label dictLabel = (Label) scene.lookup("#dictId");
                dictId = Integer.parseInt(dictLabel.getText());

                // Get front and back of flashcard
                TextArea frontArea = (TextArea) scene.lookup("#frontArea");
                String frontContent = frontArea.getText();
                TextArea backArea = (TextArea) scene.lookup("#backArea");
                String backContent = backArea.getText();

                // Alter flashcard
                locDB.updateFlashcard(flashId, frontContent, backContent);
            }
        }

        // Return to view_flashcard page
        returnToFlashcard(event, dictId, flashId);
    }

    // Returns to flashcard page after user creates/edits flashcard
    public void returnToFlashcard(ActionEvent event, int dictId, int flashId) throws IOException {
        // Get Stage info and set destination Scene
        Parent dest = FXMLLoader.load(getClass().getResource("view_flashcard.fxml"));
        Scene destScene = new Scene(dest);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);

        // Lookup in the Scene for ComboBox, fetch items from local DB and add them
        ComboBox dictDropDown = (ComboBox) destScene.lookup("#dictDrpDwn");
        List<Dictionary> dictionaries = locDB.allDictionaries();
        ObservableList<Dictionary> observableDicts = FXCollections.observableList(dictionaries);
        dictDropDown.setItems(observableDicts);

        // Updates item in ComboBox to show only their title instead of their full instance
        Callback<ListView<Dictionary>, ListCell<Dictionary>> factory = lv -> new ListCell<Dictionary>() {

            @Override
            protected void updateItem(Dictionary item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getTitle());
            }

        };

        dictDropDown.setCellFactory(factory);
        dictDropDown.setButtonCell(factory.call(null));

        // Find position of dictionary to select
        int selectDictPos = 0;

        for (Dictionary dict : dictionaries) {
            if (dict.getDict() == dictId)
                break;
            else
                selectDictPos++;
        }

        // Select dictionary corresponding to passed param
        dictDropDown.getSelectionModel().select(selectDictPos);

        // Lookup in the Scene for flashcard ComboBox, fetch items from local DB
        ComboBox flashDropDown = (ComboBox) destScene.lookup("#flashDrpDwn");
        List<Flashcard> flashcards = locDB.allFlashcards(dictId);

        ObservableList<Flashcard> observableFlashs = FXCollections.observableList(flashcards);
        flashDropDown.setItems(observableFlashs);
        flashDropDown.setVisible(true);

        // Updates item in ComboBox to show only their title instead of their full instance
        Callback<ListView<Flashcard>, ListCell<Flashcard>> flashFactory = lv -> new ListCell<Flashcard>() {

            @Override
            protected void updateItem(Flashcard item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.frontProperty().getValue());
            }

        };

        flashDropDown.setCellFactory(flashFactory);
        flashDropDown.setButtonCell(flashFactory.call(null));

        // If new flashcard as param, select last item in flashcard drop down
        if (flashId == 0) {
            flashDropDown.getSelectionModel().selectLast();
        }

        // Select param flashcard
        else {
            // Find position of flashcard to select
            int selectFlashPos = 0;

            for (Flashcard flash : flashcards) {
                if (flash.getFID() == flashId)
                    break;
                else
                    selectFlashPos++;
            }

            // Select dictionary corresponding to passed param
            flashDropDown.getSelectionModel().select(selectFlashPos);
        }

        // Lookup in Scene for buttons to interact with flashcard, make visible
        Button editFlash = (Button) destScene.lookup("#editFlash");
        editFlash.setVisible(true);
        Button deleteFlash = (Button) destScene.lookup("#deleteFlash");
        deleteFlash.setVisible(true);

        // Set displayed flashcard to altered/new flashcard
        Flashcard selected = (Flashcard) flashDropDown.getSelectionModel().getSelectedItem();
        TextArea flashContent = (TextArea) destScene.lookup("#flashContent");
        flashContent.setText(selected.frontProperty().getValue());

        // Lookup in Scene for "Flip" button, make enabled
        Button flipFlash = (Button) destScene.lookup("#flipFlash");
        flipFlash.setDisable(false);

        // Make "Prev" button enabled if not first flashcard
        if (flashDropDown.getSelectionModel().getSelectedIndex() != 0) {
            Button prevFlash = (Button) destScene.lookup("#prevFlash");
            prevFlash.setDisable(false);
        } else {
            Button prevFlash = (Button) destScene.lookup("#prevFlash");
            prevFlash.setDisable(true);
        }

        // Make "Next" button enabled if not last flashcard
        if (flashDropDown.getSelectionModel().getSelectedIndex() != flashDropDown.getItems().size() - 1) {
            Button nextFlash = (Button) destScene.lookup("#nextFlash");
            nextFlash.setDisable(false);
        } else {
            Button nextFlash = (Button) destScene.lookup("#nextFlash");
            nextFlash.setDisable(true);
        }

        //Finally show window
        window.show();
    }

    // Called to prepare Dictionary page
    public void goDictionary(ActionEvent event) throws IOException{
        // Get Stage info and set destination Scene
        Parent dest = FXMLLoader.load(getClass().getResource("view_dictionary.fxml"));
        Scene destScene = new Scene(dest);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);

        // Lookup in the Scene for ComboBox, fetch items from local DB and add them
        ComboBox dictDropDown = (ComboBox) destScene.lookup("#dictDrpDwn");
        List<Dictionary> dictionaries = locDB.allDictionaries();
        ObservableList<Dictionary> observableDicts = FXCollections.observableList(dictionaries);
        dictDropDown.setItems(observableDicts);

        // Updates item in ComboBox to show only their title instead of their full instance
        Callback<ListView<Dictionary>, ListCell<Dictionary>> factory = lv -> new ListCell<Dictionary>() {

            @Override
            protected void updateItem(Dictionary item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getTitle());
            }

        };

        dictDropDown.setCellFactory(factory);
        dictDropDown.setButtonCell(factory.call(null));

        // Finally display window
        window.show();
    }

    // Columns for table in dictionary page (need to be global objects)
    @FXML TableColumn<Flashcard, String> frontCol;
    @FXML TableColumn<Flashcard, String> backCol;

    // Displays selected Dictionary in Table of the dictionary page
    public void displayDict(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for ComboBox, get selected item
        ComboBox dictDropDown = (ComboBox) scene.lookup("#dictDrpDwn");
        Dictionary selected = (Dictionary) dictDropDown.getSelectionModel().getSelectedItem();

        //Fetch all flashcards contained in user selected dictionary
        List<Flashcard> flashcards = locDB.allFlashcards(selected.getDict());
        ObservableList<Flashcard> observableFlashcards = FXCollections.observableList(flashcards);

        // Lookup in Scene for TableView and set property value factory for the columns
        TableView<Flashcard> dictTable = (TableView<Flashcard>) scene.lookup("#dictTable");
        frontCol.setCellValueFactory(new PropertyValueFactory<Flashcard, String>("front"));
        backCol.setCellValueFactory(new PropertyValueFactory<Flashcard, String>("back"));

        // Finally add items
        dictTable.setItems(observableFlashcards);

        // Set buttons to alter and eliminate dict to visible
        Button updateDict = (Button) scene.lookup("#updateDict");
        updateDict.setVisible(true);
        Button deleteDict = (Button) scene.lookup("#deleteDict");
        deleteDict.setVisible(true);
    }

    // Deletes selected Dictionary from local db
    public void deleteDict(ActionEvent event) throws IOException{
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for ComboBox, get selected item
        ComboBox dictDropDown = (ComboBox) scene.lookup("#dictDrpDwn");
        Dictionary selected = (Dictionary) dictDropDown.getSelectionModel().getSelectedItem();

        // Try to delete dictionary if user selected one
        if (selected != null) {
            // Create and show deletion alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Dictionary Deletion");
            alert.setHeaderText("Trying to delete a dictionary...");
            alert.setContentText("Are you sure you want to delete this dictionary? You will not be able to recover its flashcards in case of deletion");
            Optional<ButtonType> result = alert.showAndWait();

            // Delete dictionary in case user clicks "OK", reload window
            if (result.get() == ButtonType.OK) {
                locDB.deleteDictionary(selected.getDict());
                goDictionary(event);
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

    // Updates record of user select note in local db
    public void updateDict(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Lookup in the Scene for ComboBox, get selected item's dictionary_id
        ComboBox dictDropDown = (ComboBox) scene.lookup("#dictDrpDwn");
        Dictionary selected = (Dictionary) dictDropDown.getSelectionModel().getSelectedItem();
        int dictId = selected.getDict();

        // Create and show prompt to rename dictionary
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Dictionary");
        dialog.setHeaderText("Trying to rename " + selected.getTitle() + "...");
        dialog.setContentText("Please enter new name for the dictionary:");
        Optional<String> result = dialog.showAndWait();

        // Rename dictionary if user input new name and reload window
        if (result.isPresent()){
            locDB.updateDictionary(dictId, result.get());
            goDictionary(event);
        }
    }

    // Saves new dictionary in local DB
    public void saveDict(ActionEvent event) throws IOException {
        // Get Stage and Scene info
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        // Create and show prompt to name new dictionary
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Dictionary");
        dialog.setHeaderText("Trying to create new dictionary...");
        dialog.setContentText("Please enter a name for the new dictionary:");
        Optional<String> result = dialog.showAndWait();

        // Rename dictionary if user input new name and reload window
        if (result.isPresent()){
            locDB.saveDictionary(result.get());

            // Create and show alert to add flashcards to new dictionary
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Dictionary Created!");
            alert.setHeaderText("Your new dictionary "+ result.get() +" has been created!");
            alert.setContentText("Congratulations, you created a new dictionary! Do you want to add flashcards to it?");
            Optional<ButtonType> resultButton = alert.showAndWait();

            // Go to flashcards page if user confirms
            if (resultButton.get() == ButtonType.OK) {
                goFlashcard(event);
            }

            // Reload page if user decides to remain
            else {
                goDictionary(event);
            }
        }
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
