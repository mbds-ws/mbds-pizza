package mg.pizza.wsrest.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.ApiErrorResponseDTO;
import mg.pizza.wsrest.dto.CreateOrderRequestDTO;
import mg.pizza.wsrest.dto.OrderResponseDTO;
import mg.pizza.wsrest.dto.UpdateOrderStatusRequestDTO;
import mg.pizza.wsrest.model.OrderStatus;
import mg.pizza.wsrest.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints for creating and managing customer orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Create an order for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order payload",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO requestDTO,
            Principal principal
    ) {
        OrderResponseDTO createdOrder = orderService.createOrder(requestDTO, principal.getName());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "List and filter orders",
        description = "Return orders filtered by status, exact date, or date range",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(
            @Parameter(description = "Filter by order status", example = "EN_ATTENTE")
            @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Filter by exact date (ISO format)", example = "2026-03-29")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Start of date range (ISO format)", example = "2026-03-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End of date range (ISO format)", example = "2026-03-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(orderService.getFilteredOrders(status, date, startDate, endDate));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Return a specific order by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found",
            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<EntityModel<OrderResponseDTO>> getOrderById(
            @Parameter(description = "Order identifier", example = "1") @PathVariable Long id
    ) {
        OrderResponseDTO order = orderService.getOrderById(id);

        EntityModel<OrderResponseDTO> orderModel = EntityModel.of(
                order,
                linkTo(methodOn(OrderController.class).getOrderById(id)).withSelfRel(),
                linkTo(methodOn(OrderController.class).getMyOrders(null)).withRel("my-orders"),
                linkTo(methodOn(OrderController.class).getAllOrders(null, null, null, null)).withRel("all-orders")
        );

        return ResponseEntity.ok(orderModel);
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Get my orders", description = "Return all orders for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))),
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
            content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<OrderResponseDTO>>> getMyOrders(Principal principal) {
        List<EntityModel<OrderResponseDTO>> orders = orderService.getMyOrders(principal.getName())
                .stream()
                .map(order -> EntityModel.of(
                        order,
                        linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel()
                ))
                .toList();

        CollectionModel<EntityModel<OrderResponseDTO>> collectionModel = CollectionModel.of(
                orders,
                linkTo(methodOn(OrderController.class).getMyOrders(principal)).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @PatchMapping("/{id}/status")
        @Operation(summary = "Update order status", description = "Update the status of an existing order")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated",
                content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                content = @Content(schema = @Schema(implementation = ApiErrorResponseDTO.class)))
        })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @Parameter(description = "Order identifier", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, requestDTO.getStatus()));
    }
}
