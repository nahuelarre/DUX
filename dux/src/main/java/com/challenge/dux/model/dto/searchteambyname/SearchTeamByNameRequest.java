package com.challenge.dux.model.dto.searchteambyname;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTeamByNameRequest {

    @NotBlank(message = "Team name is required")
    private String name;
}

