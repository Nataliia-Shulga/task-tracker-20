package com.task.tracker.entity.task;

public enum EStatus {
    VIEW("View"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private String statusName;

    EStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}
