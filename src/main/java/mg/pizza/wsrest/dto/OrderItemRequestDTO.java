package mg.pizza.wsrest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequestDTO {

    @NotNull(message = "Pizza id is required")
    private Long pizzaId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message="Quantity must be at least 1"    )
    private Integer quantity;
    
}
