package com.challenge.dux.service;

import com.challenge.dux.exception.DuplicateTeamException;
import com.challenge.dux.exception.TeamNotFoundException;
import com.challenge.dux.model.dto.createteam.CreateTeamRequest;
import com.challenge.dux.model.dto.createteam.CreateTeamResponse;
import com.challenge.dux.model.dto.deleteteam.DeleteTeamResponse;
import com.challenge.dux.model.dto.getallteams.GetAllTeamsResponse;
import com.challenge.dux.model.dto.getteambyid.GetTeamByIdResponse;
import com.challenge.dux.model.dto.searchteambyname.SearchTeamByNameResponse;
import com.challenge.dux.model.dto.updateteam.UpdateTeamRequest;
import com.challenge.dux.model.dto.updateteam.UpdateTeamResponse;
import com.challenge.dux.model.entity.Team;
import com.challenge.dux.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    private TeamRepository teamRepository;
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        teamRepository = Mockito.mock(TeamRepository.class);
        teamService = new TeamService(teamRepository);
    }

    @Test
    void shouldReturnAllTeams() {
        List<Team> mockTeams = Arrays.asList(
                new Team(1L, "Team A", "League A", "Country A"),
                new Team(2L, "Team B", "League B", "Country B")
        );
        when(teamRepository.findAll()).thenReturn(mockTeams);

        List<GetAllTeamsResponse> response = teamService.getAllTeams();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Team A", response.get(0).getName());
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTeamById() {
        Team mockTeam = new Team(1L, "Team A", "League A", "Country A");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(mockTeam));

        GetTeamByIdResponse response = teamService.getTeamById(1L);

        assertNotNull(response);
        assertEquals("Team A", response.getName());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTeamByIdNotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(99L));

        assertEquals("Team with ID 99 not found.", exception.getMessage());
        verify(teamRepository, times(1)).findById(99L);
    }

    @Test
    void shouldSearchTeamsByName() {
        List<Team> mockTeams = Arrays.asList(
                new Team(1L, "Team A", "League A", "Country A"),
                new Team(2L, "Team B", "League B", "Country B")
        );
        when(teamRepository.findByNameContainingIgnoreCase("Team")).thenReturn(mockTeams);

        List<SearchTeamByNameResponse> response = teamService.searchTeamsByName("Team");

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Team A", response.get(0).getName());
        verify(teamRepository, times(1)).findByNameContainingIgnoreCase("Team");
    }

    @Test
    void shouldThrowExceptionWhenNoTeamsFoundByName() {
        when(teamRepository.findByNameContainingIgnoreCase("Unknown")).thenReturn(List.of());

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamService.searchTeamsByName("Unknown"));

        assertEquals("No teams found with name containing: Unknown", exception.getMessage());
        verify(teamRepository, times(1)).findByNameContainingIgnoreCase("Unknown");
    }

    @Test
    void shouldCreateTeam() {
        CreateTeamRequest request = new CreateTeamRequest("New Team", "New League", "New Country");
        Team savedTeam = new Team(1L, "New Team", "New League", "New Country");

        when(teamRepository.findByNameIgnoreCase(request.getName())).thenReturn(Optional.empty());
        when(teamRepository.save(any(Team.class))).thenReturn(savedTeam);

        CreateTeamResponse response = teamService.createTeam(request);

        assertNotNull(response);
        assertEquals("New Team", response.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateTeam() {
        CreateTeamRequest request = new CreateTeamRequest("Existing Team", "League", "Country");
        when(teamRepository.findByNameIgnoreCase(request.getName())).thenReturn(Optional.of(new Team()));

        Exception exception = assertThrows(DuplicateTeamException.class, () -> teamService.createTeam(request));

        assertEquals("A team with this name already exists.", exception.getMessage());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void shouldUpdateTeam() {
        Team existingTeam = new Team(1L, "Old Team", "Old League", "Old Country");
        UpdateTeamRequest request = new UpdateTeamRequest("Updated Team", "New League", "New Country");
        Team updatedTeam = new Team(1L, "Updated Team", "New League", "New Country");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.findByNameIgnoreCase(request.getName())).thenReturn(Optional.empty());
        when(teamRepository.save(existingTeam)).thenReturn(updatedTeam);

        UpdateTeamResponse response = teamService.updateTeam(1L, request);

        assertNotNull(response);
        assertEquals("Updated Team", response.getName());
        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).save(existingTeam);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToExistingTeamName() {
        UpdateTeamRequest request = new UpdateTeamRequest("Existing Team", "League", "Country");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(new Team(1L, "Old Team", "League", "Country")));
        when(teamRepository.findByNameIgnoreCase(request.getName())).thenReturn(Optional.of(new Team(2L, "Existing Team", "League", "Country")));

        Exception exception = assertThrows(DuplicateTeamException.class, () -> teamService.updateTeam(1L, request));

        assertEquals("A team with this name already exists.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTeam() {
        UpdateTeamRequest request = new UpdateTeamRequest("Updated Team", "New League", "New Country");
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(1L, request));

        assertEquals("Team with ID 1 not found.", exception.getMessage());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void shouldDeleteTeam() {
        when(teamRepository.existsById(1L)).thenReturn(true);

        DeleteTeamResponse response = teamService.deleteTeam(1L);

        assertNotNull(response);
        assertEquals("Team with ID 1 successfully deleted.", response.getMessage());
        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTeam() {
        when(teamRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeam(1L));

        assertEquals("Team with ID 1 not found.", exception.getMessage());
        verify(teamRepository, never()).deleteById(anyLong());
    }
}
