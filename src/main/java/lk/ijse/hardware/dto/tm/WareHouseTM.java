package lk.ijse.hardware.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class WareHouseTM {
    private String WareId;
    private String WareName;
    private String SupId;
    private String WareQty;
}
