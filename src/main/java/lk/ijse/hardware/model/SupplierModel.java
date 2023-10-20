package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public static List<Supplier> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Supplier";

        List<Supplier> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)

            ));

        }
        return data;
    }
    public static Supplier getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM Supplier";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return (Supplier) ids;
    }

    public static boolean save(Supplier supplier) throws SQLException {
        String query = "INSERT INTO supplier VALUES (?,?,?,?,?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, supplier.getSupId());
        pstm.setString(2, supplier.getSupName());
        pstm.setString(3, supplier.getSupComName());
        pstm.setString(4, supplier.getSupNic());
        pstm.setString(5, supplier.getSupContact());

        return pstm.executeUpdate() > 0;
    }
    public static boolean delete(String id) throws SQLException {
        String query = "DELETE FROM supplier WHERE Supplier_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Supplier supplier) throws SQLException {
        String query = "UPDATE supplier SET Supplier_Id = ?, Supplier_Name = ?,Company_Name = ?,NIC = ?,Contact = ? WHERE Supplier_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, supplier.getSupId());
        pstm.setString(2, supplier.getSupName());
        pstm.setString(3, supplier.getSupComName());
        pstm.setString(4, supplier.getSupNic());
        pstm.setString(5, supplier.getSupContact());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getSupplierId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Supplier_Id FROM supplier";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }
}
