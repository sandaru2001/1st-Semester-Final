package lk.ijse.hardware.model;

import lk.ijse.hardware.db.DBConnection;
import lk.ijse.hardware.dto.Employee;
import lk.ijse.hardware.dto.SupDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupDetailsModel {
    public static List<SupDetails> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM supplier_details";

        List<SupDetails> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()){
            data.add(new SupDetails(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));

        }
        return data;
    }
}
