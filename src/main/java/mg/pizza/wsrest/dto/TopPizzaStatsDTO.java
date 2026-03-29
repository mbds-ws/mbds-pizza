package mg.pizza.wsrest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopPizzaStatsDTO {
    private Long pizzaId;
    private String pizzaName;
    private Long totalSold;
}
