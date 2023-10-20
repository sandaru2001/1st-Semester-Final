package lk.ijse.hardware.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Attendance {
    private String AttendId;
    private String EmpId;
    private String EmpStatus;
    private String EmpDate;
}
