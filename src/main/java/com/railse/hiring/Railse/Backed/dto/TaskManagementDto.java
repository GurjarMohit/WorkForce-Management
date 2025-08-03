package com.railse.hiring.Railse.Backed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.common.model.enums.ReferenceType;
import com.railse.hiring.Railse.Backed.common.model.enums.Task;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    //private String description;
    private TaskStatus status;
    private Long assigneeId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime taskDeadlineTime;
    private TaskPriority taskPriority;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
}


