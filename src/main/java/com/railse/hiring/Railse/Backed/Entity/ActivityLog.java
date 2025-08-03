package com.railse.hiring.Railse.Backed.Entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ActivityLog {
    private String description;
    private LocalDateTime timestamp;

    // constructor, getters, setters
}

