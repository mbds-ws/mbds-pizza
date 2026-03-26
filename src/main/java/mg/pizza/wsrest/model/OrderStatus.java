package mg.pizza.wsrest.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lifecycle states for an order")
public enum OrderStatus {
    EN_ATTENTE,
    EN_PREPARATION,
    LIVREE,
    ANNULEE
}
