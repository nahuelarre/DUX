package com.challenge.dux.model.dto.createteam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamResponse {
    private Long id;
    private String name;
    private String league;
    private String country;
}

