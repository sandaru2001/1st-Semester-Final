package lk.ijse.hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.hardware.Util.Regex;
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.tm.CustomerTM;
import lk.ijse.hardware.model.CustomerModel;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageCustomerController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    @FXML
    public TextField txtCustid;

    @FXML
    public TextField txtCustname;

    @FXML
    public TextField txtCustaddress;

    @FXML
    public TextField txtCustnic;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colNic;

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
    }

    void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("CustId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("CustName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("CustAddress"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("CustNic"));
    }

    void getAll() {
        try {
            ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
            List<Customer> cusList = CustomerModel.getAll();

            for(Customer customer : cusList) {
                obList.add(new CustomerTM(
                        customer.getCustId(),
                        customer.getCustName(),
                        customer.getCustAddress(),
                        customer.getCustNic()
                ));
            }
            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent)  {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM customer WHERE Customer_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtCustid.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
        clearAll();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String id = txtCustid.getText();
        String name = txtCustname.getText();
        String address = txtCustaddress.getText();
        String nic = txtCustnic.getText();
        boolean isValidCustId= Regex.isValidCustId(txtCustid.getText());
        boolean isValidNic= Regex.isValidNic(txtCustnic.getText());
        try(Connection con = DriverManager.getConnection(URL,props)) {
            if (!isValidCustId) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Id is not Valid!").show();
                txtCustid.setText(null);
            } else {
                if (!isValidNic) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer NIC is not Valid!").show();
                    txtCustnic.setText(null);
                }else {
                    String sql = "INSERT INTO customer(Customer_Id, Customer_Name, Address, NIC) VALUES(?,?,?,?)";

                    PreparedStatement pstm = con.prepareStatement(sql);
                    pstm.setString(1, id);
                    pstm.setString(2, name);
                    pstm.setString(3, address);
                    pstm.setString(4, nic);

                    int affectrdRows = pstm.executeUpdate();
                    if (affectrdRows > 0) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Customer Added!!");
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Error !!");
                    }
                    getAll();
                    clearAll();
                }
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String id = txtCustid.getText();
        String name = txtCustname.getText();
        String address = txtCustaddress.getText();
        String nic = txtCustnic.getText();

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE customer SET Customer_Name = ?,Address = ?,NIC = ? WHERE Customer_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setString(2, address);
            pstm.setString(3, nic);
            pstm.setString(4, id);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated!!").show();
            }
        }
        getAll();
        clearAll();
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/DashBoardFoam.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("DashBoard");
        stage.centerOnScreen();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void codeSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtCustid.getText();

        try(Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM Customer WHERE id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()) {
                String Id = txtCustid.getText();
                String name = txtCustname.getText();
                String address = txtCustaddress.getText();
                String nic = txtCustnic.getText();

                txtCustid.setText(Id);
                txtCustname.setText(name);
                txtCustaddress.setText(address);
                txtCustnic.setText(nic);

            }
        }

    }

    public void tblCustomerOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblCustomer.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtCustid.setText(colId.getCellData(index).toString());
        txtCustname.setText(colName.getCellData(index).toString());
        txtCustaddress.setText(colAddress.getCellData(index).toString());
        txtCustnic.setText(colNic.getCellData(index).toString());
    }

    public void clearAll(){
        txtCustid.setText(null);
        txtCustname.setText(null);
        txtCustaddress.setText(null);
        txtCustnic.setText(null);
    }
}
