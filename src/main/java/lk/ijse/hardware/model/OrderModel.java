package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Orders;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    public static List<Orders> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Orders";

        List<Orders> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Orders(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)

            ));

        }
        return data;
    }
    public static Orders getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM Orders";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return (Orders) ids;
    }

    public static boolean save(Orders orders) throws SQLException {
        String query = "INSERT INTO orders VALUES (?,?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, orders.getOrderId());
        pstm.setString(2, orders.getOrderDate());
        pstm.setString(3, orders.getOrderTime());
        pstm.setString(4,orders.getCustId());
        pstm.setString(5,orders.getDeliveryId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String query = "DELETE FROM orders WHERE Order_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,id);
        return pstm.executeUpdate() > 0;
    }


    public static boolean update(Orders orders) throws SQLException {
        String query = "UPDATE orders SET Order_Id = ?, Customer_Id = ?,Date = ?,Time = ? WHERE Delivery_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, orders.getOrderId());
        pstm.setString(2, orders.getCustId());
        pstm.setString(3, orders.getOrderDate());
        pstm.setString(4, orders.getOrderTime());
        pstm.setString(5, orders.getDeliveryId());

        return pstm.executeUpdate() > 0;
    }

    public static String generateNextOrderId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT Order_Id FROM Orders ORDER BY Order_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if(resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("O00");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "O00"+id;
        }
        return "O001";
    }

    public static boolean save(String orderId, String cusId, LocalDate date) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO Orders(Order_Id, Date, Customer_Id) VALUES (?, ?, ?)";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, orderId);
        pstm.setString(2, String.valueOf(date));
        pstm.setString(3, cusId);

        return pstm.executeUpdate() > 0;
    }
}
