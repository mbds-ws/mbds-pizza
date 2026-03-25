package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "IngredientResponse", description = "Ingredient data returned by the API")
public class IngredientResponseDTO {
    @Schema(description = "Ingredient id", example = "4")
    private Long id;

    @Schema(description = "Ingredient name", example = "Mozzarella")
    private String name;
}
