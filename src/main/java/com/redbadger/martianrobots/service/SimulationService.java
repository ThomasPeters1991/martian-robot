package com.redbadger.martianrobots.service;

import com.redbadger.martianrobots.exception.InvalidInputException;
import com.redbadger.martianrobots.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimulationService {

    private static final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    /**
     * Processes the entire input string to simulate robot movements.
     * @param input The raw input string containing grid and robot data.
     * @return A SimulationResult object containing the final state of all robots.
     * @throws InvalidInputException if the input format is invalid.
     */
    public SimulationResult process(String input) {
        logger.info("Starting new robot simulation.");
        if (input == null || input.trim().isEmpty()) {
            logger.warn("Simulation input was null or empty.");
            return new SimulationResult(new ArrayList<>());
        }

        List<String> lines = Arrays.stream(input.trim().split("\\r?\\n"))
                .filter(line -> !line.trim().isEmpty())
                .toList();

        if (lines.isEmpty()) {
            logger.warn("Simulation input contained no processable lines.");
            return new SimulationResult(new ArrayList<>());
        }

        Grid grid;
        try {
            String[] gridCoords = lines.get(0).trim().split("\\s+");
            grid = new Grid(Integer.parseInt(gridCoords[0]), Integer.parseInt(gridCoords[1]));
            logger.info("Created grid with max coordinates: ({}, {})", gridCoords[0], gridCoords[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidInputException("Invalid grid coordinates format on line 1.", e);
        }

        List<SimulationResult.RobotState> finalStates = new ArrayList<>();

        for (int i = 1; i < lines.size(); i += 2) {
            Robot robot;
            try {
                String[] positionParts = lines.get(i).trim().split("\\s+");
                robot = new Robot(
                        Integer.parseInt(positionParts[0]),
                        Integer.parseInt(positionParts[1]),
                        Orientation.valueOf(positionParts[2].toUpperCase())
                );
            } catch (Exception e) {
                throw new InvalidInputException("Invalid robot position or orientation on line " + (i + 1) + ".", e);
            }

            if (i + 1 < lines.size()) {
                String instructionString = lines.get(i + 1).trim().toUpperCase();
                logger.debug("Processing robot at ({}, {}) with instructions: {}", robot.getX(), robot.getY(), instructionString);
                for (char instructionChar : instructionString.toCharArray()) {
                    try {
                        Instruction instruction = Instruction.valueOf(String.valueOf(instructionChar));
                        robot.move(instruction, grid);
                        if (robot.isLost()) {
                            break;
                        }
                    } catch (IllegalArgumentException e) {
                        throw new InvalidInputException("Invalid instruction '" + instructionChar + "' on line " + (i + 2) + ".", e);
                    }
                }
            }
            finalStates.add(new SimulationResult.RobotState(robot.getX(), robot.getY(), robot.getOrientation(), robot.isLost()));
        }

        logger.info("Simulation finished. Processed {} robot(s).", finalStates.size());
        return new SimulationResult(finalStates);
    }
}
