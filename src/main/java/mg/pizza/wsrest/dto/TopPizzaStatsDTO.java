package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TopPizzaStats", description = "Top-selling pizza statistics")
public class TopPizzaStatsDTO {
    @Schema(description = "Pizza identifier", example = "1")
    private Long pizzaId;

    @Schema(description = "Pizza name", example = "Margherita")
    private String pizzaName;

    @Schema(description = "Total units sold", example = "42")
    private Long totalSold;
}
