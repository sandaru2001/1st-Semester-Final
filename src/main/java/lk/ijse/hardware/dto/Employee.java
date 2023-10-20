package lk.ijse.hardware.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Employee {
    private String EmpId;
    private String EmpName;
    private double EmpSalary;
    private int EmpContact;
    private String EmpWareid;
}
