package com.railse.hiring.Railse.Backed.dto;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.common.model.enums.ReferenceType;
import com.railse.hiring.Railse.Backed.common.model.enums.Task;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data

public class TaskCreateRequest {
    private List<RequestItem> requests= new ArrayList<>();;

    @Data
    public static class RequestItem {
        private Long referenceId;
        private ReferenceType referenceType;
        private Task task;
        private Long assigneeId;
        private TaskPriority taskPriority;
        private LocalDateTime taskDeadlineTime;
        private List<TaskManagementDto> requests;
    }
}




