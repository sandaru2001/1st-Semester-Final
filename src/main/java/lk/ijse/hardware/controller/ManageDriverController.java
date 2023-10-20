package lk.ijse.hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.hardware.Util.Regex;
import lk.ijse.hardware.dto.Driver;
import lk.ijse.hardware.dto.tm.DriverTM;
import lk.ijse.hardware.model.DriverModel;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageDriverController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtDriverid;

    @FXML
    private TextField txtDrivername;

    @FXML
    private TextField txtDriveraddress;

    @FXML
    private TextField txtDrivercontact;

    @FXML
    private TableView<DriverTM> tblDriver;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @SneakyThrows
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
    }

    void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("DrivId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("DrivName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("DrivAddress"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("DrivContact"));
    }

    void getAll() throws SQLException {
        try{
            ObservableList<DriverTM> obList = FXCollections.observableArrayList();
            List<Driver> driverList = DriverModel.getAll();

            for ( Driver driver: driverList){
                obList.add(new DriverTM(
                        driver.getDrivId(),
                        driver.getDrivName(),
                        driver.getDrivAddress(),
                        driver.getDrivContact()
                ));
            }

            tblDriver.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException{
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM driver WHERE Driver_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtDriverid.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtDriverid.getText();
        String name = txtDrivername.getText();
        String address = txtDriveraddress.getText();
        String contact = txtDrivercontact.getText();
        boolean isValidDriverId = Regex.isValidDriverId(txtDriverid.getText());
        try(Connection con = DriverManager.getConnection(URL,props)) {
            if (!isValidDriverId) {
                new Alert(Alert.AlertType.CONFIRMATION, "Driver Id is not Valid!").show();
                txtDriverid.setText(null);
            } else {
                String sql = "INSERT INTO driver(Driver_Id,Driver_Name,Address,Contact) VALUES(?,?,?,?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, id);
                pstm.setString(2, name);
                pstm.setString(3, address);
                pstm.setString(4, contact);

                int affectrdRows = pstm.executeUpdate();
                if (affectrdRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Driver Added!!");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error !!");
                }
            }
            getAll();
            clearAll();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtDriverid.getText();
        String name = txtDrivername.getText();
        String address = txtDriveraddress.getText();
        String contact = txtDrivercontact.getText();

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE driver SET Driver_Name = ?, Address = ?,Contact =? WHERE Driver_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setString(2, address);
            pstm.setString(3, contact);
            pstm.setString(4, id);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Driver Updated!!").show();
            }
        }
        getAll();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void codeSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String Id = txtDriverid.getText();

        try(Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM driver WHERE id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, Id);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()) {
                String id = txtDriverid.getText();
                String name = txtDrivername.getText();
                String address = txtDriveraddress.getText();
                String contact = txtDrivercontact.getText();

                txtDriverid.setText(id);
                txtDrivername.setText(name);
                txtDriveraddress.setText(address);
                txtDrivercontact.setText(contact);

            }
        }
    }

    public void tblDriverOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblDriver.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtDriverid.setText(colId.getCellData(index).toString());
        txtDrivername.setText(colName.getCellData(index).toString());
        txtDriveraddress.setText(colAddress.getCellData(index).toString());
        txtDrivercontact.setText(colContact.getCellData(index).toString());
    }

    public void clearAll(){
        txtDriverid.setText(null);
        txtDrivername.setText(null);
        txtDriveraddress.setText(null);
        txtDrivercontact.setText(null);
    }
}
