package com.mk.demo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 30, message = "Username must have between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = """
        Password must be at least 8 characters long and contain:
        - at least one uppercase letter
        - one lowercase letter
        - one number
        - and one special character (e.g. @, $, !, %, *, ? or &)
        """
    )
    private String password;
}
