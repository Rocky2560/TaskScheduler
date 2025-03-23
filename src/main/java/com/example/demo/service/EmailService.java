package com.example.demo.service;

import com.example.demo.model.Task;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendTaskHtmlEmail(String to, String name, String tasks) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("tasks", tasks);

        String htmlBody = templateEngine.process("email-template.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Daily Pending Task Notification");
        helper.setText(htmlBody, true); // true = is HTML

        mailSender.send(message);
    }
}
