package lk.ijse.hardware.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInFormController {

    public AnchorPane root;
    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {


        if ((txtUsername.getText().equalsIgnoreCase("Admin") & txtPassword.getText().equalsIgnoreCase("1234"))){
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/DashBoardFoam.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            root.getScene().getWindow().hide();

//            Stage stage=new Stage();
//            Scene scene=new Scene(FXMLLoader.load(getClass().getResource("/view/DashBoardFoam.fxml")));
//            stage.setScene(scene);
//            stage.show();
//            root.getScene().getWindow().hide();
        }
    }
}
