package lk.ijse.hardware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartDTO {
    private String code;
    private Integer qty;
    private double unitPrice;
}
