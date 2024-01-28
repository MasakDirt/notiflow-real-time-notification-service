package com.proj.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    private NotificationData(String recipientUserTelegram, String senderUserName) {
        this.recipientUserTelegram = recipientUserTelegram;
        this.senderUserName = senderUserName;
        this.message = String.format("""
                Hello %s,
                              
                You have successfully subscribed to receive notifications from %s.\s
                You will now receive updates and notifications whenever %s posts or shares new content.
                              
                If you wish to unsubscribe at any time, simply click on the "Unsubscribe" button in your settings.
                              
                Thank you for using our notification service!
                              
                Notiflow""", recipientUserTelegram, senderUserName, senderUserName);
    }

    public static NotificationData forTelegram(String recipientUserTelegram, String senderUserName) {
        return new NotificationData(recipientUserTelegram, senderUserName);
    }
}
