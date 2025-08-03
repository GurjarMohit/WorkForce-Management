package com.railse.hiring.Railse.Backed.service;

import com.railse.hiring.Railse.Backed.Entity.TaskManagement;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface TaskManagementService {
    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    TaskManagementDto findTaskById(Long id);
    List<TaskManagementDto> getSmartTasksByDateRange(LocalDate from, LocalDate to);
    void updateTaskPriority(Long taskId, TaskPriority priority);

    void saveTask(TaskManagement task);

;
    void addComment(Long taskId, String comment, String user);

    TaskDetailsDto getTaskDetails(Long taskId);

    List<TaskManagementDto> getTasksByPriority(TaskPriority priority);


    List<TaskManagementDto> getAllTasks();


}
