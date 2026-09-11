package com.example.Laeave_Management_Api.Repository;

import com.example.Laeave_Management_Api.Model.EmployeeLeave;
import com.example.Laeave_Management_Api.Model.LeaveStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaveRepositoryTest {

    private LeaveRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LeaveRepository();
    }

    @Test
    void save_ShouldGenerateIdWhenIdIsNull() {

        EmployeeLeave leave = EmployeeLeave.builder()
                .employeeID(101L)
                .leaveType("PAID")
                .startDate(LocalDate.of(2026, 9, 15))
                .endDate(LocalDate.of(2026, 9, 17))
                .reason("Personal work")
                .status(LeaveStatus.APPLIED)
                .build();

        EmployeeLeave saved = repository.save(leave);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
        assertEquals(101L, saved.getEmployeeID());
        assertEquals("PAID", saved.getLeaveType());
    }

    @Test
    void save_ShouldPreserveIdWhenIdIsProvided() {

        EmployeeLeave leave = EmployeeLeave.builder()
                .id(100L)
                .employeeID(101L)
                .leaveType("PAID")
                .startDate(LocalDate.of(2026, 9, 15))
                .endDate(LocalDate.of(2026, 9, 17))
                .reason("Personal work")
                .status(LeaveStatus.APPLIED)
                .build();

        EmployeeLeave saved = repository.save(leave);

        assertEquals(100L, saved.getId());
    }

    @Test
    void getByID_ShouldReturnLeaveWhenExists() {

        EmployeeLeave leave = EmployeeLeave.builder()
                .employeeID(101L)
                .leaveType("PAID")
                .startDate(LocalDate.of(2026, 9, 15))
                .endDate(LocalDate.of(2026, 9, 17))
                .reason("Personal work")
                .status(LeaveStatus.APPLIED)
                .build();

        EmployeeLeave saved = repository.save(leave);

        EmployeeLeave found = repository.getByID(saved.getId());

        assertNotNull(found);
        assertEquals(101L, found.getEmployeeID());
        assertEquals("PAID", found.getLeaveType());
    }

    @Test
    void get_ShouldReturnAllLeaves() {

        repository.save(
                EmployeeLeave.builder()
                        .employeeID(101L)
                        .leaveType("PAID")
                        .startDate(LocalDate.of(2026, 9, 15))
                        .endDate(LocalDate.of(2026, 9, 17))
                        .reason("Personal work")
                        .status(LeaveStatus.APPLIED)
                        .build()
        );

        repository.save(
                EmployeeLeave.builder()
                        .employeeID(102L)
                        .leaveType("CASUAL")
                        .startDate(LocalDate.of(2026, 9, 20))
                        .endDate(LocalDate.of(2026, 9, 22))
                        .reason("Family work")
                        .status(LeaveStatus.APPLIED)
                        .build()
        );

        List<EmployeeLeave> all = repository.get();

        assertEquals(2, all.size());
    }

    @Test
    void deleteByID_ShouldRemoveLeave() {

        EmployeeLeave saved = repository.save(
                EmployeeLeave.builder()
                        .employeeID(101L)
                        .leaveType("PAID")
                        .startDate(LocalDate.of(2026, 9, 15))
                        .endDate(LocalDate.of(2026, 9, 17))
                        .reason("Personal work")
                        .status(LeaveStatus.APPLIED)
                        .build()
        );

        assertNotNull(repository.getByID(saved.getId()));

        repository.deleteByID(saved.getId());

        assertNull(repository.getByID(saved.getId()));
    }
}
