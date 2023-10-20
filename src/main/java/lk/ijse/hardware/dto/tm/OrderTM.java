package lk.ijse.hardware.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class OrderTM {
    private String OrderId;
    private String OrderDate;
    private String OrderTime;
    private String CustId;
    private String DeliveryId;
}
