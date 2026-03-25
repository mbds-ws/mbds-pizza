package mg.pizza.wsrest.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "PizzaResponse", description = "Pizza data returned by the API")
public class PizzaResponseDTO {
    @Schema(description = "Pizza id", example = "1")
    private Long id;

    @Schema(description = "Pizza name", example = "Margherita")
    private String name;

    @Schema(description = "Pizza description", example = "Tomato sauce, mozzarella and basil")
    private String description;

    @Schema(description = "Pizza unit price", example = "22000")
    private BigDecimal price;

    @Schema(description = "Whether pizza is available", example = "true")
    private Boolean available;

    @Schema(description = "Category id", example = "1")
    private Long categoryId;

    @Schema(description = "Category name", example = "Classic")
    private String categoryName;

    @ArraySchema(schema = @Schema(description = "Ingredient id", example = "2"), arraySchema = @Schema(description = "Ingredient id list"))
    private List<Long> ingredientIds;

    @ArraySchema(schema = @Schema(description = "Ingredient name", example = "Mozzarella"), arraySchema = @Schema(description = "Ingredient name list"))
    private List<String> ingredientNames;
}
