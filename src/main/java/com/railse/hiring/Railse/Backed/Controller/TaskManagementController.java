package com.railse.hiring.Railse.Backed.Controller;


import com.railse.hiring.Railse.Backed.Entity.Comment;
import com.railse.hiring.Railse.Backed.Entity.TaskManagement;
import com.railse.hiring.Railse.Backed.Response.Response;
import com.railse.hiring.Railse.Backed.common.model.enums.TaskPriority;
import com.railse.hiring.Railse.Backed.dto.*;
import com.railse.hiring.Railse.Backed.mapper.ITaskManagementMapper;
import com.railse.hiring.Railse.Backed.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {

   @Autowired
    private final TaskManagementService taskManagementService;
    @Autowired
    private ITaskManagementMapper mapper;

    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Response<TaskManagementDto>> getTaskById(@PathVariable Long id) {
        TaskManagementDto dto = taskManagementService.findTaskById(id);
        return ResponseEntity.ok(new Response<>(dto));
    }

    @GetMapping("/all")
    public Response<List<TaskManagementDto>> getAllTasks() {
        return new Response<>(taskManagementService.getAllTasks());
    }


    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
        List<TaskManagementDto> createdTasks = taskManagementService.createTasks(request);
        return new Response<>(createdTasks);
    }


    @PostMapping("/update")
    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }


    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }


    @PostMapping("/fetch-by-date/v2")
    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }
    @GetMapping("/tasks/date-range")
    public ResponseEntity<List<TaskManagementDto>> getTasksForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<TaskManagementDto> tasks =taskManagementService.getSmartTasksByDateRange(from, to);
        return ResponseEntity.ok(tasks);
    }
    @PutMapping("/tasks/{taskId}/priority")
    public ResponseEntity<Void> updatePriority(
            @PathVariable Long taskId,
            @RequestParam TaskPriority priority) {
        taskManagementService.updateTaskPriority(taskId, priority);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<List<TaskManagementDto>> getTasksByPriority(@PathVariable TaskPriority priority) {
        return ResponseEntity.ok(taskManagementService.getTasksByPriority(priority));
    }
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable Long taskId,
            @RequestBody Comment comment) {
        taskManagementService.addComment(taskId, comment.getComment(), comment.getUser());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/tasks/{taskId}/details")
    public ResponseEntity<TaskDetailsDto> getTaskDetails(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskManagementService.getTaskDetails(taskId));
    }

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody TaskManagementDto dto) {
        TaskManagement model = mapper.dtoToModel(dto); // ✅ use the method here
        taskManagementService.saveTask(model);
        return ResponseEntity.ok("Task created");
    }


}



