package mg.pizza.wsrest.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "ValidationError", description = "Validation error payload with per-field messages")
public class ValidationErrorResponseDTO {
    @Schema(description = "Error timestamp in ISO format", example = "2026-03-25T14:12:33.120")
    private String timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private Integer status;

    @Schema(description = "Error type", example = "Validation Error")
    private String error;

    @Schema(description = "Validation messages by field", example = "{\"name\":\"Category name is required\"}")
    private Map<String, String> messages;
}
