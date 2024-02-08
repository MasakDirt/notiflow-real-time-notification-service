package com.proj.emailkafka.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationData {

    @NotEmpty(message = "The email account cannot be empty!")
    private String username;

    @NotEmpty(message = "Message must not be empty!")
    private String message;

    public static NotificationData fromJsonString(String jsonData) {
        try {
            return new ObjectMapper().readValue(jsonData, NotificationData.class);
        } catch (Exception e) {
            throw new RuntimeException("JsonData isn`t appropriate to notification data");
        }
    }

    @Override
    public String toString() {
        return "NotificationData{" +
                "username='" + username + '\'' +
                ", message='" + message.substring(0, 33) + '\'' +
                '}';
    }
}
