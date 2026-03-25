package mg.pizza.wsrest.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequestDTO {
    
    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemRequestDTO> items;
}
