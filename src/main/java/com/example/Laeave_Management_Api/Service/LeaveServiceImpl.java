package com.example.Laeave_Management_Api.Service;

import com.example.Laeave_Management_Api.DTO.EmployeeLeaveRequest;
import com.example.Laeave_Management_Api.DTO.EmployeeLeaveResponse;
import com.example.Laeave_Management_Api.DTO.UpdateEmployeeLeaveRequest;
import com.example.Laeave_Management_Api.Exception.LeaveNotFoundException;
import com.example.Laeave_Management_Api.Model.EmployeeLeave;
import com.example.Laeave_Management_Api.Model.LeaveStatus;
import com.example.Laeave_Management_Api.Model.LeaveType;
import com.example.Laeave_Management_Api.Repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService{

    private final LeaveRepository leaveRepository;

    @Override
    public List<EmployeeLeaveResponse> getAllLeaves() {
        return leaveRepository.get()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmployeeLeaveResponse getEmployeeLeaveById(Long id) {
        EmployeeLeave employeeLeave = leaveRepository.getByID(id);

        if (employeeLeave == null) {
            throw new LeaveNotFoundException(id);
        }

        return mapToResponse(employeeLeave);
    }

    public EmployeeLeaveResponse createEmployeeLeave(EmployeeLeaveRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }

        EmployeeLeave employeeLeave = EmployeeLeave.builder()
                .employeeID(request.getEmployeeId())
                .leaveType(String.valueOf(request.getLeaveType()))
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.APPLIED)
                .build();

        EmployeeLeave savedLeave = leaveRepository.save(employeeLeave);

        return mapToResponse(savedLeave);
    }

    @Override
    public EmployeeLeaveResponse updateEmployeeLeave(Long id, UpdateEmployeeLeaveRequest request) {
        EmployeeLeave existingLeave = leaveRepository.getByID(id);

        if (existingLeave == null) {
            throw new LeaveNotFoundException(id);
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }

        existingLeave.setLeaveType(String.valueOf(request.getLeaveType()));
        existingLeave.setStartDate(request.getStartDate());
        existingLeave.setEndDate(request.getEndDate());
        existingLeave.setReason(request.getReason());

        EmployeeLeave updatedLeave = leaveRepository.save(existingLeave);

        return mapToResponse(updatedLeave);
    }

    public void deleteEmployeeLeave(Long id) {
        EmployeeLeave existingLeave = leaveRepository.getByID(id);

        if (existingLeave == null) {
            throw new LeaveNotFoundException(id);
        }

        leaveRepository.deleteByID(id);
    }

    private EmployeeLeaveResponse mapToResponse(
            EmployeeLeave employeeLeave) {

        return EmployeeLeaveResponse.builder()
                .id(employeeLeave.getId())
                .employeeId(employeeLeave.getEmployeeID())
                .leaveType(LeaveType.valueOf(employeeLeave.getLeaveType()))
                .startDate(employeeLeave.getStartDate())
                .endDate(employeeLeave.getEndDate())
                .reason(employeeLeave.getReason())
                .status(employeeLeave.getStatus())
                .build();
    }
}
