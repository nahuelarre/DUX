package com.challenge.dux.model.dto.getteambyid;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamByIdRequest {

    @NotNull(message = "Team ID is required")
    private Long id;
}

