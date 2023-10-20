package lk.ijse.hardware.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
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
import lk.ijse.hardware.dto.Orders;
import lk.ijse.hardware.dto.tm.OrderTM;
import lk.ijse.hardware.model.CustomerModel;
import lk.ijse.hardware.model.DeliveryModel;
import lk.ijse.hardware.model.OrderModel;
import lk.ijse.hardware.model.WareHouseModel;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageOrderController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }



    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtorderid;

    @FXML
    private ComboBox<String> comOrderdelivery;

    @FXML
    private ComboBox <String> comOrdercustomer;

    @FXML
    public DatePicker txtSOrderDate;

    @FXML
    private Label lblTime;

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
    private TableView<OrderTM> tblOrder;

    @FXML
    private TableColumn<?, ?> colOrderid;

    @FXML
    private TableColumn<?, ?> colCustid;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableColumn<?, ?> colDeliveryid;


    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
        loadCustomerId();
        loadDeliveryId();
        TimeNow();
    }

    private void loadCustomerId() {
        try{
            List<String> id = CustomerModel.getCustomerId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comOrdercustomer.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    private void loadDeliveryId() {
        try{
            List<String> id = DeliveryModel.getDeliveryId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comOrderdelivery.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
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

    void setCellValueFactory() {
        colOrderid.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
        colCustid.setCellValueFactory(new PropertyValueFactory<>("CustId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("OrderTime"));
        colDeliveryid.setCellValueFactory(new PropertyValueFactory<>("DeliveryId"));
    }

    void getAll() {
        try {
            ObservableList<OrderTM> obList = FXCollections.observableArrayList();
            List<Orders> ordersList = OrderModel.getAll();

            for(Orders orders : ordersList) {
                obList.add(new OrderTM(
                        orders.getOrderId(),
                        orders.getOrderDate(),
                        orders.getOrderTime(),
                        orders.getCustId(),
                        orders.getDeliveryId()
                ));
            }
            tblOrder.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }



    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        boolean isValidOrderId= Regex.isValidOrderId(txtorderid.getText());
        String id = txtorderid.getText();
        String date = String.valueOf(txtSOrderDate.getValue());
        String time = lblTime.getText();
        String custid = String.valueOf(comOrdercustomer.getValue());
        String delivid = String.valueOf(comOrderdelivery.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            if (!isValidOrderId) {
                new Alert(Alert.AlertType.CONFIRMATION,"Order Id Is Not Valid!").show();
                txtorderid.setText(null);
            } else {
                String sql = "INSERT INTO orders(Order_Id ,Date ,Time ,Customer_Id ,Delivery_Id) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, id);
                pstm.setString(2, date);
                pstm.setString(3, time);
                pstm.setString(4, custid);
                pstm.setString(5, delivid);

                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Added!!").show();
                }
                getAll();
                clearAll();
            }
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM orders WHERE Order_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtorderid.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtorderid.getText();
        String date = String.valueOf(txtSOrderDate.getValue());
        String time = lblTime.getText();
        String custid = String.valueOf(comOrdercustomer.getValue());
        String delivid = String.valueOf(comOrderdelivery.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE orders SET Date = ?,Customer_Id = ?,Delivery_Id = ? WHERE Order_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, date);
            //pstm.setString(2, time);
            pstm.setString(2, custid);
            pstm.setString(3, delivid);
            pstm.setString(4, id);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Updated!!").show();
            }
        }
        getAll();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
    }

    public void codeSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtorderid.getText();

        try(Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM Orders WHERE Order_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()) {
                String Id = txtorderid.getText();
                String date = String.valueOf(txtSOrderDate.getValue());
                String time = lblTime.getText();
                String custid = String.valueOf(comOrdercustomer.getValue());
                String delivid = String.valueOf(comOrderdelivery.getValue());

                txtorderid.setText(Id);
                txtSOrderDate.setValue(LocalDate.parse(date));
                lblTime.setText(time);
                comOrdercustomer.setValue(custid);
                comOrderdelivery.setValue(delivid);

            }
        }
    }

    public void tblOrderOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblOrder.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtorderid.setText(colOrderid.getCellData(index).toString());
        txtSOrderDate.setValue(LocalDate.parse(colDate.getCellData(index).toString()));
        lblTime.setText(colTime.getCellData(index).toString());
        comOrdercustomer.setValue(colCustid.getCellData(index).toString());
        comOrderdelivery.setValue(colDeliveryid.getCellData(index).toString());
    }

    public void  clearAll(){
        txtorderid.setText(null);

    }
}
