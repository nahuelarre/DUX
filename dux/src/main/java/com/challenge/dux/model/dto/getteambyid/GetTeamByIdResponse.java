package com.challenge.dux.model.dto.getteambyid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamByIdResponse {
    private Long id;
    private String name;
    private String league;
    private String country;
}
