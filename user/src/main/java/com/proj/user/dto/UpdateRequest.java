package com.proj.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {

    private long id;

    @NotEmpty(message = "Fill in the field with the name.")
    private String fullName;

    @Pattern(regexp = "^@.*", message = "The telegram account must started with '@' character")
    private String telegram;

    @NotNull
    @Enumerated(EnumType.STRING)
    private String notificationType;

    public static UpdateRequest withId(long id) {
        return builder().id(id).build();
    }
}
