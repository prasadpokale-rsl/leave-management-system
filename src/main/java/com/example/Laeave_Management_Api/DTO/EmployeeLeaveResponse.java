package com.example.Laeave_Management_Api.DTO;

import com.example.Laeave_Management_Api.Model.LeaveStatus;
import com.example.Laeave_Management_Api.Model.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveResponse {
    private Long id;

    private Long employeeId;

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private LeaveStatus status;
}
