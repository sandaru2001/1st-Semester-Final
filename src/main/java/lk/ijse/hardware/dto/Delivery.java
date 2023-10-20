package lk.ijse.hardware.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Delivery {
    private String DelivId;
    private String DelivDate;
    private String DelivLocation;
    private String DelivEmpId;
    private String DelivVehiNo;
    private String DelivDriverId;

}
