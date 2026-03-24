package mg.pizza.wsrest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.IngredientRequestDTO;
import mg.pizza.wsrest.dto.IngredientResponseDTO;
import mg.pizza.wsrest.service.IngredientService;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientResponseDTO> createIngredient(@Valid @RequestBody IngredientRequestDTO requestDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponseDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponseDTO> getIngredientById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDTO> updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody IngredientRequestDTO requestDto
    ) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
