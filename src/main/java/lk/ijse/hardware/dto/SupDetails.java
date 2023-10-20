package lk.ijse.hardware.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class SupDetails {
    private String SupId;
    private String ItemCode;
    private String Qty;
    private String Date;
}
