package com.example.demo.controller;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Task;
import com.example.demo.model.Users;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserDetailService;
import jakarta.mail.MessagingException;
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

    @Autowired
    private UserDetailService userService;

    @Autowired
    private TaskNotificationScheduler taskNotificationScheduler;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }


    @GetMapping
    public String getAllTasks(Model model, Principal principal) throws MessagingException {

        String username = principal.getName(); // Get username from logged-in user

        Optional<Users> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            Long userId = optionalUser.get().getId(); // Get the user ID
            List<Task> tasks = taskService.getTasksByUserId(userId);
            model.addAttribute("tasks", tasks != null ? tasks : new ArrayList<>());
        } else {
            model.addAttribute("tasks", new ArrayList<>());
        }
//        List<Task> tasks = taskService.getAllTasks();
//        model.addAttribute("tasks", tasks);
//        taskNotificationScheduler.notifyPendingTasks();
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
    public String saveTask(@ModelAttribute Task task, @RequestParam("user_id") Long userId, RedirectAttributes redirectAttributes) {
        Long id = userId.longValue();
        System.out.println(userId);

        Users user = userRepository.findById(userId).orElseThrow();
        task.setUser(user);
        System.out.println(task);
        taskService.saveTask(task);
        redirectAttributes.addFlashAttribute("message", "Your tasks has been Added");
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "task-form";
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
                                 String priority, @RequestParam("status") String status,
                                RedirectAttributes redirectAttributes) throws IOException {

        try {
            System.out.println(id);
            Task task = taskService.getTaskById(id);
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setPriority(priority);
            task.setStatus(TaskStatus.valueOf(status));
            taskService.saveorUpdate(task);
            redirectAttributes.addFlashAttribute("message", "Your tasks has been Updated");
            return "redirect:/tasks";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Getting the id for the edit process
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable ("id") long id, RedirectAttributes redirectAttributes)
    {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("message", "Your tasks has been deleted");
        return "redirect:/tasks";
    }

}
