package ru.clevertec.newsmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Client request DTO for authentication")
public class AuthenticationRequest {
    @NotBlank(message = "Username mandatory")
    @Length(min = 2,max = 20)
    @Schema(description = "Client username/login(min:2/max:20)")
    private String username;
    @NotBlank(message = "Password mandatory")
    @Length(min = 8,max = 20)
    @Schema(description = "Client password(min:8/max:20)")
    private String password;
}
