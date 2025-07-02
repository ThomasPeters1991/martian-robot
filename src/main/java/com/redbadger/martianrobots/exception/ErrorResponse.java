package com.redbadger.martianrobots.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * A record to represent a standardized JSON error response.
 * @param statusCode The HTTP status code.
 * @param message The error message.
 * @param timestamp The time the error occurred.
 */
@Schema(description = "Standardized error response format")
public record ErrorResponse(
        @Schema(description = "HTTP status code", example = "400")
        int statusCode,
        @Schema(description = "A descriptive error message", example = "Invalid grid coordinates format on line 1.")
        String message,
        @Schema(description = "Timestamp of when the error occurred")
        LocalDateTime timestamp) {
}