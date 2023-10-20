package lk.ijse.hardware.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.hardware.Util.Regex;
import lk.ijse.hardware.dto.Item;
import lk.ijse.hardware.dto.tm.ItemTM;
import lk.ijse.hardware.model.ItemModel;
import lk.ijse.hardware.model.WareHouseModel;
import lombok.SneakyThrows;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

;

public class ManageItemController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }


    @FXML
    private TextField txtItemcode;

    @FXML
    private TextField txtItemqtyonhand;

    @FXML
    private TextField txtunitprice;

    @FXML
    private ComboBox<String> comItemwarehouseid;

    @FXML
    private TextField txtdescription;

    @FXML
    private JFXButton btnItemsave;

    @FXML
    private JFXButton btnItemdelete;

    @FXML
    private JFXButton btnItemupdate;

    @FXML
    private JFXButton btnItemclear;

    @FXML
    private JFXButton btnItemback;

    @FXML
    private TableView<ItemTM> tblItem;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colcode;

    @FXML
    private TableColumn<?, ?> colprice;

    @FXML
    private TableColumn<?, ?> colWarehouseId;

    @SneakyThrows
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
        loadWarehouseID();
    }

    void setCellValueFactory() {
        colcode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("QtyOnHand"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        colWarehouseId.setCellValueFactory(new PropertyValueFactory<>("Wareid"));
    }

    void getAll() throws SQLException {
        try {
            ObservableList<ItemTM> obList = FXCollections.observableArrayList();
            List<Item> itemList = ItemModel.getAll();

            for (Item item : itemList) {
                obList.add(new ItemTM(
                        item.getItemCode(),
                        item.getDescription(),
                        item.getQtyOnHand(),
                        item.getUnitPrice(),
                        item.getWareid()
                ));
            }
            tblItem.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void loadWarehouseID() {
        try {
            List<String> id = WareHouseModel.getProductID();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id) {
                obList.add(un);
            }
            comItemwarehouseid.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        boolean isValidItemCode= Regex.isValidItemCode(txtItemcode.getText());
        String code = txtItemcode.getText();
        String desc = txtdescription.getText();
        int qty = Integer.parseInt(txtItemqtyonhand.getText());
        double price = Double.parseDouble(txtunitprice.getText());
        String wareid = String.valueOf(comItemwarehouseid.getValue());


        try(Connection con = DriverManager.getConnection(URL,props)) {
            if (!isValidItemCode) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item Code is not Valid!").show();
                txtItemcode.setText(null);
            } else {
                String sql = "INSERT INTO item(Item_Code, Description, Qty_On_Hand, Uni_Price, Warehouse_Id) VALUES(?,?,?,?,?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, code);
                pstm.setString(2, desc);
                pstm.setInt(3, qty);
                pstm.setDouble(4, price);
                pstm.setString(5, wareid);

                int affectrdRows = pstm.executeUpdate();
                if (affectrdRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Item Added!!");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error !!");
                }
                clearAll();

                getAll();
            }
        }
    }

    @SneakyThrows
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM item WHERE Item_Code = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtItemcode.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {

    }

    public void btnBackOnAction(ActionEvent actionEvent) {

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String code = txtItemcode.getText();
        String desc = txtdescription.getText();
        int qty = Integer.parseInt(txtItemqtyonhand.getText());
        double price = Double.parseDouble(txtunitprice.getText());
        String wareid = String.valueOf(comItemwarehouseid.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE item SET Description = ?, Qty_On_Hand = ?, Uni_Price =?, Warehouse_Id = ? WHERE Item_Code = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, desc);
            pstm.setString(2, String.valueOf(qty));
            pstm.setString(3, String.valueOf(price));
            pstm.setString(4, wareid);
            pstm.setString(5, code);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item Updated!!").show();
            }
        }
        getAll();
    }

    public void codeSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtItemcode.getText();

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM Item WHERE Item_Code = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String code = txtItemcode.getText();
                String desc = txtdescription.getText();
                int qty = Integer.parseInt(txtItemqtyonhand.getText());
                double price = Double.parseDouble(txtunitprice.getText());
                String wareid = String.valueOf(comItemwarehouseid.getValue());

                txtItemcode.setText(code);
                txtdescription.setText(desc);
                txtItemqtyonhand.setText(String.valueOf(qty));
                txtunitprice.setText(String.valueOf(price));
                comItemwarehouseid.setValue(wareid);
            }
        }
    }

    public void tblItemOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblItem.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtItemcode.setText(colcode.getCellData(index).toString());
        txtdescription.setText(colDesc.getCellData(index).toString());
        txtItemqtyonhand.setText(colQty.getCellData(index).toString());
        txtunitprice.setText(colprice.getCellData(index).toString());
        comItemwarehouseid.setValue(colWarehouseId.getCellData(index).toString());
    }

    public void clearAll(){
        txtItemcode.setText(null);
        txtdescription.setText(null);
        txtItemqtyonhand.setText(null);
        txtunitprice.setText(null);
    }
}



