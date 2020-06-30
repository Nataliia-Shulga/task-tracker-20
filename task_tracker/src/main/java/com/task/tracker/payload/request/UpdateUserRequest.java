package com.task.tracker.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class UpdateUserRequest {
    private Set<String> role;

    @NotBlank
    private String firstName;

    @NotBlank
    private String LastName;

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}