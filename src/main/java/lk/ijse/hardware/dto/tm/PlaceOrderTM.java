package lk.ijse.hardware.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class PlaceOrderTM {
    private String code;
    private String description;
    private Integer qty;
    private double unitPrice;
    private double total;
    private Button btn;
}
