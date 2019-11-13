package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //Class for launching the application

    public static void main(String[] args) {
        launch(args); //launching application
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml")); //loading fxml file
        primaryStage.setTitle("IVP Solver"); //setting title of starting window
        primaryStage.setScene(new Scene(root, 1000, 500)); //setting size of starting window
        primaryStage.show(); //displaying starting window
    }

}
