package com.example.demo.service;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
