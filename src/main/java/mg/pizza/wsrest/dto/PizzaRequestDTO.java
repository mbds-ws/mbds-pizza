package mg.pizza.wsrest.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class PizzaRequestDTO {
    @NotBlank(message = "Pizza name is required")
    private String name;

    private String description;

    @NotNull(message = "Pizza price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Pizza price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Pizza availability is required")
    private Boolean available;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private List<Long> ingredientIds;
}
