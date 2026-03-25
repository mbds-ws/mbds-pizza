package mg.pizza.wsrest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.pizza.wsrest.model.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
public class UpdateOrderStatusRequestDTO {
    
    @NotNull(message = "Order status is required")
    private OrderStatus status;
    
}
