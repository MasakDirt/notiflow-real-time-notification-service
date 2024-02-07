package com.proj.user.dto;

import com.proj.user.model.NotificationType;
import com.proj.user.model.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NotificationData {

    @NotEmpty(message = "The username must not be empty!")
    private String username;

    @NotEmpty(message = "Message must not be empty!")
    private String message;

    private NotificationData(String username) {
        this.username = username;
    }

    public static NotificationData of(User recipient, User forWhomSubscribe) {
        NotificationData notificationData = chooseNotificationType(recipient);
        notificationData.setMessage(recipient, forWhomSubscribe);
        return notificationData;
    }

    private static NotificationData chooseNotificationType(User recipient) {
        if (recipient.getNotificationType().equals(NotificationType.EMAIL)) {
            return new NotificationData(recipient.getEmail());
        } else {
            return new NotificationData(recipient.getTelegram());
        }
    }

    private void setMessage(User recipient, User forWhomSubscribe) {
        String forWhomSubscribeUserName = forWhomSubscribe.getFullName();
        this.message = String.format("""
                        Hello "%s"👋,
                                      
                        You have successfully subscribed to receive notifications from "%s".\s
                        You will now receive updates and notifications whenever %s posts or shares new content.
                                      
                        Thank you for using our notification service❤️!
                                      
                        Notiflow🤖""", recipient.getFullName(), forWhomSubscribeUserName, forWhomSubscribeUserName);
    }
}
