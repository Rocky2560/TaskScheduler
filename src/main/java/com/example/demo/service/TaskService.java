package com.example.demo.service;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks()
    {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id)
    {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task)
    {
       return taskRepository.save(task);
    }

    public void deleteTask(Long id)
    {
        taskRepository.deleteById(id);
    }

    public List<Task> getDueTask()
    {
        return taskRepository.findByDueDateBeforeAndStatus(LocalDateTime.now(), TaskStatus.PENDING);
    }

    public Task getPostById(int id)
    {
        return taskRepository.findById((long)id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public void saveorUpdate(Task posts)
    {
        taskRepository.save(posts);
    }

}
