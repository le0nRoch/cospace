package ch.zli.cospace.dto;

import ch.zli.cospace.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserInput extends RegisterUserInput {

    @NotNull
    private Role role;

    public CreateUserInput(@NotBlank @Email @Size(max = 50) String email, @NotBlank @Size(min = 8, max = 128) String password, @NotBlank @Size(max = 30) String firstName, @NotBlank @Size(max = 30) String lastName) {
        super(email, password, firstName, lastName);
    }
}
