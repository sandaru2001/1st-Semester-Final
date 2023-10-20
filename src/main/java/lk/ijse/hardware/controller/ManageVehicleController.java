package lk.ijse.hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.Item;
import lk.ijse.hardware.dto.Supplier;
import lk.ijse.hardware.dto.Vehicle;
import lk.ijse.hardware.dto.tm.VehicleTM;
import lk.ijse.hardware.model.CustomerModel;
import lk.ijse.hardware.model.ItemModel;
import lk.ijse.hardware.model.VehicleModel;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageVehicleController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    @FXML
    private TextField txtVehicleno;

    @FXML
    private TextField txtVehicletype;

    @FXML
    private TextField txtDescription;

    @FXML
    private TableView<VehicleTM> tblVehicle;

    @FXML
    private TableColumn<?, ?> colVehino;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colVehiType;


    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
    }

    void setCellValueFactory() {
        colVehino.setCellValueFactory(new PropertyValueFactory<>("VehiNo"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colVehiType.setCellValueFactory(new PropertyValueFactory<>("VehiType"));
    }

    void getAll() {
        try {
            ObservableList<VehicleTM> obList = FXCollections.observableArrayList();
            List<Vehicle> vehicleList = VehicleModel.getAll();

            for(Vehicle vehicle : vehicleList) {
                obList.add(new VehicleTM(
                        vehicle.getVehiNo(),
                        vehicle.getVehiType(),
                        vehicle.getDescription()
                ));
            }
            tblVehicle.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String vehino = txtVehicleno.getText();
        String vehitype = txtVehicletype.getText();
        String desc = txtDescription.getText();


        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE vehicle SET Vehicle_Type = ?,Description = ?WHERE Vehicle_No = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, vehitype);
            pstm.setString(2, desc);
            pstm.setString(3, vehino);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Vehicle Updated!!").show();
            }
        }
        getAll();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM vehicle WHERE Vehicle_No = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtVehicleno.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String vehino = txtVehicleno.getText();
        String vehitype = txtVehicletype.getText();
        String desc = txtDescription.getText();


        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "INSERT INTO vehicle(Vehicle_No ,Vehicle_Type ,Description) VALUES(?, ?, ?)";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, vehino);
            pstm.setString(2, vehitype);
            pstm.setString(3, desc);

            int affectedRows = pstm.executeUpdate();
            if(affectedRows > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Vehicle Added!!").show();
            }
        }
        getAll();
    }

    public void codeSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtVehicleno.getText();

        try(Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM Vehicle WHERE id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()) {
                String vehino = txtVehicleno.getText();
                String vehitype = txtVehicletype.getText();
                String desc = txtDescription.getText();

                txtVehicleno.setText(vehino);
                txtVehicletype.setText(vehitype);
                txtDescription.setText(desc);

            }
        }
    }

    public void tblVehicleOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblVehicle.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtVehicleno.setText(colVehino.getCellData(index).toString());
        txtVehicletype.setText(colVehiType.getCellData(index).toString());
        txtDescription.setText(colDesc.getCellData(index).toString());
    }
}
