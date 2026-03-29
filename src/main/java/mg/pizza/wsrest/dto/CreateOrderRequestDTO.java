package mg.pizza.wsrest.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@Schema(name = "CreateOrderRequest", description = "Payload used to create an order")
public class CreateOrderRequestDTO {
    
    @NotEmpty(message = "Order items are required")
    @Valid
    @ArraySchema(arraySchema = @Schema(description = "List of ordered pizza items"))
    private List<OrderItemRequestDTO> items;
}
