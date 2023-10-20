package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.CartDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailModel {
    public static boolean save(String oId, List<CartDTO> cartDTOList) throws SQLException {
        for(CartDTO dto :  cartDTOList) {
            if(!save(oId, dto)) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(String oId, CartDTO dto) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO order_details(Order_Id, Unit_Price, Quantity, Item_Code,Total) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, oId);
        pstm.setDouble(2, dto.getUnitPrice());
        pstm.setInt(3, dto.getQty());
        pstm.setString(4, dto.getCode());
        pstm.setDouble(5,dto.getQty() * dto.getUnitPrice());

        return pstm.executeUpdate() > 0;

    }
}
