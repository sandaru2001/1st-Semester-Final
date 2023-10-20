package lk.ijse.hardware.dto;

import javafx.scene.control.Button;
import lombok.*;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class OrderDetails {
    private String OrderItemCode;
    private double OrderDescription;
    private int OrderQuantity;
    private String OrderUnitPrice;
    private double total;
    private Button btn;
}
