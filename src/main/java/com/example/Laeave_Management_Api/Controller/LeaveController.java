package com.example.Laeave_Management_Api.Controller;

import com.example.Laeave_Management_Api.DTO.EmployeeLeaveRequest;
import com.example.Laeave_Management_Api.DTO.EmployeeLeaveResponse;
import com.example.Laeave_Management_Api.DTO.UpdateEmployeeLeaveRequest;
import com.example.Laeave_Management_Api.Model.EmployeeLeave;
import com.example.Laeave_Management_Api.Service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping
    public ResponseEntity<List<EmployeeLeaveResponse>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeLeaveResponse> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getEmployeeLeaveById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeLeaveResponse> createLeave(@Valid @RequestBody EmployeeLeaveRequest request) {
        EmployeeLeaveResponse leaveResponse = leaveService.createLeaveLeave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeLeaveResponse> updateLeave(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeLeaveRequest request) {
        EmployeeLeaveResponse updatedEmployee = leaveService.updateLeaveLeave(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeaveLeave(id);
        return ResponseEntity.noContent().build();
    }
}
