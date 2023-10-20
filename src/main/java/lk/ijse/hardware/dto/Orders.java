package lk.ijse.hardware.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Orders {
    private String OrderId;
    private String OrderDate;
    private String OrderTime;
    private String CustId;
    private String DeliveryId;
}
