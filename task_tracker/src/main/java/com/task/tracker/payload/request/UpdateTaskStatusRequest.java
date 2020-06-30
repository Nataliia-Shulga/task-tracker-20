package com.task.tracker.payload.request;

import javax.validation.constraints.NotBlank;

public class UpdateTaskStatusRequest {
    @NotBlank
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}