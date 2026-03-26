package mg.pizza.wsrest.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "A single order item in an order response")
public class OrderItemResponseDTO {
    @Schema(description = "Identifier of the ordered pizza", example = "1")
    private Long pizzaId;

    @Schema(description = "Name of the ordered pizza", example = "Runner Pizza")
    private String pizzaName;

    @Schema(description = "Ordered quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price of the pizza", example = "25000")
    private BigDecimal unitPrice;

    @Schema(description = "Subtotal for the item", example = "50000")
    private BigDecimal subTotal;
}
