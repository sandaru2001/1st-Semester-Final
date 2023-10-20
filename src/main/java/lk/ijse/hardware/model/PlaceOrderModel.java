package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.CartDTO;
import lk.ijse.hardware.dto.Item;
import lk.ijse.hardware.dto.OrderDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PlaceOrderModel {
    public static boolean placeOrder(String oId, String cusId, List<CartDTO> cartDTOList) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();

            con.setAutoCommit(false);

            boolean isSaved = OrderModel.save(oId, cusId, LocalDate.now());
            if (isSaved) {
                boolean isUpdated = ItemModel.updateQty(cartDTOList);
                if (isUpdated) {
                    boolean isOrderDetailSaved = OrderDetailModel.save(oId, cartDTOList);
                    if (isOrderDetailSaved) {
                        con.commit();
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            System.out.println("finally");
            con.setAutoCommit(true);
        }
    }
}
