package mg.pizza.wsrest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "LoginRequest", description = "Credentials used for authentication")
public class LoginDTO {
    @Schema(description = "Phone number used as login", example = "0341234567", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone; 

    @Schema(description = "User password", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
