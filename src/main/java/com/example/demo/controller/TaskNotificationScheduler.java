package com.example.demo.controller;

import com.example.demo.Repository.TaskRepository;
import com.example.demo.model.Task;
import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskNotificationScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    // Cron expression: Every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void notifyPendingTasks() throws MessagingException {
        List<Task> pendingTasks = taskRepository.findByStatus("PENDING");

        if (!pendingTasks.isEmpty()) {
            String taskList = pendingTasks.stream()
                    .map(task -> "- " + task.getTitle() + " | Due: " + task.getDueDate())
                    .collect(Collectors.joining("\n"));
            // Group by User
//            Map<User, List<Task>> tasksByUser = pendingTasks.stream()
//                    .filter(Task::isNotifyUser) // Only tasks with notifyUser = true
//                    .collect(Collectors.groupingBy(Task::getUser));

            String body = "Hello,\n\nYou have the following PENDING tasks:\n\n" + taskList +
                    "\n\nPlease take necessary actions.\n\nRegards,\nTask Scheduler System";

            emailService.sendTaskHtmlEmail("recipient-email@gmail.com", "Pending Tasks Notification", body);
        }
    }
}
