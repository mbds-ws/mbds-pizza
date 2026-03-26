package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.pizza.wsrest.model.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payload used to update order status")
public class UpdateOrderStatusRequestDTO {
    
    @NotNull(message = "Order status is required")
    @Schema(
            description = "Target order status",
            example = "EN_PREPARATION",
            allowableValues = {"EN_ATTENTE", "EN_PREPARATION", "LIVREE", "ANNULEE"}
    )
    private OrderStatus status;
    
}
