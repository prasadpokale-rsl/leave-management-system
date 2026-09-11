package com.example.Laeave_Management_Api.DTO;

import com.example.Laeave_Management_Api.Model.LeaveType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeLeaveRequest{

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date should not be in past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Size(max = 500, message = "Reason cannot exceed 500 character")
    private String reason;
}
