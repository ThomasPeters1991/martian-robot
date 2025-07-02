package com.redbadger.martianrobots;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RobotControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void simulate_withValidInput_shouldReturnOkAndCorrectJson() throws Exception {
        // Given
        String input = "5 3\n" +
                "1 1 E\n" +
                "RFRFRFRF\n" +
                "3 2 N\n" +
                "FRRFLLFFRRFLL\n" +
                "0 3 W\n" +
                "LLFFFLFLFL";

        // When & Then
        mockMvc.perform(post("/simulate")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.finalStates").isArray())
                .andExpect(jsonPath("$.finalStates.length()").value(3))
                // Robot 1
                .andExpect(jsonPath("$.finalStates[0].x").value(1))
                .andExpect(jsonPath("$.finalStates[0].y").value(1))
                .andExpect(jsonPath("$.finalStates[0].orientation").value("E"))
                .andExpect(jsonPath("$.finalStates[0].isLost").value(false))
                // Robot 2
                .andExpect(jsonPath("$.finalStates[1].x").value(3))
                .andExpect(jsonPath("$.finalStates[1].y").value(3))
                .andExpect(jsonPath("$.finalStates[1].orientation").value("N"))
                .andExpect(jsonPath("$.finalStates[1].isLost").value(true))
                // Robot 3
                .andExpect(jsonPath("$.finalStates[2].x").value(2))
                .andExpect(jsonPath("$.finalStates[2].y").value(3))
                .andExpect(jsonPath("$.finalStates[2].orientation").value("S"))
                .andExpect(jsonPath("$.finalStates[2].isLost").value(false));
    }

    @Test
    void simulate_withInvalidInput_shouldReturnBadRequestAsJson() throws Exception {
        // Given
        String invalidInput = "5 X\n" + // Invalid coordinate
                "1 1 E\n" +
                "RFRFRFRF";

        // When & Then
        mockMvc.perform(post("/simulate")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(invalidInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

}
