package mg.pizza.wsrest.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class OrderItemResponseDTO {
    private Long pizzaId;
    private String pizzaName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
}
