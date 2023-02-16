package ru.clevertec.newsmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Server response DTO after successfully authentication")
public class AuthenticationResponse {
    @Schema(description = "Client username/login")
    private String username;
    @Schema(description = "Client JWT token for bearer authentication")
    private String jwt;
}
