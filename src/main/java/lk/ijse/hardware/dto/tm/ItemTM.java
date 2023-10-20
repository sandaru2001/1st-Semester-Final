package lk.ijse.hardware.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class ItemTM {
    private String ItemCode;
    private String Description;
    private int QtyOnHand;
    private double UnitPrice;
    private String Wareid;
}
