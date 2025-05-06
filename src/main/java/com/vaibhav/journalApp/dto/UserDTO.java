package com.vaibhav.journalApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull
    @Schema(description = "The user's username")
    private String username;
    private String email;
    private boolean sentimentAnalysis;
    @NotNull
    private String password;
}
