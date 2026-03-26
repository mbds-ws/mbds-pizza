package mg.pizza.wsrest.repository;

import mg.pizza.wsrest.model.Pizza;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    @Query("""
        SELECT p FROM Pizza p
        WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:categoryName IS NULL OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :categoryName, '%')))
          AND (:available IS NULL OR p.available = :available)
    """)
    List<Pizza> findByFilters(
            @Param("name") String name,
            @Param("categoryName") String categoryName,
            @Param("available") Boolean available
    );
}
