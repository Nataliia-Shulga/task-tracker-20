package com.task.tracker.servise;

import com.task.tracker.entity.task.EStatus;
import com.task.tracker.entity.task.Task;
import com.task.tracker.entity.user.User;
import com.task.tracker.repository.TaskRepository;
import com.task.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> sortByUser() {
        return  taskRepository.findAll(Sort.by("userId").descending());
    }

    public List<Task> findAllByStatus(String strStatus) {
        EStatus status = getStatus(strStatus);
        return taskRepository.findAllByStatus(status);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task save(Task task, String username) {
        Task newTask = new Task(task.getTitle(), task.getDescription());
        newTask.setStatus(com.task.tracker.entity.task.EStatus.VIEW);
        Long userId;
        Optional<User> userFromDB;
        if ((userFromDB = userRepository.findByUsername(username)).isPresent()) {
            userId = userFromDB.get().getUserId();
            newTask.setUserId(userId);
        } else {
            throw new RuntimeException("Error: User for task assigning is not found.");
        }
        return taskRepository.save(newTask);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public EStatus getStatus(String strStatus) {
        EStatus status;
        if (strStatus == null) {
            throw new RuntimeException("Error: Status is not given.");
        } else {
            switch (strStatus) {
                case "done":
                    status = EStatus.DONE;
                    break;
                case "prog":
                    status = EStatus.IN_PROGRESS;
                    break;
                default:
                    status = EStatus.VIEW;
            }
        }
        return status;
    }

    public void deleteById(Long id) {
        Optional<Task> taskFromDB = taskRepository.findById(id);
        taskFromDB.ifPresent(task -> taskRepository.delete(task));
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }

    public Optional<Task> findByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public Boolean existsByTitle(String title) {
        return taskRepository.existsByTitle(title);
    }
}
