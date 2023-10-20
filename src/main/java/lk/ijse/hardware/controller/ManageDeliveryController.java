package lk.ijse.hardware.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.hardware.Util.Regex;
import lk.ijse.hardware.dto.Delivery;
import lk.ijse.hardware.dto.tm.DeliveryTM;
import lk.ijse.hardware.model.*;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ManageDeliveryController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }



    @FXML
    private TextField txtdelivid;

    @FXML
    private TextField txtlocation;

    @FXML
    private DatePicker txtDeliDate;

    @FXML
    private ComboBox<String> comDelivVehino;

    @FXML
    private ComboBox <String> comdelivempid;

    @FXML
    private ComboBox <String> comDriverid;

    @FXML
    private JFXButton btnOrdersave;

    @FXML
    private JFXButton btnOrderdelete;

    @FXML
    private JFXButton btnOrderupdate;

    @FXML
    private JFXButton btnOrderclear;

    @FXML
    private JFXButton btnItemback;

    @FXML
    private TableView<DeliveryTM> tblDelivery;

    @FXML
    private TableColumn<?, ?> colDelivid;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colEmployeeid;

    @FXML
    private TableColumn<?, ?> colVehiid;

    @FXML
    private TableColumn<?, ?> colDriverid;

    @SneakyThrows
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
        loadEmployeeId();
        loadVehicleId();
        loadDriverId();
    }

    private void loadEmployeeId() {
        try{
            List<String> id = EmployeeModel.getEmployeeId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comdelivempid.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    private void loadVehicleId() {
        try{
            List<String> id = VehicleModel.getVehicleNo();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comDelivVehino.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    private void loadDriverId() {
        try{
            List<String> id = DriverModel.getDriverId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comDriverid.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }


    void setCellValueFactory() {
        colDelivid.setCellValueFactory(new PropertyValueFactory<>("DelivId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("DelivDate"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("DelivLocation"));
        colEmployeeid.setCellValueFactory(new PropertyValueFactory<>("DelivEmpId"));
        colVehiid.setCellValueFactory(new PropertyValueFactory<>("DelivVehiNo"));
        colDriverid.setCellValueFactory(new PropertyValueFactory<>("DelivDriverId"));
    }

    void getAll() {
        try {
            ObservableList<DeliveryTM> obList = FXCollections.observableArrayList();
            List<Delivery> deliveryList = DeliveryModel.getAll();

            for(Delivery delivery : deliveryList) {
                obList.add(new DeliveryTM(
                        delivery.getDelivId(),
                        delivery.getDelivDate(),
                        delivery.getDelivLocation(),
                        delivery.getDelivEmpId(),
                        delivery.getDelivVehiNo(),
                        delivery.getDelivDriverId()
                ));
            }
            tblDelivery.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }


    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String delivid = txtdelivid.getText();
        String date = String.valueOf(txtDeliDate.getValue());
        String location = txtlocation.getText();
        String empid = String.valueOf(comdelivempid.getValue());
        String vehiNo = String.valueOf(comDelivVehino.getValue());
        String drivid = String.valueOf(comDriverid.getValue());
        boolean isValidDeliveryId = Regex.isValidDeliveryId(txtdelivid.getText());

        try(Connection con = DriverManager.getConnection(URL,props)) {
            if (!isValidDeliveryId) {
                new Alert(Alert.AlertType.CONFIRMATION, "Delivery Id is not Valid!").show();
                txtdelivid.setText(null);
            } else {
                String sql = "INSERT INTO delivery(Delivery_Id, Date, Location, Employee_Id, Vehicle_No, Driver_Id) VALUES(?,?,?,?,?,?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, delivid);
                pstm.setString(2, date);
                pstm.setString(3, location);
                pstm.setString(4, empid);
                pstm.setString(5, vehiNo);
                pstm.setString(6, drivid);

                int affectrdRows = pstm.executeUpdate();
                if (affectrdRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Delivery Added!!");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error !!");
                }
            }
            getAll();
            clearAll();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM delivery WHERE Delivery_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtdelivid.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String delivid = txtdelivid.getText();
        String date = String.valueOf(txtDeliDate.getValue());
        String location = txtlocation.getText();
        String empid = String.valueOf(comdelivempid.getValue());
        String vehiid = String.valueOf(comDelivVehino.getValue());
        String drivid = String.valueOf(comDriverid.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE delivery SET Date = ?, Location = ?, Employee_Id =?, Vehicle_No =?, Driver_Id =? WHERE Delivery_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, date);
            pstm.setString(2, location);
            pstm.setString(3, empid);
            pstm.setString(4, vehiid);
            pstm.setString(5, drivid);
            pstm.setString(6, delivid);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Delivery Updated!!").show();
            }
        }
        getAll();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {

    }

    public void btnBackOnAction(ActionEvent actionEvent) {

    }

    public void codeSearchOnAction(ActionEvent actionEvent) {

    }


    public void tblDeliveryOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblDelivery.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtdelivid.setText(colDelivid.getCellData(index).toString());
        txtDeliDate.setValue(LocalDate.parse(colDate.getCellData(index).toString()));
        txtlocation.setText(colLocation.getCellData(index).toString());
        comdelivempid.setValue(colEmployeeid.getCellData(index).toString());
        comDelivVehino.setValue(colVehiid.getCellData(index).toString());
        comDriverid.setValue(colDriverid.getCellData(index).toString());
    }
    public void clearAll(){
        txtdelivid.setText(null);
        txtDeliDate.setValue(null);
        txtlocation.setText(null);
    }
}
