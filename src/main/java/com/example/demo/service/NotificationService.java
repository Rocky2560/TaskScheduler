package com.example.demo.service;

import com.example.demo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTaskReminder(Task task)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(task.getUser().getEmail());
        message.setSubject("Task Reminder: " + task.getTitle());
        message.setText("Your task '" + task.getTitle() + "' is due on " + task.getDueDate());
        mailSender.send(message);
    }

}
