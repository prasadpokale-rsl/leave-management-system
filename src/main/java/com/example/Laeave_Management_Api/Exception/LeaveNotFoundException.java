package com.example.Laeave_Management_Api.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LeaveNotFoundException extends RuntimeException {

    public LeaveNotFoundException(Long id) {
        super("Leave not found with id: " + id);
    }
}
