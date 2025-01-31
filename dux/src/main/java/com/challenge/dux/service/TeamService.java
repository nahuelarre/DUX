package com.challenge.dux.service;

import com.challenge.dux.exception.TeamNotFoundException;
import com.challenge.dux.exception.DuplicateTeamException;
import com.challenge.dux.model.dto.TeamRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<GetAllTeamsResponse> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new GetAllTeamsResponse(team.getId(), team.getName(), team.getLeague(), team.getCountry()))
                .collect(Collectors.toList());
    }

    public GetTeamByIdResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team with ID " + id + " not found."));
        return new GetTeamByIdResponse(team.getId(), team.getName(), team.getLeague(), team.getCountry());
    }

    public List<SearchTeamByNameResponse> searchTeamsByName(String name) {
        List<Team> teams = teamRepository.findByNameContainingIgnoreCase(name);
        if (teams.isEmpty()) {
            throw new TeamNotFoundException("No teams found with name containing: " + name);
        }
        return teams.stream()
                .map(team -> new SearchTeamByNameResponse(team.getId(), team.getName(), team.getLeague(), team.getCountry()))
                .collect(Collectors.toList());
    }

    public CreateTeamResponse createTeam(CreateTeamRequest request) {
        validateTeam(request);

        if (teamRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DuplicateTeamException("A team with this name already exists.");
        }

        Team newTeam = new Team();
        newTeam.setName(request.getName());
        newTeam.setLeague(request.getLeague());
        newTeam.setCountry(request.getCountry());

        Team savedTeam = teamRepository.save(newTeam);

        return new CreateTeamResponse(savedTeam.getId(), savedTeam.getName(), savedTeam.getLeague(), savedTeam.getCountry());
    }



    public UpdateTeamResponse updateTeam(Long id, UpdateTeamRequest request) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team with ID " + id + " not found."));

        validateTeam(request);

        Optional<Team> existingTeamWithSameName = teamRepository.findByNameIgnoreCase(request.getName());
        if (existingTeamWithSameName.isPresent() && !existingTeamWithSameName.get().getId().equals(id)) {
            throw new DuplicateTeamException("A team with this name already exists.");
        }

        existingTeam.setName(request.getName());
        existingTeam.setLeague(request.getLeague());
        existingTeam.setCountry(request.getCountry());

        Team updatedTeam = teamRepository.save(existingTeam);

        return new UpdateTeamResponse(updatedTeam.getId(), updatedTeam.getName(), updatedTeam.getLeague(), updatedTeam.getCountry());
    }


    public DeleteTeamResponse deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new TeamNotFoundException("Team with ID " + id + " not found.");
        }
        teamRepository.deleteById(id);
        return new DeleteTeamResponse("Team with ID " + id + " successfully deleted.");
    }

    private void validateTeam(TeamRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Team name cannot be empty.");
        }
        if (request.getLeague() == null || request.getLeague().isBlank()) {
            throw new IllegalArgumentException("League cannot be empty.");
        }
        if (request.getCountry() == null || request.getCountry().isBlank()) {
            throw new IllegalArgumentException("Country cannot be empty.");
        }
    }

}
