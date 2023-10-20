package lk.ijse.hardware.model;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import lk.ijse.hardware.Util.CrudUtil;
import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    public static List<Customer> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Customer";

        List<Customer> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));

        }
        return data;
    }

    public static Customer getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Customer_Id FROM Customer";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
           ids.add(resultSet.getString(1));
        }
        return (Customer) ids;
    }

    public static Customer searchById(String cusId) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Customer WHERE Customer_Id = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, cusId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }

    public static boolean save(Customer customer) throws SQLException {
        String query = "INSERT INTO customer VALUES (?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,customer.getCustId());
        pstm.setString(2,customer.getCustName());
        pstm.setString(3,customer.getCustAddress());
        pstm.setString(4,customer.getCustNic());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String query = "DELETE FROM customer WHERE Customer_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Customer customer) throws SQLException {
        String query = "UPDATE customer SET Customer_Name = ?, Address = ?,NIC = ? WHERE Customer_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);

        pstm.setString(1, customer.getCustName());
        pstm.setString(2, customer.getCustAddress());
        pstm.setString(3, customer.getCustNic());
        pstm.setString(4, customer.getCustId());
        return pstm.executeUpdate() > 0;
    }

    public static List<String> getCustomerId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Customer_Id FROM customer";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }
}
