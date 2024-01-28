package com.proj.user.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
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

    private int age;
}
