package lk.ijse.hardware.dto.tm;

import lk.ijse.hardware.dto.SupDetails;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class SupDetailsTM {
    private String SupId;
    private String ItemCode;
    private String Qty;
    private String Date;

}
