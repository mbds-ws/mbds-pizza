package mg.pizza.wsrest.config;

import java.util.List;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Provide the JWT token obtained from /api/auth/login"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MBDS Pizza API")
                        .version("v1")
                        .description("REST API for authentication, pizza catalog management (categories, ingredients, pizzas), orders and statistics.")
                        .contact(new Contact().name("MBDS Team"))
                        .license(new License().name("Internal Use")))
                .servers(List.of(new Server()
                    .url("/")
                    .description("Current host")));
    }
}
