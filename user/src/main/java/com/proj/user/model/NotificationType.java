package com.proj.user.model;

import lombok.Getter;

@Getter
public enum NotificationType {
    TELEGRAM("Telegram"), EMAIL("E-mail");

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }

    public static NotificationType getTypeFromName(String name) {
        for (NotificationType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new EnumConstantNotPresentException(NotificationType.class, name);
    }
}
