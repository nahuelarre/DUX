package com.challenge.dux.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequest {

    @Schema(description = "User's username", example = "test")
    private String username;

    @Schema(description = "User's password", example = "12345")
    private String password;
}
