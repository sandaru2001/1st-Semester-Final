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
import lk.ijse.hardware.dto.SupDetails;
import lk.ijse.hardware.dto.tm.SupDetailsTM;
import lk.ijse.hardware.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageSupDetailsController implements Initializable {

    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    @FXML
    private AnchorPane root;

    @FXML
    private ComboBox<String> comSupplierid;

    @FXML
    private ComboBox<String> comItemcode;

    @FXML
    private TextField txtSupquantity;

    @FXML
    private DatePicker txtSupDate;

    @FXML
    private TableView<SupDetailsTM> tblSupDetails;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colItemcode;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colDate;

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
        loadSupplierId();
        loadItemCode();
    }

    private void loadSupplierId() {
        try{
            List<String> id = SupplierModel.getSupplierId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comSupplierid.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    private void loadItemCode() {
        try{
            List<String> id = ItemModel.getItemCode();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comItemcode.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("SupId"));
        colItemcode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }



    private void getAll() {
        try{
            ObservableList<SupDetailsTM> obList = FXCollections.observableArrayList();
            List<SupDetails> supDetailsList = SupDetailsModel.getAll();

            for ( SupDetails supDetails: supDetailsList){
                obList.add(new SupDetailsTM(
                        supDetails.getSupId(),
                        supDetails.getItemCode(),
                        supDetails.getQty(),
                        supDetails.getDate()
                ));
            }

            tblSupDetails.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String id = String.valueOf(comSupplierid.getValue());
        String code = String.valueOf(comItemcode.getValue());
        String qty = txtSupquantity.getText();
        String date = String.valueOf(txtSupDate.getValue());

        try(Connection con = DriverManager.getConnection(URL,props)){
            String sql = "INSERT INTO supplier_details(Supplier_Id, Item_Code, Quantity, Date) VALUES(?,?,?,?)";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1,id);
            pstm.setString(2,code);
            pstm.setString(3,qty);
            pstm.setString(4,date);

            int affectrdRows = pstm.executeUpdate();
            if (affectrdRows > 0){
                new Alert(Alert.AlertType.CONFIRMATION ,"SupDetails Added!!");
            }else {
                new Alert(Alert.AlertType.ERROR ,"Error !!");
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
                String sql = "DELETE FROM supplier_details WHERE Supplier_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, comSupplierid.getValue());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = String.valueOf(comSupplierid.getValue());
        String code = String.valueOf(comItemcode.getValue());
        String qty = txtSupquantity.getText();
        String date = String.valueOf(txtSupDate.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE supplier_details SET Item_Code = ?, Quantity = ?,Date =? WHERE Supplier_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, code);
            pstm.setString(2, qty);
            pstm.setString(3, date);
            pstm.setString(4, id);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "SupDetails Updated!!").show();
            }
        }
        getAll();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void tblSupDetailsOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblSupDetails.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }

        comSupplierid.setValue(colId.getCellData(index).toString());
        comItemcode.setValue(colItemcode.getCellData(index).toString());
        txtSupquantity.setText(colQuantity.getCellData(index).toString());
        txtSupDate.setValue(LocalDate.parse(colDate.getCellData(index).toString()));
    }
}
