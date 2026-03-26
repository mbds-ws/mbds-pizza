package mg.pizza.wsrest.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.CreateOrderRequestDTO;
import mg.pizza.wsrest.dto.OrderResponseDTO;
import mg.pizza.wsrest.dto.UpdateOrderStatusRequestDTO;
import mg.pizza.wsrest.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints for creating and managing customer orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates an order for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order payload"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO requestDTO,
            Principal principal
    ) {
        OrderResponseDTO createdOrder = orderService.createOrder(requestDTO, principal.getName());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Get order by id", description = "Returns the details of a specific order")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        })
        public ResponseEntity<OrderResponseDTO> getOrderById(
            @Parameter(description = "Order identifier", example = "1") @PathVariable Long id
        ) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/my-orders")
        @Operation(summary = "Get my orders", description = "Returns all orders for the authenticated user")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
        })
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal.getName()));
    }

    @PatchMapping("/{id}/status")
        @Operation(summary = "Update order status", description = "Updates the status of an existing order")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @Parameter(description = "Order identifier", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, requestDTO.getStatus()));
    }
}
