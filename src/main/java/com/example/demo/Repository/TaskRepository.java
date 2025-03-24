package com.example.demo.Repository;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByDueDateBeforeAndStatus(LocalDateTime dateTime, TaskStatus status);

    List<Task> findByStatus(TaskStatus status);
}
