package com.redbadger.martianrobots;

import com.redbadger.martianrobots.model.Orientation;
import com.redbadger.martianrobots.model.SimulationResult;
import com.redbadger.martianrobots.service.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationServiceTest {

    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        simulationService = new SimulationService();
    }

    @Test
    void testProcess_withSampleInput_shouldReturnCorrectResultObject() {
        // Given
        String input = "5 3\n" +
                "1 1 E\n" +
                "RFRFRFRF\n" +
                "3 2 N\n" +
                "FRRFLLFFRRFLL\n" +
                "0 3 W\n" +
                "LLFFFLFLFL";

        // When
        SimulationResult result = simulationService.process(input);
        List<SimulationResult.RobotState> states = result.finalStates();

        // Then
        assertNotNull(states);
        assertEquals(3, states.size());

        // Robot 1
        assertEquals(1, states.get(0).x());
        assertEquals(1, states.get(0).y());
        assertEquals(Orientation.E, states.get(0).orientation());
        assertFalse(states.get(0).isLost());

        // Robot 2
        assertEquals(3, states.get(1).x());
        assertEquals(3, states.get(1).y());
        assertEquals(Orientation.N, states.get(1).orientation());
        assertTrue(states.get(1).isLost());

        // Robot 3
        assertEquals(2, states.get(2).x());
        assertEquals(3, states.get(2).y());
        assertEquals(Orientation.S, states.get(2).orientation());
        assertFalse(states.get(2).isLost());
    }
}