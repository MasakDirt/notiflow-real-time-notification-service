package com.proj.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NotificationData {

    @NotNull(message = "The telegram account must started with '@' character")
    @Pattern(regexp = "^@.+", message = "The telegram account must started with '@' character")
    private String recipientUserTelegram;

    private String senderUserName;

    @NotEmpty(message = "Message must not be empty!")
    private String message;
}
