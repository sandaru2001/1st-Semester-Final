package lk.ijse.hardware.dto.tm;

import lk.ijse.hardware.dto.Employee;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class EmployeeTM extends Employee {
    private String EmpId;
    private String EmpName;
    private double EmpSalary;
    private int EmpContact;
    private String EmpWareid;
}
