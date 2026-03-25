package mg.pizza.wsrest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import mg.pizza.wsrest.model.OrderStatus;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;

    private Long userId;
    private String userFullname;
    private String userPhone;

    private List<OrderItemResponseDTO> items;
}
