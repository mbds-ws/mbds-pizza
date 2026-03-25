package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "RegisterRequest", description = "Payload used to create a new customer account")
public class RegisterDTO {
    @Schema(description = "User full name", example = "Rakotonoelina Manitriniaina")
    private String fullname;

    @Schema(description = "Unique phone number", example = "0341234567")
    private String phone;

    @Schema(description = "Raw password before hashing", example = "customer123")
    private String password;
}
