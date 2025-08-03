package com.railse.hiring.Railse.Backed.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railse.hiring.Railse.Backed.common.model.enums.ReferenceType;
import com.railse.hiring.Railse.Backed.common.model.enums.Task;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskManagement {

    private Long id;
    private Long referenceId;

    private LocalDate startDate;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId;

   // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime taskDeadlineTime; // ✅ correct type

    private TaskPriority taskPriority;


}
