package mg.pizza.wsrest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "MBDS Pizza API",
        version = "v1",
        description = "REST API for authentication and pizza catalog management (categories, ingredients, pizzas).",
        contact = @Contact(name = "MBDS Team"),
        license = @License(name = "Internal Use")
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Provide the JWT token obtained from /api/auth/login"
)
public class OpenApiConfig {
}
