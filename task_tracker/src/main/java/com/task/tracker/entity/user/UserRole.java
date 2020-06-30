package com.task.tracker.entity.user;

import javax.persistence.*;

@Entity
@Table(	name = "user_roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userRoleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EUserRole name;

    public UserRole() {

    }

    public UserRole(EUserRole name) {
        this.name = name;
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public EUserRole getName() {
        return name;
    }

    public void setName(EUserRole name) {
        this.name = name;
    }
}

