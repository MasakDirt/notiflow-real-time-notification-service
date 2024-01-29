package com.proj.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.proj.user.model.User;
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

    @NotEmpty(message = "Message must not be empty!")
    private String message;

    private NotificationData(String recipientUserTelegram) {
        this.recipientUserTelegram = recipientUserTelegram;
    }

    public static NotificationData forTelegram(User recipient, User sender) {
        NotificationData notificationData = new NotificationData(recipient.getTelegram());
        notificationData.message = String.format("""
                Hello "%s"(%s)👋,
                              
                You have successfully subscribed to receive notifications from "%s"(%s).\s
                You will now receive updates and notifications whenever %s posts or shares new content.
                              
                Thank you for using our notification service❤️!
                              
                Notiflow🤖""", recipient.getFullName(), recipient.getTelegram(),
                sender.getFullName(), sender.getTelegram(), sender.getTelegram());
        return notificationData;
    }
}
