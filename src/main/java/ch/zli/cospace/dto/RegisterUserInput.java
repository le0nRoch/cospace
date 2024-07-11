package ch.zli.cospace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterUserInput {
    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @NotNull
    @Size(min = 8, max = 128)
    private String password;
    @NotNull
    @Size(max = 30)
    private String firstName;
    @NotNull
    @Size(max = 30)
    private String lastName;
}
