package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "A single pizza item within an order")
public class OrderItemRequestDTO {

    @NotNull(message = "Pizza id is required")
    @Schema(description = "Identifier of the pizza to order", example = "1")
    private Long pizzaId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message="Quantity must be at least 1"    )
    @Schema(description = "Number of pizza units", example = "2", minimum = "1")
    private Integer quantity;
    
}
