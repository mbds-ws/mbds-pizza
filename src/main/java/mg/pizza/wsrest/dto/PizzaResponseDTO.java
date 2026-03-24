package mg.pizza.wsrest.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PizzaResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
    private Long categoryId;
    private String categoryName;
    private List<Long> ingredientIds;
    private List<String> ingredientNames;
}
