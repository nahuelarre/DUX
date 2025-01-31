package com.challenge.dux.model.dto.deleteteam;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTeamRequest {

    @NotNull(message = "Team ID is required")
    private Long id;
}
