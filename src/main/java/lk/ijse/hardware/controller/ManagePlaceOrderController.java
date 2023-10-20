package lk.ijse.hardware.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.CartDTO;
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.Item;
import lk.ijse.hardware.dto.tm.PlaceOrderTM;
import lk.ijse.hardware.model.CustomerModel;
import lk.ijse.hardware.model.ItemModel;
import lk.ijse.hardware.model.OrderModel;
import lk.ijse.hardware.model.PlaceOrderModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ManagePlaceOrderController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblNetTotal;

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private Label lblOrderId;

    @FXML
    private TextField txtQty;
    private ObservableList<PlaceOrderTM> obList = FXCollections.observableArrayList();

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private TableView<PlaceOrderTM> tblOrderCart;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblUnitPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        setOrderDate();
        loadCustomerIds();
        loadItemCodes();
        generateNextOrderId();
    }

    private void generateNextOrderId() {
        try {
            String nextId = OrderModel.generateNextOrderId();
            lblOrderId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemCodes() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> codes = ItemModel.getItemCode();

            for (String code : codes) {
                obList.add(code);
            }
            cmbItemCode.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void loadCustomerIds() {
        try {
            List<String> ids = CustomerModel.getCustomerId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String id : ids) {
                obList.add(id);
            }
            cmbCustomerId.setItems(obList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void setOrderDate() {
        lblOrderDate.setText(String.valueOf(LocalDate.now()));
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String code = String.valueOf(cmbItemCode.getValue());
        String description = lblDescription.getText();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double total = qty * unitPrice;
        Button btnRemove = new Button("Remove");
        btnRemove.setCursor(Cursor.HAND);

        setRemoveBtnOnAction(btnRemove); /* set action to the btnRemove */

        if (!obList.isEmpty()) {
            for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
                if (colItemCode.getCellData(i).equals(code)) {
                    qty += (int) colQty.getCellData(i);
                    total = qty * unitPrice;

                    obList.get(i).setQty(qty);
                    obList.get(i).setTotal(total);

                    tblOrderCart.refresh();
                    calculateNetTotal();
                    return;
                }
            }
        }

        PlaceOrderTM tm = new PlaceOrderTM(code, description, qty, unitPrice, total, btnRemove);

        obList.add(tm);
        tblOrderCart.setItems(obList);
        calculateNetTotal();

        txtQty.setText("");
    }

    private void calculateNetTotal() {
        double netTotal = 0.0;
        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            double total  = (double) colTotal.getCellData(i);
            netTotal += total;
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }

    private void setRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblOrderCart.getSelectionModel().getSelectedIndex();
                obList.remove(index);

                tblOrderCart.refresh();
                calculateNetTotal();
            }

        });
    }

    void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/DashBoardFoam.fxml"));
        Stage stage = (Stage) pane.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/ManageCustomer.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Customer Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

//    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
//
//    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        String oId = lblOrderId.getText();
        String cusId = String.valueOf(cmbCustomerId.getValue());

        List<CartDTO> cartDTOList = new ArrayList<>();

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            PlaceOrderTM tm = obList.get(i);

            CartDTO cartDTO = new CartDTO(tm.getCode(), tm.getQty(), tm.getUnitPrice());
            cartDTOList.add(cartDTO);
        }

        try {
            boolean isPlaced = PlaceOrderModel.placeOrder(oId, cusId, cartDTOList);
            if(isPlaced) {
                System.out.println("Number 1 : "+isPlaced);
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order Not Placed!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }

        JasperDesign jasDesign = JRXmlLoader.load("src/main/resources/report/PlaceOrder.jrxml");
        JasperReport jasReport = JasperCompileManager.compileReport(jasDesign);
        JasperPrint jasPrint = JasperFillManager.fillReport(jasReport, null, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasPrint,false);
    }

    public void cmbItemOnAction(ActionEvent actionEvent) {
        String code = String.valueOf(cmbItemCode.getSelectionModel().getSelectedItem());

        try {
            Item item = ItemModel.searchById(code);
            fillItemFields(item);
            txtQty.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }

    }

    private void fillItemFields(Item item) {
        lblDescription.setText(item.getDescription());
        lblUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        lblQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
    }

    public void cmbCustomerOnAction(ActionEvent actionEvent) {
        String cus_id = String.valueOf(cmbCustomerId.getSelectionModel().getSelectedItem());
        try {
            Customer customer = CustomerModel.searchById(cus_id);
            lblCustomerName.setText(customer.getCustName());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    public void txtQtyOnAction(ActionEvent actionEvent) {
        btnAddToCartOnAction(actionEvent);
    }
}
