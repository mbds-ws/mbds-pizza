package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "IngredientRequest", description = "Ingredient payload for create/update operations")
public class IngredientRequestDTO {

    @NotBlank(message = "Ingredient name is required")
    @Schema(description = "Ingredient name", example = "Mozzarella", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
