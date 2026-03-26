package mg.pizza.wsrest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import mg.pizza.wsrest.model.OrderStatus;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Order response returned by the API")
public class OrderResponseDTO {
    @Schema(description = "Order identifier", example = "1")
    private Long id;

    @Schema(description = "Timestamp when the order was created", example = "2026-03-26T10:30:00")
    private LocalDateTime orderDate;

    @Schema(
            description = "Current order status",
            example = "EN_ATTENTE",
            allowableValues = {"EN_ATTENTE", "EN_PREPARATION", "LIVREE", "ANNULEE"}
    )
    private OrderStatus status;

    @Schema(description = "Total amount of the order", example = "50000")
    private BigDecimal totalAmount;

    @Schema(description = "Identifier of the user who placed the order", example = "1")
    private Long userId;

    @Schema(description = "Full name of the user who placed the order", example = "John Doe")
    private String userFullname;

    @Schema(description = "Phone number of the user who placed the order", example = "+261000000000")
    private String userPhone;

    @ArraySchema(arraySchema = @Schema(description = "List of ordered items"))
    private List<OrderItemResponseDTO> items;
}
