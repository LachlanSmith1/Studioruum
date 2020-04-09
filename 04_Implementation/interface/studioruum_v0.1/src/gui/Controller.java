package gui;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Controller {
    
    public String accountType="";

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
