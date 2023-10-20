package lk.ijse.hardware.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Item {
    private String ItemCode;
    private String Description;
    private int QtyOnHand;
    private double UnitPrice;
    private String Wareid;
}
