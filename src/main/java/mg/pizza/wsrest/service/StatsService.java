package mg.pizza.wsrest.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.RevenueStatsDTO;
import mg.pizza.wsrest.dto.TopPizzaStatsDTO;
import mg.pizza.wsrest.exception.InvalidStatsPeriodException;
import mg.pizza.wsrest.model.OrderStatus;
import mg.pizza.wsrest.model.StatsPeriod;
import mg.pizza.wsrest.repository.OrderItemRepository;
import mg.pizza.wsrest.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public RevenueStatsDTO getRevenueStats(String period) {
        StatsPeriod statsPeriod = parsePeriod(period);
        LocalDateTime[] range = resolvePeriodRange(statsPeriod);

        BigDecimal revenue = orderRepository.calculateRevenue(
                OrderStatus.ANNULEE,
                range[0],
                range[1]
        );

        return new RevenueStatsDTO(statsPeriod.name(), revenue);
    }

    private StatsPeriod parsePeriod(String period) {
        if (period == null || period.isBlank()) {
            return StatsPeriod.ALL;
        }

        try {
            return StatsPeriod.valueOf(period.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidStatsPeriodException(
                    "Invalid stats period: " + period + ". Allowed values: ALL, TODAY, WEEK, MONTH"
            );
        }
    }

    private LocalDateTime[] resolvePeriodRange(StatsPeriod statsPeriod) {
        LocalDate today = LocalDate.now();

        return switch (statsPeriod) {
            case ALL -> new LocalDateTime[]{null, null};

            case TODAY -> new LocalDateTime[]{
                    today.atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            };

            case WEEK -> {
                LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                yield new LocalDateTime[]{
                        startOfWeek.atStartOfDay(),
                        startOfWeek.plusDays(7).atStartOfDay()
                };
            }

            case MONTH -> {
                LocalDate startOfMonth = today.withDayOfMonth(1);
                yield new LocalDateTime[]{
                        startOfMonth.atStartOfDay(),
                        startOfMonth.plusMonths(1).atStartOfDay()
                };
            }
        };
    }

    public List<TopPizzaStatsDTO> getTopPizzasStats(String period) {
        StatsPeriod statsPeriod = parsePeriod(period);
        LocalDateTime[] range = resolvePeriodRange(statsPeriod);

        return orderItemRepository.findTopPizzas(
                OrderStatus.ANNULEE,
                range[0],
                range[1]
        );
    }
}
