package mg.pizza.wsrest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import mg.pizza.wsrest.dto.CategoryResponseDTO;
import mg.pizza.wsrest.exception.ResourceNotFoundException;
import mg.pizza.wsrest.dto.CategoryRequestDTO;
import mg.pizza.wsrest.repository.CategoryRepository;
import mg.pizza.wsrest.model.Category;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDto) {
        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDto(savedCategory);
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id : " + id));

        return mapToResponseDto(category);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTOCategoryResponseDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id : " + id));

        category.setName(requestDTOCategoryResponseDTO.getName());
        category.setDescription(requestDTOCategoryResponseDTO.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDto(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id : " + id));

        categoryRepository.delete(category);
    }

    private CategoryResponseDTO mapToResponseDto(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
