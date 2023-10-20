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
import lk.ijse.hardware.dto.Supplier;
import lk.ijse.hardware.dto.tm.SupplierTM;
import lk.ijse.hardware.model.SupplierModel;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageSupplierController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }
    @FXML
    private AnchorPane SupManager;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtSupId;

    @FXML
    private TextField txtSupName;

    @FXML
    private TextField txtComname;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtContact;

    @FXML
    private TableView<SupplierTM> tblSupplier;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colComname;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colContact;


    @SneakyThrows
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
    }

    void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("SupId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("SupName"));
        colComname.setCellValueFactory(new PropertyValueFactory<>("SupComName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("SupNic"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("SupContact"));
    }

    void getAll() {
        try {
            ObservableList<SupplierTM> obList = FXCollections.observableArrayList();
            List<Supplier> supplierList = SupplierModel.getAll();

            for(Supplier supplier : supplierList) {
                obList.add(new SupplierTM(
                        supplier.getSupId(),
                        supplier.getSupName(),
                        supplier.getSupComName(),
                        supplier.getSupNic(),
                        supplier.getSupContact()
                ));
            }
            tblSupplier.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }


        @FXML
        void btnDeleteOnAction(ActionEvent event) {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try (Connection con = DriverManager.getConnection(URL, props)) {
                    String sql = "DELETE FROM supplier WHERE Supplier_Id = ?";
                    PreparedStatement pstm = con.prepareStatement(sql);
                    pstm.setString(1, txtSupId.getText());
                    pstm.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            getAll();
        }

        @FXML
        void btnSaveOnAction(ActionEvent event) throws SQLException {
            String id = txtSupId.getText();
            String name = txtSupName.getText();
            String address = txtComname.getText();
            String nic = txtNic.getText();
            String contact = txtContact.getText();
            boolean isValidSupplierId= Regex.isValidSuplId(txtSupId.getText());

            try (Connection con = DriverManager.getConnection(URL, props)) {
                if (!isValidSupplierId) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier Id is not Valid!").show();
                    txtSupId.setText(null);
                } else {
                    String sql = "INSERT INTO supplier(Supplier_Id ,Supplier_Name ,Company_Name ,NIC ,Contact) VALUES (?, ?, ?, ?,?)";

                    PreparedStatement pstm = con.prepareStatement(sql);
                    pstm.setString(1, id);
                    pstm.setString(2, name);
                    pstm.setString(3, address);
                    pstm.setString(4, nic);
                    pstm.setString(5, contact);

                    int affectedRows = pstm.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Supplier Added!!").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Error !!").show();
                    }

                    getAll();
                    clearAll();
                }
            }
        }

        @FXML
        void btnUpdateOnAction(ActionEvent event) throws SQLException {
            String id = txtSupId.getText();
            String name = txtSupName.getText();
            String address = txtComname.getText();
            String nic = txtNic.getText();
            String contact = txtContact.getText();

            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "UPDATE supplier SET Supplier_Name = ?, Company_Name =?, NIC = ?, Contact = ? WHERE Supplier_id = ?";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, name);
                pstm.setString(2, address);
                pstm.setString(3, nic);
                pstm.setString(4, contact);
                pstm.setString(5, id);

                if (pstm.executeUpdate() > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier Updated!!").show();
                }
            }
            getAll();
        }

    @FXML
    void btnBackOnAction(ActionEvent event) {

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

        @FXML
        void codeSearchOnAction(ActionEvent event) throws SQLException {
            String id = txtSupId.getText();

            try(Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "SELECT * FROM Supplier WHERE id = ?";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, id);

                ResultSet resultSet = pstm.executeQuery();

                if(resultSet.next()) {
                    String Id = txtSupId.getText();
                    String name = txtSupName.getText();
                    String comname = txtComname.getText();
                    String nic = txtNic.getText();
                    String contact = txtContact.getText();

                    txtSupId.setText(Id);
                    txtSupName.setText(name);
                    txtComname.setText(comname);
                    txtNic.setText(nic);
                    txtContact.setText(contact);

                }
            }
        }

    public void tlbSupplierOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblSupplier.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtSupId.setText(colId.getCellData(index).toString());
        txtSupName.setText(colName.getCellData(index).toString());
        txtComname.setText(colComname.getCellData(index).toString());
        txtNic.setText(colNic.getCellData(index).toString());
        txtContact.setText(colContact.getCellData(index).toString());
    }
    public void clearAll(){
        txtSupId.setText(null);
        txtSupName.setText(null);
        txtComname.setText(null);
        txtNic.setText(null);
        txtContact.setText(null);
    }
}



