package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "CategoryRequest", description = "Category payload for create/update operations")
public class CategoryRequestDTO {
    @NotBlank(message = "Category name is required")
    @Schema(description = "Category name", example = "Classic", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Category description", example = "Classic pizza recipes")
    private String description;
}
