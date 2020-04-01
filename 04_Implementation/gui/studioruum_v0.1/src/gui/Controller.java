package gui;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Controller {

    Boolean picked=false;

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

    public void goNotes(ActionEvent event) throws IOException{
        Parent dest = FXMLLoader.load(getClass().getResource("view_notes.fxml"));
        Scene destScene = new Scene(dest);
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(destScene);
        window.show();
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

    public void selected(ActionEvent event) throws IOException{

        Image scholarslct = new Image("@gui/scholarslct.png");
        Image scholarim = new Image("@gui/scholar.png");

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = window.getScene();

        ImageView scholar = (ImageView) scene.lookup("scholarunselect");

        if(picked==true){
            scholar.setImage(scholarim);
            picked=false;
        }
        else{
            scholar.setImage(scholarslct);
            picked=true;
        }

    }

}
