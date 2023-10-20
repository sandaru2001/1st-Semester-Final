package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.CartDTO;
import lk.ijse.hardware.dto.Customer;
import lk.ijse.hardware.dto.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    public static List<Item> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Item";

        List<Item> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            ));

        }
        return data;
    }
    public static Item getIds() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT code FROM Item";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return (Item) ids;
    }

    public static boolean delete(String id) throws SQLException {
        String query = "DELETE FROM item WHERE Item_Code = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1,id);
        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Item item) throws SQLException {
        String query = "UPDATE item SET Item_Code = ?, Description = ?,Qty_On_Hand = ?,Uni_Price = ? WHERE Warehouse_Id = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);

        pstm.setString(1, item.getItemCode());
        pstm.setString(2, item.getDescription());
        pstm.setString(3, String.valueOf(item.getQtyOnHand()));
        pstm.setString(4, String.valueOf(item.getUnitPrice()));
        pstm.setString(5, item.getDescription());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getItemCode() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT Item_Code FROM item";

        List<String> ids = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

    public static Item searchById(String code) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM item WHERE Item_Code = ?";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, code);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    public static boolean updateQty(List<CartDTO> cartDTOList) throws SQLException {
        for (CartDTO dto : cartDTOList) {
            if(!updateQty(dto)) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(CartDTO dto) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "UPDATE Item SET Qty_On_Hand = (Qty_On_Hand - ?) WHERE Item_Code = ?";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setInt(1, dto.getQty());
        pstm.setString(2, dto.getCode());

        return pstm.executeUpdate() > 0;
    }
}
