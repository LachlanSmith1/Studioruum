package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Studioruum");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}