package com.example.demo.service;

import com.example.demo.model.Task;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import java.util.List;

@Component
@DisallowConcurrentExecution
public class TaskReminderJob implements Job {

    @Autowired
    private  TaskService taskService;

    @Autowired
    private NotificationService notificationServer;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Task> dueTasks = taskService.getDueTask();
        for (Task task : dueTasks)
        {
            notificationServer.sendTaskReminder(task);
        }
    }
}
