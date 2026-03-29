package mg.pizza.wsrest.repository;

import mg.pizza.wsrest.model.Order;
import mg.pizza.wsrest.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserPhoneOrderByOrderDateDesc(String phone);

    @Query("""
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
          AND (:startDateTime IS NULL OR o.orderDate >= :startDateTime)
          AND (:endDateTime IS NULL OR o.orderDate < :endDateTime)
        ORDER BY o.orderDate DESC
    """)
    List<Order> findByFilters(
            @Param("status") OrderStatus status,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

     @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.status <> :excludedStatus
          AND (:startDateTime IS NULL OR o.orderDate >= :startDateTime)
          AND (:endDateTime IS NULL OR o.orderDate < :endDateTime)
    """)
    BigDecimal calculateRevenue(
            @Param("excludedStatus") OrderStatus excludedStatus,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}

