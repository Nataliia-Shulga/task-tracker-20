package com.task.tracker.controller;

import com.task.tracker.entity.task.Task;
import com.task.tracker.payload.request.UpdateTaskRequest;
import com.task.tracker.payload.request.UpdateTaskStatusRequest;
import com.task.tracker.payload.request.UpdateTaskUserRequest;
import com.task.tracker.payload.response.MessageResponse;
import com.task.tracker.security.service.UserDetailsImpl;
import com.task.tracker.servise.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = taskService.findAll();

            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable("status") String status) {
        try {
            List<Task> tasks = taskService.findAllByStatus(status);
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks/sort")
    public ResponseEntity<?> getSortedTasks() {
        try {
            List<Task> tasks = taskService.sortByUser();
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks/{task_id}")
    public ResponseEntity<Task> getTaskByTitle(@PathVariable("task_id") Long task_id) {
        Optional<Task> taskFromDB = taskService.findById(task_id);
        return taskFromDB.map(task -> new ResponseEntity<>(task, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        if (taskService.existsByTitle(task.getTitle())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Title is already taken!"));
        }
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Task newTask = taskService
                    .save(task, userDetails.getUsername());
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/tasks/update/{task_id}")
    public ResponseEntity<Task> updateTask(@PathVariable("task_id") Long task_id,
                                           @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        Optional<Task> taskFromDB = taskService.findById(task_id);

        if (taskFromDB.isPresent()) {
            Task updatedTask = taskFromDB.get();
            updatedTask.setTitle(updateTaskRequest.getTitle());
            updatedTask.setDescription(updateTaskRequest.getDescription());
            return new ResponseEntity<>(taskService.update(updatedTask), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/tasks/change/{task_id}")
    public ResponseEntity<Task> changeTask(@PathVariable("task_id") Long task_id,
                                           @Valid @RequestBody UpdateTaskStatusRequest updateStatusRequest) {
        Optional<Task> taskFromDB = taskService.findById(task_id);

        if (taskFromDB.isPresent()) {
            Task updatedTask = taskFromDB.get();
            String status = updateStatusRequest.getStatus();
            updatedTask.setStatus(taskService.getStatus(status));
            return new ResponseEntity<>(taskService.update(updatedTask), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/tasks/user/{task_id}")
    public ResponseEntity<Task> changeUser(@PathVariable("task_id") Long task_id,
                                           @Valid @RequestBody UpdateTaskUserRequest updateTaskUserRequest) {
        Optional<Task> taskFromDB = taskService.findById(task_id);

        if (taskFromDB.isPresent()) {
            Task updatedTask = taskFromDB.get();
            updatedTask.setUserId(updateTaskUserRequest.getUserId());
            return new ResponseEntity<>(taskService.update(updatedTask), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tasks/{task_id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("task_id") Long task_id) {
        try {
            taskService.deleteById(task_id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<HttpStatus> deleteAllTasks() {
        try {
            taskService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}