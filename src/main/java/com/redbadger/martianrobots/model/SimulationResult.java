package com.redbadger.martianrobots.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "The final result of the robot simulation")
public record SimulationResult(
        @Schema(description = "A list containing the final state of each robot processed in the simulation")
        List<RobotState> finalStates
) {
    /**
     * Represents the final state of a single robot.
     */
    @Schema(description = "The final position, orientation, and status of a single robot")
    public record RobotState(
            @Schema(description = "Final X coordinate", example = "1")
            int x,
            @Schema(description = "Final Y coordinate", example = "1")
            int y,
            @Schema(description = "Final orientation", example = "E")
            Orientation orientation,
            @Schema(description = "Indicates if the robot was lost during the simulation", example = "true")
            boolean isLost
    ) {}
}
