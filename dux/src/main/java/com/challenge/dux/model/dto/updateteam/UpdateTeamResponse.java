package com.challenge.dux.model.dto.updateteam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamResponse {
    private Long id;
    private String name;
    private String league;
    private String country;
}

