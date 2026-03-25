package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "ApiError", description = "Standard error payload")
public class ApiErrorResponseDTO {
    @Schema(description = "Error timestamp in ISO format", example = "2026-03-25T14:12:33.120")
    private String timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private Integer status;

    @Schema(description = "HTTP error name", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "Category not found with id: 10")
    private String message;
}
