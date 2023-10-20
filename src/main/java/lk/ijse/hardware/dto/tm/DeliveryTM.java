package lk.ijse.hardware.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class DeliveryTM {
    private String DelivId;
    private String DelivDate;
    private String DelivLocation;
    private String DelivEmpId;
    private String DelivVehiNo;
    private String DelivDriverId;

}
