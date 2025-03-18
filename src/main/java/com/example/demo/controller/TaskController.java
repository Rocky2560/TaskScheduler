package com.example.demo.controller;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Task;
import com.example.demo.model.Users;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }

    @GetMapping
    public String getAllTasks(Model model, Principal principal) {

        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    @GetMapping("/new")
    public String showTaskForm(Model model, Principal principal) {
        Users user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setUser(user);

        model.addAttribute("task", task); // Use singular naming for clarity
        return "task-form";
    }


    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task, @RequestParam("user_id") String userId) {
        Long id = Long.parseLong(userId);
        System.out.println(id);
        Users user = userRepository.findById(id).orElseThrow();
        task.setUser(user);
        System.out.println(task);
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id).orElse(null));
        return "task-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
