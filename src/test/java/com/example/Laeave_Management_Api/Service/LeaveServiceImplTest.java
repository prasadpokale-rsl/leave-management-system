package com.example.Laeave_Management_Api.Service;

import com.example.Laeave_Management_Api.DTO.EmployeeLeaveRequest;
import com.example.Laeave_Management_Api.DTO.EmployeeLeaveResponse;
import com.example.Laeave_Management_Api.DTO.UpdateEmployeeLeaveRequest;
import com.example.Laeave_Management_Api.Exception.LeaveNotFoundException;
import com.example.Laeave_Management_Api.Model.EmployeeLeave;
import com.example.Laeave_Management_Api.Model.LeaveStatus;
import com.example.Laeave_Management_Api.Model.LeaveType;
import com.example.Laeave_Management_Api.Repository.LeaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    private EmployeeLeave employeeLeave;

    @BeforeEach
    void setUp() {
        employeeLeave = EmployeeLeave.builder()
                .id(1L)
                .employeeID(101L)
                .leaveType("PAID")
                .startDate(LocalDate.of(2026, 9, 15))
                .endDate(LocalDate.of(2026, 9, 17))
                .reason("Personal work")
                .status(LeaveStatus.APPLIED)
                .build();
    }

    @Test
    void shouldGetAllLeaves() {

        when(leaveRepository.get())
                .thenReturn(List.of(employeeLeave));

        List<EmployeeLeaveResponse> response =
                leaveService.getAllLeaves();

        assertNotNull(response);
        assertEquals(1, response.size());

        EmployeeLeaveResponse leaveResponse = response.get(0);

        assertEquals(1L, leaveResponse.getId());
        assertEquals(101L, leaveResponse.getEmployeeId());
        assertEquals(LeaveType.PAID, leaveResponse.getLeaveType());
        assertEquals(
                LocalDate.of(2026, 9, 15),
                leaveResponse.getStartDate()
        );
        assertEquals(
                LocalDate.of(2026, 9, 17),
                leaveResponse.getEndDate()
        );
        assertEquals("Personal work", leaveResponse.getReason());
        assertEquals(LeaveStatus.APPLIED, leaveResponse.getStatus());

        verify(leaveRepository).get();
    }

    @Test
    void shouldGetEmployeeLeaveById() {

        when(leaveRepository.getByID(1L))
                .thenReturn(employeeLeave);

        EmployeeLeaveResponse response =
                leaveService.getEmployeeLeaveById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(101L, response.getEmployeeId());
        assertEquals(LeaveType.PAID, response.getLeaveType());
        assertEquals(LeaveStatus.APPLIED, response.getStatus());

        verify(leaveRepository).getByID(1L);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeLeaveNotFound() {

        when(leaveRepository.getByID(99L))
                .thenReturn(null);

        assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.getEmployeeLeaveById(99L)
        );

        verify(leaveRepository).getByID(99L);
    }

    @Test
    void shouldCreateEmployeeLeave() {

        EmployeeLeaveRequest request =
                EmployeeLeaveRequest.builder()
                        .employeeId(101L)
                        .leaveType(LeaveType.PAID)
                        .startDate(LocalDate.of(2026, 9, 15))
                        .endDate(LocalDate.of(2026, 9, 17))
                        .reason("Personal work")
                        .build();

        when(leaveRepository.save(any(EmployeeLeave.class)))
                .thenAnswer(invocation -> {

                    EmployeeLeave leave =
                            invocation.getArgument(0);

                    leave.setId(1L);

                    return leave;
                });

        EmployeeLeaveResponse response =
                leaveService.createEmployeeLeave(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(101L, response.getEmployeeId());
        assertEquals(LeaveType.PAID, response.getLeaveType());
        assertEquals(
                LocalDate.of(2026, 9, 15),
                response.getStartDate()
        );
        assertEquals(
                LocalDate.of(2026, 9, 17),
                response.getEndDate()
        );
        assertEquals("Personal work", response.getReason());
        assertEquals(LeaveStatus.APPLIED, response.getStatus());

        verify(leaveRepository).save(any(EmployeeLeave.class));
    }

    @Test
    void shouldRejectLeaveWhenEndDateIsBeforeStartDate() {

        EmployeeLeaveRequest request =
                EmployeeLeaveRequest.builder()
                        .employeeId(101L)
                        .leaveType(LeaveType.PAID)
                        .startDate(LocalDate.of(2026, 9, 20))
                        .endDate(LocalDate.of(2026, 9, 15))
                        .reason("Invalid dates")
                        .build();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> leaveService.createEmployeeLeave(request)
                );

        assertEquals(
                "End date cannot be before start date",
                exception.getMessage()
        );

        verify(leaveRepository, never())
                .save(any(EmployeeLeave.class));
    }

    @Test
    void shouldUpdateEmployeeLeave() {

        UpdateEmployeeLeaveRequest request =
                UpdateEmployeeLeaveRequest.builder()
                        .leaveType(LeaveType.CASUAL)
                        .startDate(LocalDate.of(2026, 9, 20))
                        .endDate(LocalDate.of(2026, 9, 22))
                        .reason("Updated reason")
                        .build();

        when(leaveRepository.getByID(1L))
                .thenReturn(employeeLeave);

        when(leaveRepository.save(any(EmployeeLeave.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        EmployeeLeaveResponse response =
                leaveService.updateEmployeeLeave(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(101L, response.getEmployeeId());
        assertEquals(LeaveType.CASUAL, response.getLeaveType());
        assertEquals(
                LocalDate.of(2026, 9, 20),
                response.getStartDate()
        );
        assertEquals(
                LocalDate.of(2026, 9, 22),
                response.getEndDate()
        );
        assertEquals("Updated reason", response.getReason());

        verify(leaveRepository).getByID(1L);
        verify(leaveRepository).save(employeeLeave);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingLeave() {

        UpdateEmployeeLeaveRequest request =
                UpdateEmployeeLeaveRequest.builder()
                        .leaveType(LeaveType.CASUAL)
                        .startDate(LocalDate.of(2026, 9, 20))
                        .endDate(LocalDate.of(2026, 9, 22))
                        .reason("Updated reason")
                        .build();

        when(leaveRepository.getByID(99L))
                .thenReturn(null);

        assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.updateEmployeeLeave(99L, request)
        );

        verify(leaveRepository).getByID(99L);

        verify(
                leaveRepository,
                never()
        ).save(any(EmployeeLeave.class));
    }

    @Test
    void shouldRejectUpdateWhenEndDateIsBeforeStartDate() {

        UpdateEmployeeLeaveRequest request =
                UpdateEmployeeLeaveRequest.builder()
                        .leaveType(LeaveType.CASUAL)
                        .startDate(LocalDate.of(2026, 9, 20))
                        .endDate(LocalDate.of(2026, 9, 15))
                        .reason("Invalid dates")
                        .build();

        when(leaveRepository.getByID(1L))
                .thenReturn(employeeLeave);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> leaveService.updateEmployeeLeave(1L, request)
                );

        assertEquals(
                "End date cannot be before start date",
                exception.getMessage()
        );

        verify(leaveRepository).getByID(1L);

        verify(
                leaveRepository,
                never()
        ).save(any(EmployeeLeave.class));
    }

    @Test
    void shouldDeleteEmployeeLeave() {

        when(leaveRepository.getByID(1L))
                .thenReturn(employeeLeave);

        leaveService.deleteEmployeeLeave(1L);

        verify(leaveRepository).getByID(1L);
        verify(leaveRepository).deleteByID(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingLeave() {

        when(leaveRepository.getByID(99L))
                .thenReturn(null);

        assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.deleteEmployeeLeave(99L)
        );

        verify(leaveRepository).getByID(99L);

        verify(
                leaveRepository,
                never()
        ).deleteByID(anyLong());
    }
}

