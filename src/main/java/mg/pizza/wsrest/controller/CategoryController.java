package mg.pizza.wsrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import mg.pizza.wsrest.dto.CategoryRequestDTO;
import mg.pizza.wsrest.dto.CategoryResponseDTO;
import mg.pizza.wsrest.dto.ApiErrorResponseDTO;
import mg.pizza.wsrest.dto.ValidationErrorResponseDTO;
import mg.pizza.wsrest.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "CRUD endpoints for pizza categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
        @Operation(
            summary = "Create category",
            description = "Create a new category (ADMIN only)",
            security = @SecurityRequirement(name = "bearerAuth")
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDto) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(requestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
        @Operation(summary = "List categories", description = "Return all categories", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
        @Operation(summary = "Get category by id", description = "Return one category by its id", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
        public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "Category id", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
        @Operation(
            summary = "Update category",
            description = "Update an existing category (ADMIN only)",
            security = @SecurityRequirement(name = "bearerAuth")
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated",
                content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "Category id", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO requestDto
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
        @Operation(
            summary = "Delete category",
            description = "Delete a category by id (ADMIN only)",
            security = @SecurityRequirement(name = "bearerAuth")
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted",
                content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
        public ResponseEntity<String> deleteCategory(
            @Parameter(description = "Category id", required = true, example = "1")
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
