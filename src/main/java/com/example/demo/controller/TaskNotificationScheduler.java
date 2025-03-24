package com.example.demo.controller;

import com.example.demo.Enum.TaskStatus;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Task;
import com.example.demo.model.Users;
import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TaskNotificationScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Cron expression: Every day at 8 AM
    @Scheduled(cron = "0 1 0 * * ?")
    public void notifyPendingTasks() throws MessagingException {
        List<Task> pendingTasks = taskRepository.findByStatus(TaskStatus.PENDING);

        if (!pendingTasks.isEmpty()) {
            // Group tasks by User
            Map<Users, List<Task>> tasksByUser = pendingTasks.stream()
                    .filter(task -> task.getUser() != null && task.getUser().getEmail() != null)
                    .collect(Collectors.groupingBy(Task::getUser));

            // Send email to each user individually
            for (Map.Entry<Users, List<Task>> entry : tasksByUser.entrySet()) {
                Users user = entry.getKey();
                List<Task> userTasks = entry.getValue();

                String userTaskList = userTasks.stream()
                        .map(task -> "- " + task.getTitle() + " | Due: " + task.getDueDate())
                        .collect(Collectors.joining("\n"));

                String body = "Hello " + user.getEmail() + ",\n\nYou have the following PENDING tasks:\n\n"
                        + userTaskList + "\n\nPlease take necessary actions.\n\nRegards,\nTask Scheduler System";

                String recipientEmail = user.getEmail();
                String subject = "Pending Tasks Notification";

                emailService.sendEmail(recipientEmail, subject, body);
            }
        }
    }
}
