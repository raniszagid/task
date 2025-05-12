package com.example.task_app.service;

import com.example.task_app.model.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.from}")
    private String from;
    @Value("${mail.to}")
    private String to;

    public void sendEmail(TaskDto taskDto, Long id) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Изменение задачи " + id);
        message.setText("Произошли следующие изменения:\n" + taskDto.toString());
        mailSender.send(message);
        log.info("Уведомление отправлено");
    }
}
