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
import lk.ijse.hardware.dto.Employee;
import lk.ijse.hardware.dto.tm.EmployeeTM;
import lk.ijse.hardware.model.EmployeeModel;
import lk.ijse.hardware.model.WareHouseModel;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageEmployeeController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtEmpid;

    @FXML
    private TextField txtEmpName;

    @FXML
    private TextField txtEmpsalary;

    @FXML
    private TextField txtEmpcontact;

    @FXML
    private ComboBox<String> comWarehouseID;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colContact;

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
        colId.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("EmpSalary"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("EmpContact"));
        colWarehouseId.setCellValueFactory(new PropertyValueFactory<>("EmpWareid"));
    }

    void getAll() throws SQLException {
        try {
            ObservableList<EmployeeTM> observableList = FXCollections.observableArrayList();
            List<Employee> employeeList = EmployeeModel.getAll();

            for (Employee employee : employeeList) {
                observableList.add(new EmployeeTM(
                        employee.getEmpId(),
                        employee.getEmpName(),
                        employee.getEmpSalary(),
                        employee.getEmpContact(),
                        employee.getEmpWareid()
                ));
            }

            tblEmployee.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    private void loadWarehouseID() {
        try{
            List<String> id = WareHouseModel.getProductID();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comWarehouseID.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM employee WHERE Employee_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtEmpid.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String id = txtEmpid.getText();
        String name = txtEmpName.getText();
        String salary = txtEmpsalary.getText();
        String contact = String.valueOf(txtEmpcontact.getText());
        String wareid = String.valueOf(comWarehouseID.getValue());
        boolean isValidEmpId= Regex.isValidEmpId(txtEmpid.getText());
        try (Connection con = DriverManager.getConnection(URL, props)) {
            if (!isValidEmpId) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Id is not Valid!").show();
                txtEmpid.setText(null);
            } else {
                String sql = "INSERT INTO employee(Employee_Id, Employee_Name, Salary,Contact ,WareHouse_Id) VALUES(?,?,?,?,?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, id);
                pstm.setString(2, name);
                pstm.setString(3, salary);
                pstm.setString(4, contact);
                pstm.setString(5, wareid);

                int affectrdRows = pstm.executeUpdate();
                if (affectrdRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee Added!!");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error !!");
                }
            }
            getAll();
            clearAll();
        }
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String id = txtEmpid.getText();
        String name = txtEmpName.getText();
        String salary = txtEmpsalary.getText();
        String contact = String.valueOf(txtEmpcontact.getText());
        String wareid = String.valueOf(comWarehouseID.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE employee SET Employee_Name = ?,Salary =?,Contact = ?, WareHouse_Id = ? WHERE Employee_Id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, name);
            pstm.setString(2, salary);
            pstm.setString(3, contact);
            pstm.setString(4, wareid);
            pstm.setString(5, id);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Updated!!").show();
            }
        }
        getAll();
    }

    @FXML
    void codeSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtEmpid.getText();

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM Employee WHERE id = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String Id = txtEmpid.getText();
                String name = txtEmpName.getText();
                double salary = Double.parseDouble(txtEmpsalary.getText());
                int contact = Integer.parseInt(txtEmpcontact.getText());
                String wareid = String.valueOf(comWarehouseID.getValue());

                txtEmpid.setText(Id);
                txtEmpName.setText(name);
                txtEmpsalary.setText(String.valueOf(salary));
                txtEmpcontact.setText(String.valueOf(contact));
                comWarehouseID.setValue(wareid);
            }
        }

    }

    public void tblEmployeeOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblEmployee.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtEmpid.setText(colId.getCellData(index).toString());
        txtEmpName.setText(colName.getCellData(index).toString());
        txtEmpsalary.setText(colSalary.getCellData(index).toString());
        txtEmpcontact.setText(colContact.getCellData(index).toString());
        comWarehouseID.setValue(colWarehouseId.getCellData(index).toString());
    }

    public void clearAll(){
        txtEmpid.setText(null);
        txtEmpName.setText(null);
        txtEmpsalary.setText(null);
        txtEmpcontact.setText(null);
    }
}
