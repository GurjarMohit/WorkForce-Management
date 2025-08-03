package com.railse.hiring.Railse.Backed.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskDetailsDto {
    private TaskManagementDto task;
    private List<String> activityHistory;
    private List<String> comments;
}
