package mg.pizza.wsrest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientResponseDTO {
    private Long id;
    private String name;
}
