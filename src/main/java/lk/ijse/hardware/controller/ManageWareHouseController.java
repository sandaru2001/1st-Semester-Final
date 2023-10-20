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
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.Item;
import lk.ijse.hardware.dto.Vehicle;
import lk.ijse.hardware.dto.WareHouse;
import lk.ijse.hardware.dto.tm.WareHouseTM;
import lk.ijse.hardware.model.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageWareHouseController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }


    @FXML
    private TextField txtWareid;

    @FXML
    private TextField txtWarename;

    @FXML
    private ComboBox<String> comSupid;

    @FXML
    private TextField txtWareQty;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<WareHouseTM> tblWarehouse;

    @FXML
    private TableColumn<?, ?> colWareid;

    @FXML
    private TableColumn<?, ?> colWarename;

    @FXML
    private TableColumn<?, ?> colWaresupid;

    @FXML
    private TableColumn<?, ?> colWareqty;


    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
        loadWarehouseID();
    }

    private void loadWarehouseID() {
        try{
            List<String> id = SupplierModel.getSupplierId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comSupid.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    void setCellValueFactory() {
        colWareid.setCellValueFactory(new PropertyValueFactory<>("WareId"));
        colWarename.setCellValueFactory(new PropertyValueFactory<>("WareName"));
        colWareqty.setCellValueFactory(new PropertyValueFactory<>("WareQty"));
        colWaresupid.setCellValueFactory(new PropertyValueFactory<>("SupId"));
    }

    void getAll() {
        try {
            ObservableList<WareHouseTM> obList = FXCollections.observableArrayList();
            List<WareHouse> wareHouseList = WareHouseModel.getAll();

            for(WareHouse wareHouse : wareHouseList) {
                obList.add(new WareHouseTM(
                        wareHouse.getWareId(),
                        wareHouse.getWareName(),
                        wareHouse.getWareQty(),
                        wareHouse.getSupId()
                ));
            }
            tblWarehouse.setItems(obList);
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
        String id = txtWareid.getText();
        String name = txtWarename.getText();
        String qty = txtWareQty.getText();
        String supid = String.valueOf(comSupid.getValue());


        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE warehouse SET Warehouse_Name = ?, Quntity =?, Supplier_Id = ? WHERE Warehouse_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setString(2, qty);
            pstm.setString(3, supid);
            pstm.setString(4, id);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "WareHouse Updated!!").show();
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
                String sql = "DELETE FROM warehouse WHERE Warehouse_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtWareid.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        boolean isValidWareHouseId= Regex.isValidWhId(txtWareid.getText());
        String id = txtWareid.getText();
        String name = txtWarename.getText();
        String qty = txtWareQty.getText();
        String supid = String.valueOf(comSupid.getValue());


        try (Connection con = DriverManager.getConnection(URL, props)) {
            if (!isValidWareHouseId) {
                new Alert(Alert.AlertType.CONFIRMATION, "WareHouse Id is not Valid!").show();
                txtWareid.setText(null);
            } else {
                String sql = "INSERT INTO warehouse(Warehouse_Id ,Warehouse_Name ,Quntity ,Supplier_Id) VALUES(?, ?, ?, ?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, id);
                pstm.setString(2, name);
                pstm.setString(3, qty);
                pstm.setString(4, supid);

                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "WareHouse Added!!").show();
                }
                getAll();
                clearAll();
            }
        }
    }

    public void codeSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtWareid.getText();

        try(Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM WareHouse WHERE id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()) {
                String Id = txtWareid.getText();
                String name = txtWarename.getText();
                String qty = txtWareQty.getText();
                String supid = String.valueOf(comSupid.getValue());

                txtWareid.setText(Id);
                txtWarename.setText(name);
                txtWareQty.setText(qty);
                comSupid.setValue(supid);

            }
        }
    }

    public void tblWarehouseOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblWarehouse.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtWareid.setText(colWareid.getCellData(index).toString());
        txtWarename.setText(colWarename.getCellData(index).toString());
        txtWareQty.setText(colWareqty.getCellData(index).toString());
        comSupid.setValue(colWaresupid.getCellData(index).toString());
    }

    public void clearAll(){
        txtWareid.setText(null);
        txtWarename.setText(null);
        txtWareQty.setText(null);
    }
}
