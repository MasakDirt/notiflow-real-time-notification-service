package com.proj.telegramkafka.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "telegram_users")
@NoArgsConstructor
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "The username cannot be empty")
    private String username;

    @NotEmpty(message = "The chatId cannot be empty")
    @Column(name = "chat_id", nullable = false)
    private String chatId;

    public TelegramUser(String username, String chatId) {
        this.username = username;
        this.chatId = chatId;
    }
}
