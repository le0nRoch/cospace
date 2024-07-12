package ch.zli.cospace.dto;

import ch.zli.cospace.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserInput {
    @Email
    @Size(max = 50)
    private String email;

    @Size(min = 8, max = 128)
    private String password;
    @Size(max = 30)
    private String firstName;
    @Size(max = 30)
    private String lastName;
    private Role role;
}
