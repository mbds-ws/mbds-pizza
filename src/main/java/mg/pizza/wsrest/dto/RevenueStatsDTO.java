package mg.pizza.wsrest.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RevenueStats", description = "Revenue statistics for a given period")
public class RevenueStatsDTO {
    @Schema(description = "Period used for the query", example = "MONTH", allowableValues = {"ALL", "TODAY", "WEEK", "MONTH"})
    private String period;

    @Schema(description = "Total revenue for the period", example = "1250000")
    private BigDecimal totalRevenue;
}
