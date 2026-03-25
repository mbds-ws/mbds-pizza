package mg.pizza.wsrest.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "PizzaRequest", description = "Pizza payload for create/update operations")
public class PizzaRequestDTO {
    @NotBlank(message = "Pizza name is required")
    @Schema(description = "Pizza name", example = "Margherita", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Pizza description", example = "Tomato sauce, mozzarella and basil")
    private String description;

    @NotNull(message = "Pizza price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Pizza price must be greater than 0")
    @Schema(description = "Pizza unit price", example = "22000", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @NotNull(message = "Pizza availability is required")
    @Schema(description = "Whether pizza is available", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean available;

    @NotNull(message = "Category id is required")
    @Schema(description = "Category identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @ArraySchema(schema = @Schema(description = "Ingredient id", example = "2"), arraySchema = @Schema(description = "Ingredient id list"))
    private List<Long> ingredientIds;
}
