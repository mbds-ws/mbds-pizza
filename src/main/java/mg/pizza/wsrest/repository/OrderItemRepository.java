package mg.pizza.wsrest.repository;

import mg.pizza.wsrest.dto.TopPizzaStatsDTO;
import mg.pizza.wsrest.model.OrderItem;
import mg.pizza.wsrest.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
        SELECT new mg.pizza.wsrest.dto.TopPizzaStatsDTO(
            oi.pizza.id,
            oi.pizza.name,
            SUM(oi.quantity)
        )
        FROM OrderItem oi
        WHERE oi.order.status <> :excludedStatus
          AND (:startDateTime IS NULL OR oi.order.orderDate >= :startDateTime)
          AND (:endDateTime IS NULL OR oi.order.orderDate < :endDateTime)
        GROUP BY oi.pizza.id, oi.pizza.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<TopPizzaStatsDTO> findTopPizzas(
            @Param("excludedStatus") OrderStatus excludedStatus,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
