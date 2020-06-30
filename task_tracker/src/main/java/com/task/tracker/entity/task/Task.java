package com.task.tracker.entity.task;

import com.task.tracker.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private java.lang.Long taskId;

    @NotBlank
    @Size(max = 20)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    private Long userId;

    public Task() {
    }

    public Task(@NotBlank @Size(max = 20) String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
