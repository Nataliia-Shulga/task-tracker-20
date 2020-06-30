package com.task.tracker.repository;

import com.task.tracker.entity.user.EUserRole;
import com.task.tracker.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(EUserRole name);
}
