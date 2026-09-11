package com.example.Laeave_Management_Api.Controller;

import com.example.Laeave_Management_Api.DTO.EmployeeLeaveResponse;
import com.example.Laeave_Management_Api.Exception.LeaveNotFoundException;
import com.example.Laeave_Management_Api.Model.LeaveStatus;
import com.example.Laeave_Management_Api.Model.LeaveType;
import com.example.Laeave_Management_Api.Service.LeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveController.class)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeaveService leaveService;

    @Test
    void shouldGetAllLeaves() throws Exception {

        EmployeeLeaveResponse response =
                EmployeeLeaveResponse.builder()
                        .id(1L)
                        .employeeId(101L)
                        .leaveType(LeaveType.PAID)
                        .startDate(LocalDate.of(2026, 9, 15))
                        .endDate(LocalDate.of(2026, 9, 17))
                        .reason("Personal work")
                        .status(LeaveStatus.APPLIED)
                        .build();

        when(leaveService.getAllLeaves())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/leaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].employeeId").value(101))
                .andExpect(jsonPath("$[0].leaveType").value("PAID"))
                .andExpect(jsonPath("$[0].startDate").value("2026-09-15"))
                .andExpect(jsonPath("$[0].endDate").value("2026-09-17"))
                .andExpect(jsonPath("$[0].reason").value("Personal work"))
                .andExpect(jsonPath("$[0].status").value("APPLIED"));
    }

    @Test
    void shouldGetEmployeeLeaveById() throws Exception {

        EmployeeLeaveResponse response =
                EmployeeLeaveResponse.builder()
                        .id(1L)
                        .employeeId(101L)
                        .leaveType(LeaveType.PAID)
                        .startDate(LocalDate.of(2026, 9, 15))
                        .endDate(LocalDate.of(2026, 9, 17))
                        .reason("Personal work")
                        .status(LeaveStatus.APPLIED)
                        .build();

        when(leaveService.getEmployeeLeaveById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/leaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value(101))
                .andExpect(jsonPath("$.leaveType").value("PAID"))
                .andExpect(jsonPath("$.status").value("APPLIED"));
    }

    @Test
    void shouldReturnNotFoundWhenLeaveDoesNotExist()
            throws Exception {

        when(leaveService.getEmployeeLeaveById(99L))
                .thenThrow(new LeaveNotFoundException(99L));

        mockMvc.perform(get("/leaves/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateEmployeeLeave() throws Exception {

        EmployeeLeaveResponse response =
                EmployeeLeaveResponse.builder()
                        .id(1L)
                        .employeeId(101L)
                        .leaveType(LeaveType.PAID)
                        .startDate(LocalDate.of(2026, 9, 15))
                        .endDate(LocalDate.of(2026, 9, 17))
                        .reason("Personal work")
                        .status(LeaveStatus.APPLIED)
                        .build();

        when(leaveService.createEmployeeLeave(
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(response);

        String requestBody = """
                {
                    "employeeId": 101,
                    "leaveType": "PAID",
                    "startDate": "2026-09-15",
                    "endDate": "2026-09-17",
                    "reason": "Personal work"
                }
                """;

        mockMvc.perform(
                        post("/leaves")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value(101))
                .andExpect(jsonPath("$.leaveType").value("PAID"))
                .andExpect(jsonPath("$.status").value("APPLIED"));
    }

    @Test
    void shouldUpdateEmployeeLeave() throws Exception {

        EmployeeLeaveResponse response =
                EmployeeLeaveResponse.builder()
                        .id(1L)
                        .employeeId(101L)
                        .leaveType(LeaveType.CASUAL)
                        .startDate(LocalDate.of(2026, 9, 20))
                        .endDate(LocalDate.of(2026, 9, 22))
                        .reason("Updated reason")
                        .status(LeaveStatus.APPLIED)
                        .build();

        when(leaveService.updateEmployeeLeave(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(response);

        String requestBody = """
                {
                    "leaveType": "CASUAL",
                    "startDate": "2026-09-20",
                    "endDate": "2026-09-22",
                    "reason": "Updated reason"
                }
                """;

        mockMvc.perform(
                        put("/leaves/1")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value(101))
                .andExpect(jsonPath("$.leaveType").value("CASUAL"))
                .andExpect(jsonPath("$.reason").value("Updated reason"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingLeave()
            throws Exception {

        when(leaveService.updateEmployeeLeave(
                org.mockito.ArgumentMatchers.eq(99L),
                org.mockito.ArgumentMatchers.any()
        )).thenThrow(new LeaveNotFoundException(99L));

        String requestBody = """
                {
                    "leaveType": "CASUAL",
                    "startDate": "2026-09-20",
                    "endDate": "2026-09-22",
                    "reason": "Updated reason"
                }
                """;

        mockMvc.perform(
                        put("/leaves/99")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEmployeeLeave() throws Exception {

        doNothing()
                .when(leaveService)
                .deleteEmployeeLeave(1L);

        mockMvc.perform(delete("/leaves/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingLeave()
            throws Exception {

        doThrow(new LeaveNotFoundException(99L))
                .when(leaveService)
                .deleteEmployeeLeave(99L);

        mockMvc.perform(delete("/leaves/99"))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnBadRequestWhenRequestValidationFails() throws Exception {

        String requestBody = """
                {
                    "employeeId": null,
                    "leaveType": null,
                    "startDate": null,
                    "endDate": null,
                    "reason": "Personal work"
                }
                """;

        mockMvc.perform(
                        post("/leaves")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.validationErrors.employeeId").value("Employee ID is required"));
    }

    @Test
    void shouldReturnBadRequestWhenDatesAreInvalid() throws Exception {

        when(leaveService.createEmployeeLeave(
                org.mockito.ArgumentMatchers.any()
        )).thenThrow(new IllegalArgumentException("End date cannot be before start date"));

        String requestBody = """
                {
                    "employeeId": 101,
                    "leaveType": "PAID",
                    "startDate": "2026-09-20",
                    "endDate": "2026-09-15",
                    "reason": "Personal work"
                }
                """;

        mockMvc.perform(
                        post("/leaves")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("End date cannot be before start date"));
    }

}
