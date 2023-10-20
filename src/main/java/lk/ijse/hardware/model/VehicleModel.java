package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.Orders;
import lk.ijse.hardware.dto.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleModel {
    public static List<Vehicle> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Vehicle";

        List<Vehicle> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)


            ));

        }
        return data;
    }
    public static Vehicle getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM Vehicle";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return (Vehicle) ids;
    }

    public static boolean save(Vehicle vehicle) throws SQLException {
        String query = "INSERT INTO vehicle VALUES (?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, vehicle.getVehiNo());
        pstm.setString(2, vehicle.getVehiType());
        pstm.setString(3, vehicle.getDescription());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String query = "DELETE FROM vehicle WHERE Vehicle_No = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Vehicle vehicle) throws SQLException {
        String query = "UPDATE vehicle SET Vehicle_No = ?, Vehicle_Type = ? WHERE Description = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, vehicle.getVehiNo());
        pstm.setString(2, vehicle.getVehiType());
        pstm.setString(3, vehicle.getDescription());


        return pstm.executeUpdate() > 0;
    }

    public static List<String> getVehicleNo() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Vehicle_No FROM vehicle";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }
}
