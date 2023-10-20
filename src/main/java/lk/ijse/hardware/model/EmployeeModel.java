package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public static List<Employee> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employee";

        List<Employee> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getString(5)
            ));

        }
        return data;
    }

    public static boolean save(Employee employee) throws SQLException {
        String query = "INSERT INTO employee VALUES (?,?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,employee.getEmpId());
        pstm.setString(2,employee.getEmpName());
        pstm.setString(3, String.valueOf(employee.getEmpSalary()));
        pstm.setString(4, String.valueOf(employee.getEmpContact()));
        pstm.setString(5,employee.getEmpWareid());

        return pstm.executeUpdate() > 0;
    }

    public static Employee getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM Employee";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return (Employee) ids;
    }

    public static boolean update(Employee employee) throws SQLException {
        String query = "UPDATE employee SET Employee_Id = ?,Employee_Name = ?, Salary = ?,Contact = ? WHERE Warehouse_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);

        pstm.setString(1,employee.getEmpId());
        pstm.setString(2,employee.getEmpName());
        pstm.setString(3, String.valueOf(employee.getEmpSalary()));
        pstm.setString(4, String.valueOf(employee.getEmpContact()));
        pstm.setString(5,employee.getEmpWareid());
        return pstm.executeUpdate() > 0;
    }

    public static List<String> getEmployeeId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Employee_Id FROM employee";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

}
