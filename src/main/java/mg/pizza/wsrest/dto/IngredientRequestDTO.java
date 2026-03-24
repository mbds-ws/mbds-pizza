package mg.pizza.wsrest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientRequestDTO {

    @NotBlank(message = "Ingredient name is required")
    private String name;
}
