package com.example.Laeave_Management_Api.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeave {
    private Long id;
    private Long employeeID;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
}
