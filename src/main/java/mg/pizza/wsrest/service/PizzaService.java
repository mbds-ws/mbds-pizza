package mg.pizza.wsrest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.PizzaRequestDTO;
import mg.pizza.wsrest.dto.PizzaResponseDTO;
import mg.pizza.wsrest.exception.ResourceNotFoundException;
import mg.pizza.wsrest.model.Ingredient;
import mg.pizza.wsrest.model.Pizza;
import mg.pizza.wsrest.model.Category;
import mg.pizza.wsrest.repository.CategoryRepository;
import mg.pizza.wsrest.repository.IngredientRepository;
import mg.pizza.wsrest.repository.PizzaRepository;

@Service
@RequiredArgsConstructor
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;

    public PizzaResponseDTO createPizza(PizzaRequestDTO requestDTO) {
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId()));

        List<Ingredient> ingredients = getIngredientsFromIds(requestDTO.getIngredientIds());

        Pizza pizza = new Pizza();
        pizza.setName(requestDTO.getName());
        pizza.setDescription(requestDTO.getDescription());
        pizza.setPrice(requestDTO.getPrice());
        pizza.setAvailable(requestDTO.getAvailable());
        pizza.setCategory(category);
        pizza.setIngredients(ingredients);

        Pizza savedPizza = pizzaRepository.save(pizza);
        return mapToResponseDTO(savedPizza);
    }

    public List<PizzaResponseDTO> getAllPizzas() {
        return pizzaRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PizzaResponseDTO> getFilteredPizzas(String name, String categoryName, Boolean available) {
        return pizzaRepository.findByFilters(name, categoryName, available)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public PizzaResponseDTO getPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id: " + id));

        return mapToResponseDTO(pizza);
    }

    public PizzaResponseDTO updatePizza(Long id, PizzaRequestDTO requestDTO) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id: " + id));

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDTO.getCategoryId()));

        List<Ingredient> ingredients = getIngredientsFromIds(requestDTO.getIngredientIds());

        pizza.setName(requestDTO.getName());
        pizza.setDescription(requestDTO.getDescription());
        pizza.setPrice(requestDTO.getPrice());
        pizza.setAvailable(requestDTO.getAvailable());
        pizza.setCategory(category);
        pizza.setIngredients(ingredients);

        Pizza updatedPizza = pizzaRepository.save(pizza);
        return mapToResponseDTO(updatedPizza);
    }

    public void deletePizza(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id: " + id));

        pizzaRepository.delete(pizza);
    }

    private List<Ingredient> getIngredientsFromIds(List<Long> ingredientIds) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return new ArrayList<>();
        }

        return ingredientIds.stream()
                .map(ingredientId -> ingredientRepository.findById(ingredientId)
                        .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + ingredientId)))
                .collect(Collectors.toList());
    }

    private PizzaResponseDTO mapToResponseDTO(Pizza pizza) {
        PizzaResponseDTO DTO = new PizzaResponseDTO();
        DTO.setId(pizza.getId());
        DTO.setName(pizza.getName());
        DTO.setDescription(pizza.getDescription());
        DTO.setPrice(pizza.getPrice());
        DTO.setAvailable(pizza.getAvailable());
        DTO.setCategoryId(pizza.getCategory().getId());
        DTO.setCategoryName(pizza.getCategory().getName());
        DTO.setIngredientIds(
                pizza.getIngredients().stream().map(Ingredient::getId).collect(Collectors.toList())
        );
        DTO.setIngredientNames(
                pizza.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())
        );
        return DTO;
    }
}
