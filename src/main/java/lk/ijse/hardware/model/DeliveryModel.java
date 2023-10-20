package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Delivery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryModel {
    public static List<Delivery> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM delivery";

        List<Delivery> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new Delivery(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));

        }
        return data;
    }

    public static List<String> getDeliveryId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Delivery_Id FROM delivery";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }
}
