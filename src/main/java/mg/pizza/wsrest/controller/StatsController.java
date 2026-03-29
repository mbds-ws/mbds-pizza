package mg.pizza.wsrest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.ApiErrorResponseDTO;
import mg.pizza.wsrest.dto.RevenueStatsDTO;
import mg.pizza.wsrest.dto.TopPizzaStatsDTO;
import mg.pizza.wsrest.service.StatsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Endpoints for revenue and top-selling pizza statistics")
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/revenue")
    @Operation(
        summary = "Get revenue stats by period",
        description = "Return revenue statistics filtered by period: ALL, TODAY, WEEK or MONTH",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenue stats found",
            content = @Content(schema = @Schema(implementation = RevenueStatsDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid stats period",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<RevenueStatsDTO> getRevenueStats(
            @Parameter(description = "Stats period", example = "ALL", schema = @Schema(allowableValues = {"ALL", "TODAY", "WEEK", "MONTH"}))
            @RequestParam(required = false, defaultValue = "ALL") String period
    ) {
        return ResponseEntity.ok(statsService.getRevenueStats(period));
    }

    @GetMapping("/top-pizzas")
    @Operation(
        summary = "Get top pizzas by period",
        description = "Return top selling pizzas filtered by period: ALL, TODAY, WEEK or MONTH",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top pizzas found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopPizzaStatsDTO.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid stats period",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<List<TopPizzaStatsDTO>> getTopPizzasStats(
            @Parameter(description = "Stats period", example = "ALL", schema = @Schema(allowableValues = {"ALL", "TODAY", "WEEK", "MONTH"}))
            @RequestParam(required = false, defaultValue = "ALL") String period
    ) {
        return ResponseEntity.ok(statsService.getTopPizzasStats(period));
    }
}
