package lk.ijse.hardware.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class CustomerTM {
    private String CustId;
    private String CustName;
    private String CustAddress;
    private String CustNic;
}
