package com.example.task_app.kafka;

import com.example.task_app.model.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class UpdateLogProducer {
    private final KafkaTemplate kafkaTemplate;

    public void send(TaskDto taskDto, Long id) {
        Message<TaskDto> message = MessageBuilder
                .withPayload(taskDto)
                .setHeader(KafkaHeaders.TOPIC, "updated_tasks")
                .setHeader(KafkaHeaders.KEY, String.valueOf(id))
                .build();
        try {
            kafkaTemplate.send(message);
            log.info("Измененные данные отправлены в топик");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        kafkaTemplate.flush();
    }
}
