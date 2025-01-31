package com.challenge.dux.model.dto.createteam;

import com.challenge.dux.model.dto.TeamRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamRequest implements TeamRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "League is required")
    private String league;

    @NotBlank(message = "Country is required")
    private String country;
}
