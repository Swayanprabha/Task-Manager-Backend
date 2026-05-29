package org.seenu.taskManager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserSignUpRequestDto {
    private String name;
    @Email(message = "Invalid email format")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one letter and one number")
    private String password;
}
