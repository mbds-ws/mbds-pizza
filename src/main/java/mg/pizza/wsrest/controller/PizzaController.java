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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.ApiErrorResponseDTO;
import mg.pizza.wsrest.dto.PizzaRequestDTO;
import mg.pizza.wsrest.dto.PizzaResponseDTO;
import mg.pizza.wsrest.dto.ValidationErrorResponseDTO;
import mg.pizza.wsrest.service.PizzaService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
@Tag(name = "Pizzas", description = "CRUD endpoints for pizzas")
public class PizzaController {
    private final PizzaService pizzaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create pizza", description = "Create a pizza (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pizza created",
            content = @Content(schema = @Schema(implementation = PizzaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<PizzaResponseDTO> createPizza(@Valid @RequestBody PizzaRequestDTO requestDto) {
        return new ResponseEntity<>(pizzaService.createPizza(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List and filter pizzas",
        description = "Return pizzas filtered by name, category name and availability",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pizzas found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO.class)))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<PizzaResponseDTO>>> getAllPizzas(
            @Parameter(description = "Filter by pizza name (partial match)", example = "Margherita")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filter by category name (partial match)", example = "Classic")
            @RequestParam(required = false) String categoryName,
            @Parameter(description = "Filter by availability", example = "true")
            @RequestParam(required = false) Boolean available
    ) {
        List<EntityModel<PizzaResponseDTO>> pizzas = pizzaService.getFilteredPizzas(name, categoryName, available)
                .stream()
                .map(pizza -> EntityModel.of(
                        pizza,
                        linkTo(methodOn(PizzaController.class).getPizzaById(pizza.getId())).withSelfRel(),
                        linkTo(methodOn(CategoryController.class).getCategoryById(pizza.getCategoryId())).withRel("category")
                ))
                .toList();

        CollectionModel<EntityModel<PizzaResponseDTO>> collectionModel = CollectionModel.of(
                pizzas,
                linkTo(methodOn(PizzaController.class).getAllPizzas(name, categoryName, available)).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get pizza by id",
        description = "Return one pizza by its id",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pizza found",
            content = @Content(schema = @Schema(implementation = PizzaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pizza not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<EntityModel<PizzaResponseDTO>> getPizzaById(
            @Parameter(description = "Pizza id", required = true, example = "1")
            @PathVariable Long id) {

        PizzaResponseDTO pizza = pizzaService.getPizzaById(id);

        EntityModel<PizzaResponseDTO> pizzaModel = EntityModel.of(
                pizza,
                linkTo(methodOn(PizzaController.class).getPizzaById(id)).withSelfRel(),
                linkTo(methodOn(PizzaController.class).getAllPizzas(null, null, null)).withRel("all-pizzas"),
                linkTo(methodOn(CategoryController.class).getCategoryById(pizza.getCategoryId())).withRel("category")
        );

        return ResponseEntity.ok(pizzaModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update pizza", description = "Update a pizza by id (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pizza updated",
            content = @Content(schema = @Schema(implementation = PizzaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pizza not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<PizzaResponseDTO> updatePizza(
        @Parameter(description = "Pizza id", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PizzaRequestDTO requestDto) {
        return ResponseEntity.ok(pizzaService.updatePizza(id, requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pizza", description = "Delete a pizza by id (ADMIN only)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pizza deleted"),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-admin",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pizza not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<Void> deletePizza(
        @Parameter(description = "Pizza id", required = true, example = "1")
        @PathVariable Long id) {
        pizzaService.deletePizza(id);
        return ResponseEntity.noContent().build();
    }
}
