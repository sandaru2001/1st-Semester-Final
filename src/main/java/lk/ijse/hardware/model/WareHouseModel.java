package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.WareHouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WareHouseModel {
    public static List<WareHouse> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Warehouse";

        List<WareHouse> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new WareHouse(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)

            ));

        }
        return data;
    }

    public static WareHouse getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM Warehouse";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return (WareHouse) ids;
    }

    public static boolean save(WareHouse wareHouse) throws SQLException {
        String query = "INSERT INTO warehouse VALUES (?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, wareHouse.getWareId());
        pstm.setString(2, wareHouse.getWareName());
        pstm.setString(3, wareHouse.getSupId());
        pstm.setString(4, wareHouse.getWareQty());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String query = "DELETE FROM warehouse WHERE Warehouse_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean update(WareHouse wareHouse) throws SQLException {
        String query = "UPDATE customer SET WareHouse_Id = ?, WHERE WareHouse_Name = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, wareHouse.getWareId());
        pstm.setString(2, wareHouse.getWareName());
        pstm.setString(3, wareHouse.getSupId());
        pstm.setString(4, wareHouse.getWareQty());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getProductID() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Warehouse_Id FROM warehouse";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }
}
