package com.proj.user.model;

public enum NotificationType {
    TELEGRAM("Telegram"), EMAIL("E-mail");

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }

    public static NotificationType getTypeFromName(String name) {
        for (NotificationType type : NotificationType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new EnumConstantNotPresentException(NotificationType.class, name);
    }

    public String getName() {
        return name;
    }
}
