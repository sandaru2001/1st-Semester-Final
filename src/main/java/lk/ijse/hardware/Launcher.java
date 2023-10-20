package lk.ijse.hardware;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Scene scene=new Scene(FXMLLoader.load(getClass().getResource("/view/LogInForm.fxml")));
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
