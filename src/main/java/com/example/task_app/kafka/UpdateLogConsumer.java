package com.example.task_app.kafka;

import com.example.task_app.model.dto.TaskDto;
import com.example.task_app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateLogConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            id = "accountListener",
            topics = {"${kafka.topic.updates}"},
            containerFactory = "kafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=com.example.task_app.model.dto.TaskDto"}
    )
    public void listener(@Payload List<TaskDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String id) {
        try {
            log.info("Получено сообщение в топик: " + topic);
            messageList.forEach(taskDto -> notificationService.sendEmail(taskDto, Long.valueOf(id)));
        }
        finally {
            ack.acknowledge();
        }
    }
}
