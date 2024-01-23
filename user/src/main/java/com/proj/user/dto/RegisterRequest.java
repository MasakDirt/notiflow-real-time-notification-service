package com.proj.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Fill in the field with the name.")
    private String fullName;

    @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid e-mail address")
    private String email;

    @NotBlank(message = "The password cannot be 'blank'")
    private String password;

    @Pattern(regexp = "^@.*", message = "The telegram account must started with '@' character")
    private String telegram;

    @NotNull
    private int age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private String notificationType;
}
