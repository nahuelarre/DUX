package com.challenge.dux.model.dto.searchteambyname;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTeamByNameResponse {
    private Long id;
    private String name;
    private String league;
    private String country;
}

