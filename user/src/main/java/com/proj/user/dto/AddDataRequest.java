package com.proj.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddDataRequest {

    @Pattern(regexp = "^@.*", message = "The telegram account must started with '@' character")
    private String telegram;

    private int age;

    @NotNull
    private String notificationType;
}
