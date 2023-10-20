package lk.ijse.hardware.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AttendanceTM {
    private String AttendId;
    private String EmpId;
    private String EmpStatus;
    private String EmpDate;
}
