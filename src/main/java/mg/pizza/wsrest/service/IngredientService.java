package mg.pizza.wsrest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.IngredientRequestDTO;
import mg.pizza.wsrest.dto.IngredientResponseDTO;
import mg.pizza.wsrest.exception.ResourceNotFoundException;
import mg.pizza.wsrest.model.Ingredient;
import mg.pizza.wsrest.repository.IngredientRepository;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientResponseDTO createIngredient(IngredientRequestDTO requestDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(requestDto.getName());

        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return mapToResponseDto(savedIngredient);
    }

    public List<IngredientResponseDTO> getAllIngredients() {
        return ingredientRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public IngredientResponseDTO getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));

        return mapToResponseDto(ingredient);
    }

    public IngredientResponseDTO updateIngredient(Long id, IngredientRequestDTO requestDto) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));

        ingredient.setName(requestDto.getName());

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return mapToResponseDto(updatedIngredient);
    }

    public void deleteIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));

        ingredientRepository.delete(ingredient);
    }

    private IngredientResponseDTO mapToResponseDto(Ingredient ingredient) {
        IngredientResponseDTO dto = new IngredientResponseDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        return dto;
    }
}
