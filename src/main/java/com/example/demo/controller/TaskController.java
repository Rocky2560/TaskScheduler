package com.example.demo.controller;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Task;
import com.example.demo.model.Users;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (tasks == null) {
            tasks = new ArrayList<>(); // or use Collections.emptyList();
        }
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    @GetMapping("/new")
    public String showTaskForm(Model model, Principal principal) {
        Users user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setUser(user);

        model.addAttribute("task", task);
        model.addAttribute("user_id", user.getId());// Use singular naming for clarity
        return "task-form";
    }


    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task, @RequestParam("user_id") Long userId) {
        Long id = userId.longValue();
        System.out.println(userId);

        Users user = userRepository.findById(userId).orElseThrow();
        task.setUser(user);
        System.out.println(task);
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "task-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }


    @GetMapping("editTask/{id}")
    public  ResponseEntity<Task>  editTask(@PathVariable("id") int id, Model model)
    {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("editTask")
    public String updateTask(@RequestParam("user_id") int id, @RequestParam("title") String title, @RequestParam("description")
                                String description, @RequestParam("dueDate") LocalDateTime dueDate, @RequestParam("priority")
                                 String priority,
                                RedirectAttributes redirectAttributes) throws IOException {

        try {
            System.out.println(id);
            Task task = taskService.getTaskById(id);
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setPriority(priority);
            taskService.saveorUpdate(task);
            return "redirect:/tasks";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
