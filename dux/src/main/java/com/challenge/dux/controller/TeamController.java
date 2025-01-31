package com.challenge.dux.controller;

import com.challenge.dux.model.dto.createteam.CreateTeamRequest;
import com.challenge.dux.model.dto.createteam.CreateTeamResponse;
import com.challenge.dux.model.dto.getallteams.GetAllTeamsResponse;
import com.challenge.dux.model.dto.getteambyid.GetTeamByIdResponse;
import com.challenge.dux.model.dto.searchteambyname.SearchTeamByNameResponse;
import com.challenge.dux.model.dto.updateteam.UpdateTeamRequest;
import com.challenge.dux.model.dto.updateteam.UpdateTeamResponse;
import com.challenge.dux.service.TeamService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Teams", description = "Endpoints for managing football teams")
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(summary = "Get all teams", description = "Returns a list of all registered teams.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of teams found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetAllTeamsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to perform this action")
    })
    @GetMapping
    public ResponseEntity<List<GetAllTeamsResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @Operation(summary = "Get a team by ID", description = "Returns a specific team by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetTeamByIdResponse.class))),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to perform this action")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetTeamByIdResponse> getTeamById(
            @Parameter(description = "ID of the team to search", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @Operation(summary = "Search teams by name", description = "Returns a list of teams that match the given name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teams found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchTeamByNameResponse.class))),
            @ApiResponse(responseCode = "404", description = "No teams found with the given name"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to perform this action")
    })
    @GetMapping("/search")
    public ResponseEntity<List<SearchTeamByNameResponse>> searchTeamsByName(
            @Parameter(description = "Name of the team to search", example = "Real Madrid")
            @RequestParam String name) {
        return ResponseEntity.ok(teamService.searchTeamsByName(name));
    }

    @Operation(summary = "Create a new team", description = "Creates and saves a new team in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTeamResponse.class))),
            @ApiResponse(responseCode = "400", description = "The team already exists or the request is invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to perform this action")
    })
    @PostMapping
    public ResponseEntity<CreateTeamResponse> createTeam(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request containing team details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTeamRequest.class)))
            CreateTeamRequest request) {
        return new ResponseEntity<>(teamService.createTeam(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a team", description = "Updates the details of an existing team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTeamResponse.class))),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to perform this action")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateTeamResponse> updateTeam(
            @Parameter(description = "ID of the team to update", example = "1")
            @PathVariable Long id,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request containing updated team details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTeamRequest.class)))
            UpdateTeamRequest request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }

    @Operation(summary = "Delete a team", description = "Deletes a specific team by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Team successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to perform this action")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(
            @Parameter(description = "ID of the team to delete", example = "1")
            @PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

}
