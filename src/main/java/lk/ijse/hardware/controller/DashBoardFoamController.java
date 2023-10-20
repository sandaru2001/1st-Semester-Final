package lk.ijse.hardware.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.SneakyThrows;


import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class DashBoardFoamController implements Initializable {

    @FXML
    private AnchorPane LoadContext;

    @FXML
    private JFXButton btnHome;

    @FXML
    private AnchorPane home;

    @FXML
    private StackPane ControllArea;

    @FXML
    private JFXButton btnManagesupllier;

    @FXML
    public Label lblTime;

    @FXML
    public Label lblDate;

    @FXML
    public AnchorPane dashboard;

    @FXML
    public AnchorPane Dash;

    @FXML
    public JFXButton btnManagewarehouse;

    @FXML
    public JFXButton btnManageorder;

    @FXML
    public JFXButton btnManagevehicle;

    @FXML
    public JFXButton btnManageattendance;

    @FXML
    public JFXButton btnManageDelivery;

    @FXML
    public JFXButton btnManageSupplierdetails;

    @FXML
    public JFXButton btnManagedriver;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnManageitem;

    @FXML
    private JFXButton btnManageemployee;

    @FXML
    private JFXButton btnManagecustomer;

    @SneakyThrows


    @FXML
    void btnDashBoardOnAction(ActionEvent event) throws IOException {
        Dash.setVisible(true);
        ControllArea.setVisible(false);
    }

    @FXML
    void btnManageCustomerOnAction(ActionEvent event) throws IOException {
//        Node node = FXMLLoader.load(getClass().getResource("/view/ManageCustomer.fxml"));
//        home.getChildren().clear();
//        home.getChildren().addAll(node);
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageCustomer.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);

    }

    @FXML
    void btnManageEmployeeOnAction(ActionEvent event) throws IOException {
//        Node node=FXMLLoader.load(getClass().getResource("/view/ManageEmployee.fxml"));
//        home.getChildren().clear();
//        home.getChildren().addAll(node);
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageEmployee.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    void btnManageItemOnAction(ActionEvent event) throws IOException {
//        Node node=FXMLLoader.load(getClass().getResource("/view/ManageItem.fxml"));
//        home.getChildren().clear();
//        home.getChildren().addAll(node);
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageItem.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);

    }


    @FXML
    void btnManageOrderOnAction(ActionEvent actionEvent) throws IOException {
//        Node node=FXMLLoader.load(getClass().getResource("/view/ManageOrder.fxml"));
//        home.getChildren().clear();
//        home.getChildren().addAll(node);
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageOrder.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnManageSupplierOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageSupplier.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnManageWarehouseOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageWareHouse.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnManageAttendanceOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/ManageAttendance.fxml"));
        AnchorPane anchorPane = loader.load();
        ManageAttendanceController controller = loader.getController();
        controller.setDashboard(ControllArea,Dash);
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(anchorPane);
    }

    @FXML
    public void btnManageVehicleOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageVehicle.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnManageDeliveryOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageDelivery.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnManageSupDetailsOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageSupDetails.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnManageDriverOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageDriver.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        Dash.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageOrderDetails.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    private void TimeNow(){
        Thread thread = new Thread(() ->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while (!false){
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println(e);
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() ->{
                    lblTime.setText(timenow);
                });
            }
        });
        thread.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblDate.setText(String.valueOf(LocalDate.now()));
        TimeNow();
    }
}
