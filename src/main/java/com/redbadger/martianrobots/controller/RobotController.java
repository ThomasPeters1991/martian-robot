package com.redbadger.martianrobots.controller;

import com.redbadger.martianrobots.exception.ErrorResponse;
import com.redbadger.martianrobots.model.SimulationResult;
import com.redbadger.martianrobots.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Martian Robots", description = "Endpoints for simulating robot movements")
public class RobotController {

    private final SimulationService simulationService;
    private static final Logger logger = LoggerFactory.getLogger(RobotController.class);

    public RobotController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Operation(summary = "Run a robot simulation",
            description = "Accepts a plain text block defining the grid and a series of robots with their instructions. " +
                    "It processes all instructions and returns the final state of each robot as a JSON object.")
    @RequestBody(description = "The complete simulation input as a single plain text block. " +
            "The first line must be the grid coordinates (e.g., '5 3'). " +
            "Subsequent pairs of lines must be the robot's starting position (e.g., '1 1 E') " +
            "followed by its instruction string (e.g., 'RFRFRFRF').",
            required = true,
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                    schema = @Schema(type = "string", example = "5 3\n1 1 E\nRFRFRFRF\n3 2 N\nFRRFLLFFRRFLL")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulation successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SimulationResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input format",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/simulate", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimulationResult> simulate(@org.springframework.web.bind.annotation.RequestBody String input) {
        logger.info("Received request for simulation.");
        SimulationResult result = simulationService.process(input);
        return ResponseEntity.ok(result);
    }
}
