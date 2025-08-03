package com.railse.hiring.Railse.Backed.repository;

import com.railse.hiring.Railse.Backed.Entity.TaskManagement;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.common.model.enums.ReferenceType;
import com.railse.hiring.Railse.Backed.common.model.enums.Task;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskRepository implements TaskRepository{


    private final Map<Long, TaskManagement> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);


    public InMemoryTaskRepository() {
        // Seed data
        createSeedTask(101L, ReferenceType.ORDER, Task.CREATE_INVOICE, 1L, TaskStatus.ASSIGNED, TaskPriority.HIGH);
        createSeedTask(101L, ReferenceType.ORDER, Task.ARRANGE_PICKUP, 1L, TaskStatus.COMPLETED, TaskPriority.HIGH);
        createSeedTask(102L, ReferenceType.ORDER, Task.CREATE_INVOICE, 2L, TaskStatus.ASSIGNED, TaskPriority.MEDIUM);
        createSeedTask(201L, ReferenceType.ENTITY, Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 2L, TaskStatus.ASSIGNED, TaskPriority.LOW);
        createSeedTask(201L, ReferenceType.ENTITY, Task.ASSIGN_CUSTOMER_TO_SALES_PERSON, 3L, TaskStatus.ASSIGNED, TaskPriority.LOW); // Duplicate for Bug #1
        createSeedTask(103L, ReferenceType.ORDER, Task.COLLECT_PAYMENT, 1L, TaskStatus.CANCELLED, TaskPriority.MEDIUM); // For Bug #2
    }


    private void createSeedTask(Long refId, ReferenceType refType, Task task, Long assigneeId, TaskStatus status, TaskPriority taskPriority) {
        long newId = idCounter.incrementAndGet();
        TaskManagement newTask = new TaskManagement();
        newTask.setId(newId);
        newTask.setReferenceId(refId);
        newTask.setReferenceType(refType);
        newTask.setTask(task);
        newTask.setAssigneeId(assigneeId);
        newTask.setStatus(status);
        newTask.setTaskPriority(taskPriority);
        newTask.setDescription("This is a seed task.");

        // ✅ Correctly setting LocalDateTime
        newTask.setTaskDeadlineTime(LocalDateTime.now().plusDays(1));

        taskStore.put(newId, newTask);
    }




    @Override
    public Optional<TaskManagement> findById(Long id) {
        return Optional.ofNullable(taskStore.get(id));
    }


    @Override
    public TaskManagement save(TaskManagement task) {
        if (task.getId() == null) {
            task.setId(idCounter.incrementAndGet());
        }
        taskStore.put(task.getId(), task);
        return task;
    }


    @Override
    public List<TaskManagement> findAll() {
        return List.copyOf(taskStore.values());
    }



@Override
    public List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, com.railse.hiring.Railse.Backed.common.model.enums.ReferenceType referenceType) {
        return taskStore.values().stream()
                .filter(task -> task.getReferenceId().equals(referenceId) && task.getReferenceType().equals(referenceType))
                .collect(Collectors.toList());
    }




    @Override
    public List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds) {
        return taskStore.values().stream()
                .filter(task -> assigneeIds.contains(task.getAssigneeId()))
                .collect(Collectors.toList());
    }
}

