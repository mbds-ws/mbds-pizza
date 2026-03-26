package mg.pizza.wsrest.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.CreateOrderRequestDTO;
import mg.pizza.wsrest.dto.OrderItemRequestDTO;
import mg.pizza.wsrest.dto.OrderItemResponseDTO;
import mg.pizza.wsrest.dto.OrderResponseDTO;
import mg.pizza.wsrest.exception.PizzaUnavailableException;
import mg.pizza.wsrest.exception.ResourceNotFoundException;
import mg.pizza.wsrest.model.Order;
import mg.pizza.wsrest.model.OrderItem;
import mg.pizza.wsrest.model.OrderStatus;
import mg.pizza.wsrest.model.Pizza;
import mg.pizza.wsrest.model.User;
import mg.pizza.wsrest.repository.OrderRepository;
import mg.pizza.wsrest.repository.PizzaRepository;
import mg.pizza.wsrest.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;
    private final UserRepository userRepository;

    public OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO, String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with phone: " + phone));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.EN_ATTENTE);

        List<OrderItem> orderItems = requestDTO.getItems().stream()
                .map(itemRequest -> buildOrderItem(itemRequest, order))
                .collect(Collectors.toList());

        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return mapToResponseDTO(order);
    }

    public List<OrderResponseDTO> getMyOrders(String phone) {
        return orderRepository.findByUserPhoneOrderByOrderDateDesc(phone)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private OrderItem buildOrderItem(OrderItemRequestDTO itemRequest, Order order) {
        Pizza pizza = pizzaRepository.findById(itemRequest.getPizzaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pizza not found with id: " + itemRequest.getPizzaId()));

        if(!Boolean.TRUE.equals(pizza.getAvailable())){
            throw new PizzaUnavailableException("Pizza is not available for ordering: "+ pizza.getName());
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setPizza(pizza);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setUnitPrice(pizza.getPrice());

        BigDecimal subTotal = pizza.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
        orderItem.setSubTotal(subTotal);

        return orderItem;
    }

    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setUserId(order.getUser().getId());
        dto.setUserFullname(order.getUser().getFullname());
        dto.setUserPhone(order.getUser().getPhone());

        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream().map(orderItem -> {
            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
            itemDTO.setPizzaId(orderItem.getPizza().getId());
            itemDTO.setPizzaName(orderItem.getPizza().getName());
            itemDTO.setQuantity(orderItem.getQuantity());
            itemDTO.setUnitPrice(orderItem.getUnitPrice());
            itemDTO.setSubTotal(orderItem.getSubTotal());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}
