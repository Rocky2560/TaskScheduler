package com.example.demo.service;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.model.Task;
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

//    public List<Task> getTasksByUser(String username)
//    {
//        return taskRepository.findByUser(username);
//    }

    public Task getTaskById(long id)
    {
        return taskRepository.findById(id).get();
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUser_Id(userId);
    }

    public Task getTaskByUser(long id)
    {
        return taskRepository.findById(id).get();
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

    public void saveorUpdate(Task task)
    {
        taskRepository.save(task);
    }

}
