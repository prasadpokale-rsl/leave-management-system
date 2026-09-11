package com.example.Laeave_Management_Api.Service;

import com.example.Laeave_Management_Api.DTO.EmployeeLeaveRequest;
import com.example.Laeave_Management_Api.DTO.EmployeeLeaveResponse;
import com.example.Laeave_Management_Api.DTO.UpdateEmployeeLeaveRequest;

import java.util.List;

public interface LeaveService {

    List<EmployeeLeaveResponse> getAllLeaves();

    EmployeeLeaveResponse getEmployeeLeaveById(Long id);

    EmployeeLeaveResponse createEmployeeLeave(EmployeeLeaveRequest request);

    EmployeeLeaveResponse updateEmployeeLeave(
            Long id,
            UpdateEmployeeLeaveRequest request
    );

    void deleteEmployeeLeave(Long id);
}
