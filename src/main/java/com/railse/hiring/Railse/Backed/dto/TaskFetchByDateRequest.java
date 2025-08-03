package com.railse.hiring.Railse.Backed.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskFetchByDateRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> assigneeIds;
}

