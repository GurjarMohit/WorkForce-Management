package com.railse.hiring.Railse.Backed.service;

import com.railse.hiring.Railse.Backed.Entity.TaskManagement;
import com.railse.hiring.Railse.Backed.common.model.enums.Task;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskStatus;
import com.railse.hiring.Railse.Backed.dto.*;
import com.railse.hiring.Railse.Backed.exception.ResourceNotFoundException;
import com.railse.hiring.Railse.Backed.mapper.ITaskManagementMapper;
import com.railse.hiring.Railse.Backed.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    // For activity logging
    private final Map<Long, List<String>> taskActivityHistory = new HashMap<>();
    private final Map<Long, List<String>> taskComments = new HashMap<>();
    private final Map<Long, List<String>> activityMap = new HashMap<>();

    @Autowired
    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public void updateTaskPriority(Long taskId, TaskPriority priority) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
        task.setTaskPriority(priority);
        taskRepository.save(task);
        logActivity(taskId, "Priority changed to " + priority);
    }

    private void logActivity(Long taskId, String activity) {
        String logEntry = LocalDateTime.now() + " - " + activity;
        taskActivityHistory.computeIfAbsent(taskId, k -> new ArrayList<>()).add(logEntry);
        activityMap.computeIfAbsent(taskId, k -> new ArrayList<>()).add(logEntry);
    }

    @Override
    public void addComment(Long taskId, String comment, String user) {
        String entry = user + " commented: " + comment + " at " + LocalDateTime.now();
        taskComments.computeIfAbsent(taskId, k -> new ArrayList<>()).add(entry);
        activityMap.computeIfAbsent(taskId, k -> new ArrayList<>()).add(entry);
    }

    @Override
    public void saveTask(TaskManagement task) {
        taskRepository.save(task);
        log.info("Saved task with priority: {}", task.getTaskPriority());
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getTaskPriority() == priority && t.getStatus() != TaskStatus.CANCELLED)
                .map(taskMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskManagementDto> getAllTasks() {
        return taskMapper.modelListToDtoList(taskRepository.findAll());
    }

    @Override
    public List<TaskManagementDto> getSmartTasksByDateRange(LocalDate from, LocalDate to) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getStartDate() != null &&
                        ((!task.getStartDate().isBefore(from) && !task.getStartDate().isAfter(to)) ||
                                (task.getStartDate().isBefore(from) && task.getStatus() != TaskStatus.COMPLETED)))
                .map(taskMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        if (createRequest.getRequests() == null || createRequest.getRequests().isEmpty()) {
            throw new IllegalArgumentException("Request list cannot be null or empty.");
        }

        List<TaskManagement> createdTasks = createRequest.getRequests().stream().map(item -> {
            TaskManagement task = new TaskManagement();
            task.setReferenceId(item.getReferenceId());
            task.setReferenceType(item.getReferenceType());
            task.setTask(item.getTask());
            task.setAssigneeId(item.getAssigneeId());
            task.setTaskPriority(Optional.ofNullable(item.getTaskPriority()).orElse(TaskPriority.MEDIUM));
            task.setTaskDeadlineTime(item.getTaskDeadlineTime());
            task.setStatus(TaskStatus.ASSIGNED);
            task.setDescription("New task created.");
            return taskRepository.save(task);
        }).collect(Collectors.toList());

        return taskMapper.modelListToDtoList(createdTasks);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest request) {
        List<TaskManagementDto> updatedTasks = new ArrayList<>();

        for (UpdateTaskRequest.RequestItem item : request.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            task.setStatus(item.getTaskStatus());
            task.setDescription(item.getDescription());
            taskRepository.save(task);

            logActivity(task.getId(), "Status updated to " + item.getTaskStatus() + ", Description: " + item.getDescription());

            updatedTasks.add(taskMapper.modelToDto(task));
        }

        return updatedTasks;
    }

    @Override
    public TaskDetailsDto getTaskDetails(Long taskId) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        TaskDetailsDto dto = new TaskDetailsDto();
        dto.setTask(taskMapper.modelToDto(task));
        dto.setActivityHistory(activityMap.getOrDefault(taskId, List.of()));
        dto.setComments(taskComments.getOrDefault(taskId, List.of()));
        return dto;
    }
//bug 1
    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());

        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(
                request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> activeTasks = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());

            for (TaskManagement oldTask : activeTasks) {
                oldTask.setStatus(TaskStatus.CANCELLED);
                taskRepository.save(oldTask);
            }

            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(request.getReferenceId());
            newTask.setReferenceType(request.getReferenceType());
            newTask.setTask(taskType);
            newTask.setAssigneeId(request.getAssigneeId());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setTaskPriority(TaskPriority.MEDIUM);
            taskRepository.save(newTask);
        }

        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }
//bug 2
    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date must be provided.");
        }

        if (request.getAssigneeIds() == null || request.getAssigneeIds().isEmpty()) {
            throw new IllegalArgumentException("Assignee IDs must be provided.");
        }

        LocalDateTime startTime = request.getStartDate();
        LocalDateTime endTime = request.getEndDate();

        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        List<TaskManagement> filtered = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED)
                .filter(task -> task.getTaskDeadlineTime() != null)
                .filter(task -> !task.getTaskDeadlineTime().isBefore(startTime)
                        && !task.getTaskDeadlineTime().isAfter(endTime))
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filtered);
    }
}
