package com.challenge.dux.model.dto.getallteams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllTeamsResponse {
    private Long id;
    private String name;
    private String league;
    private String country;
}

