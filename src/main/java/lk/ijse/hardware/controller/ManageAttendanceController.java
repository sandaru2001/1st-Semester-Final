package lk.ijse.hardware.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lk.ijse.hardware.Util.Regex;
import lk.ijse.hardware.Util.TextFilds;
import lk.ijse.hardware.dto.Attendance;
import lk.ijse.hardware.dto.tm.AttendanceTM;
import lk.ijse.hardware.model.AttendanceModel;
import lk.ijse.hardware.model.EmployeeModel;
import lombok.SneakyThrows;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageAttendanceController implements Initializable {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TextField txtAttendID;
    public TextField txtAttendstatus;

    @FXML
    private ComboBox<String> comEmpid;



    @FXML
    private DatePicker txtAttenddate;

    @FXML
    StackPane Dasshboard;

    @FXML
    AnchorPane AnchorPane;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<AttendanceTM> tblAttendance;

    @FXML
    private TableColumn<?, ?> colAttendID;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colDate;



    @SneakyThrows
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAll();
        loadEmployeeId();
    }

    private void loadEmployeeId() {
        try{
            List<String> id = EmployeeModel.getEmployeeId();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : id){
                obList.add(un);
            }
            comEmpid.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }

    void setCellValueFactory(){
        colAttendID.setCellValueFactory(new PropertyValueFactory<>("AttendId"));
        colId.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("EmpDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("EmpStatus"));
    }

    void getAll() throws SQLException {
        try{
            ObservableList<AttendanceTM> obList = FXCollections.observableArrayList();
            List<Attendance> attendanceList = AttendanceModel.getAll();

            for ( Attendance attendance: attendanceList){
                obList.add(new AttendanceTM(
                        attendance.getAttendId(),
                        attendance.getEmpId(),
                        attendance.getEmpStatus(),
                        attendance.getEmpDate()
                ));
            }

            tblAttendance.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"Please Check TextFilds !").show();
            return;
        }

        String Aid = txtAttendID.getText();
        String id = String.valueOf(comEmpid.getValue());
        String status = txtAttendstatus.getText();
        String date = String.valueOf(txtAttenddate.getValue());

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "UPDATE attendence SET Employee_Id = ?, Status = ?,Date =? WHERE AttendanceID = ?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, id);
            pstm.setString(2, status);
            pstm.setString(3, date);
            pstm.setString(4, Aid);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Attendence Updated!!").show();
            }
        }
        getAll();
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM attendence WHERE AttendanceID = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, txtAttendID.getText());
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        getAll();
    }

    @FXML
    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"Please Check TextFilds !").show();
            return;
        }

        String Aid = txtAttendID.getText();
        String id = String.valueOf(comEmpid.getValue());
        String status = txtAttendstatus.getText();
        String date = String.valueOf(txtAttenddate.getValue());
        boolean isValidAttendanceId=Regex.isValidAtteId(txtAttendID.getText());
        try(Connection con = DriverManager.getConnection(URL,props)) {
            if (!isValidAttendanceId) {
                new Alert(Alert.AlertType.CONFIRMATION, "Attendance Id is not Valid!").show();
                txtAttendID.setText(null);
            } else {
                String sql = "INSERT INTO attendence(AttendanceID,Employee_Id, Status, Date) VALUES(?,?,?,?)";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, Aid);
                pstm.setString(2, id);
                pstm.setString(3, status);
                pstm.setString(4, date);

                int affectrdRows = pstm.executeUpdate();
                if (affectrdRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Attendance Added!!");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error !!");
                }
            }
            getAll();
            clearAll();
        }
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
//        Dasshboard.getChildren().removeAll();
        AnchorPane.setVisible(true);
        Dasshboard.setVisible(false);
    }

    @FXML
    public void btnClearOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void codeSearchOnAction(ActionEvent actionEvent) throws SQLException {

    }

    public void setDashboard(StackPane dashboard, AnchorPane dash) {//S A
        this.Dasshboard=dashboard;
        this.AnchorPane = dash;
    }

    public void AtendenceOnAction(MouseEvent mouseEvent) {
        Integer index = tblAttendance.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtAttendID.setText(colAttendID.getCellData(index).toString());
        comEmpid.setValue(colId.getCellData(index).toString());
        txtAttendstatus.setText(colStatus.getCellData(index).toString());
        txtAttenddate.setValue(LocalDate.parse(colDate.getCellData(index).toString()));
    }

    public void txtAttendIDOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextFilds.NAME, (JFXTextField) txtAttendID);
    }

    public void txtAttendstatusOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextFilds.ADDRESS, (JFXTextField) txtAttendstatus);
    }

    public boolean isValidated(){
        if (!Regex.setTextColor(TextFilds.NAME, (JFXTextField) txtAttendID))return false;
        if (!Regex.setTextColor(TextFilds.ADDRESS, (JFXTextField) txtAttendstatus))return false;
        return true;
    }

    public void clearAll(){
        txtAttendID.setText(null);
        txtAttendstatus.setText(null);
        txtAttenddate.setValue(null);
    }

}
