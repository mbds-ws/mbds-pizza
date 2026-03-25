package mg.pizza.wsrest.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Users", description = "Endpoints related to authenticated users")
public class UserController {

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile", description = "Return basic profile data from JWT-authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile returned",
                    content = @Content(mediaType = "application/json", examples =
                            @ExampleObject(value = "{\"phone\":\"0341234567\",\"roles\":[{\"authority\":\"ROLE_ADMIN\"}]}"))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    public Map<String, Object> getProfile(Authentication authentication) {
        return Map.of(
                "phone", authentication.getName(),
                "roles", authentication.getAuthorities());
    }
}
