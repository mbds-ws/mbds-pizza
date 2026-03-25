package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryResponse", description = "Category data returned by the API")
public class CategoryResponseDTO {
    @Schema(description = "Category id", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Classic")
    private String name;

    @Schema(description = "Category description", example = "Classic pizza recipes")
    private String description;
}
