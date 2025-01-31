package com.challenge.dux.controller;

import com.challenge.dux.exception.TeamNotFoundException;
import com.challenge.dux.model.dto.createteam.CreateTeamRequest;
import com.challenge.dux.model.dto.createteam.CreateTeamResponse;
import com.challenge.dux.model.dto.getallteams.GetAllTeamsResponse;
import com.challenge.dux.model.dto.getteambyid.GetTeamByIdResponse;
import com.challenge.dux.model.dto.searchteambyname.SearchTeamByNameResponse;
import com.challenge.dux.model.dto.updateteam.UpdateTeamRequest;
import com.challenge.dux.model.dto.updateteam.UpdateTeamResponse;
import com.challenge.dux.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private GetAllTeamsResponse team1;
    private GetAllTeamsResponse team2;
    private CreateTeamRequest createRequest;
    private CreateTeamResponse createResponse;
    private UpdateTeamRequest updateRequest;
    private UpdateTeamResponse updateResponse;

    @BeforeEach
    void setUp() {
        team1 = new GetAllTeamsResponse(1L, "Team A", "League A", "Country A");
        team2 = new GetAllTeamsResponse(2L, "Team B", "League B", "Country B");

        createRequest = new CreateTeamRequest("New Team", "New League", "New Country");
        createResponse = new CreateTeamResponse(3L, "New Team", "New League", "New Country");

        updateRequest = new UpdateTeamRequest("Updated Team", "League A", "Country A");
        updateResponse = new UpdateTeamResponse(1L, "Updated Team", "League A", "Country A");
    }

    @Test
    void shouldReturnAllTeams() {
        when(teamService.getAllTeams()).thenReturn(Arrays.asList(team1, team2));

        ResponseEntity<List<GetAllTeamsResponse>> response = teamController.getAllTeams();

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Team A", response.getBody().get(0).getName());
    }

    @Test
    void shouldReturnTeamById() {
        GetTeamByIdResponse teamResponse = new GetTeamByIdResponse(1L, "Team A", "League A", "Country A");
        when(teamService.getTeamById(1L)).thenReturn(teamResponse);

        ResponseEntity<GetTeamByIdResponse> response = teamController.getTeamById(1L);

        assertNotNull(response.getBody());
        assertEquals("Team A", response.getBody().getName());
    }

    @Test
    void shouldThrowExceptionWhenTeamNotFound() {
        when(teamService.getTeamById(anyLong())).thenThrow(new TeamNotFoundException("Team not found"));

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamController.getTeamById(99L));

        assertEquals("Team not found", exception.getMessage());
    }

    @Test
    void shouldSearchTeamsByName() {
        SearchTeamByNameResponse teamResponse1 = new SearchTeamByNameResponse(1L, "Team A", "League A", "Country A");
        SearchTeamByNameResponse teamResponse2 = new SearchTeamByNameResponse(2L, "Team B", "League B", "Country B");

        when(teamService.searchTeamsByName("Team")).thenReturn(Arrays.asList(teamResponse1, teamResponse2));

        ResponseEntity<List<SearchTeamByNameResponse>> response = teamController.searchTeamsByName("Team");

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldCreateTeam() {
        when(teamService.createTeam(any(CreateTeamRequest.class))).thenReturn(createResponse);

        ResponseEntity<CreateTeamResponse> response = teamController.createTeam(createRequest);

        assertNotNull(response.getBody());
        assertEquals("New Team", response.getBody().getName());
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateTeam() {
        when(teamService.createTeam(any(CreateTeamRequest.class))).thenThrow(new IllegalArgumentException("Team already exists"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> teamController.createTeam(createRequest));

        assertEquals("Team already exists", exception.getMessage());
    }

    @Test
    void shouldUpdateTeam() {
        when(teamService.updateTeam(anyLong(), any(UpdateTeamRequest.class))).thenReturn(updateResponse);

        ResponseEntity<UpdateTeamResponse> response = teamController.updateTeam(1L, updateRequest);

        assertNotNull(response.getBody());
        assertEquals("Updated Team", response.getBody().getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTeam() {
        when(teamService.updateTeam(anyLong(), any(UpdateTeamRequest.class))).thenThrow(new TeamNotFoundException("Team not found"));

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamController.updateTeam(99L, updateRequest));

        assertEquals("Team not found", exception.getMessage());
    }

    @Test
    void shouldDeleteTeam() {
        ResponseEntity<Void> response = teamController.deleteTeam(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(teamService, times(1)).deleteTeam(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTeam() {
        doThrow(new TeamNotFoundException("Team not found")).when(teamService).deleteTeam(anyLong());

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamController.deleteTeam(99L));

        assertEquals("Team not found", exception.getMessage());
    }
}
