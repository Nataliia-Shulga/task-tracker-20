package com.task.tracker.repository;

import com.task.tracker.entity.task.EStatus;
import com.task.tracker.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    Boolean existsByTitle(String title);

    List<Task> findAllByStatus(EStatus status);
}
