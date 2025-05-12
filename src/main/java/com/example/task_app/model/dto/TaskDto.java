package com.example.task_app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("status")
    private String status;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (title != null) {
            stringBuilder.append("title: ").append(title);
        }
        if (description != null) {
            stringBuilder.append("description: ").append(description);
        }
        if (userId != null) {
            stringBuilder.append("user ID: ").append(userId);
        }
        if (status != null) {
            stringBuilder.append("status: ").append(status);
        }
        return stringBuilder.toString();
    }
}
