package mg.pizza.wsrest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.PizzaRequestDTO;
import mg.pizza.wsrest.dto.PizzaResponseDTO;
import mg.pizza.wsrest.service.PizzaService;

@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {
    private final PizzaService pizzaService;

    @PostMapping
    public ResponseEntity<PizzaResponseDTO> createPizza(@Valid @RequestBody PizzaRequestDTO requestDto) {
        return new ResponseEntity<>(pizzaService.createPizza(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PizzaResponseDTO>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.getAllPizzas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaResponseDTO> getPizzaById(@PathVariable Long id) {
        return ResponseEntity.ok(pizzaService.getPizzaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaResponseDTO> updatePizza(
            @PathVariable Long id,
            @Valid @RequestBody PizzaRequestDTO requestDto
    ) {
        return ResponseEntity.ok(pizzaService.updatePizza(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        pizzaService.deletePizza(id);
        return ResponseEntity.noContent().build();
    }
}
