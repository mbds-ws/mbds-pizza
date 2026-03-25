 package mg.pizza.wsrest.controller;

import java.util.List;

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
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.IngredientRequestDTO;
import mg.pizza.wsrest.dto.IngredientResponseDTO;
import mg.pizza.wsrest.dto.ApiErrorResponseDTO;
import mg.pizza.wsrest.dto.ValidationErrorResponseDTO;
import mg.pizza.wsrest.service.IngredientService;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@Tag(name = "Ingredients", description = "CRUD endpoints for pizza ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
        @Operation(summary = "Create ingredient", description = "Create an ingredient (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingredient created",
                content = @Content(schema = @Schema(implementation = IngredientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<IngredientResponseDTO> createIngredient(@Valid @RequestBody IngredientRequestDTO requestDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
        @Operation(summary = "List ingredients", description = "Return all ingredients", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredients found",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = IngredientResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<List<IngredientResponseDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
        @Operation(summary = "Get ingredient by id", description = "Return one ingredient by id", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient found",
                content = @Content(schema = @Schema(implementation = IngredientResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
        public ResponseEntity<IngredientResponseDTO> getIngredientById(
            @Parameter(description = "Ingredient id", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
        @Operation(summary = "Update ingredient", description = "Update an ingredient by id (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient updated",
                content = @Content(schema = @Schema(implementation = IngredientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<IngredientResponseDTO> updateIngredient(
            @Parameter(description = "Ingredient id", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody IngredientRequestDTO requestDto
    ) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
        @Operation(summary = "Delete ingredient", description = "Delete an ingredient by id (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ingredient deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
        public ResponseEntity<Void> deleteIngredient(
            @Parameter(description = "Ingredient id", required = true, example = "1")
            @PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
