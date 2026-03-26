package mg.pizza.wsrest.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.LoginDTO;
import mg.pizza.wsrest.dto.RegisterDTO;
import mg.pizza.wsrest.dto.ValidationErrorResponseDTO;
import mg.pizza.wsrest.service.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Public endpoints for login and customer registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
        @Operation(summary = "Login", description = "Authenticate with phone and password to receive a JWT token")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login result",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = "{\"token\":\"eyJhbGciOiJIUzI1NiJ9...\"}"),
                    @ExampleObject(name = "Invalid credentials", value = "{\"message\":\"invalid login\"}")
                })),
            @ApiResponse(responseCode = "400", description = "Malformed request",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ValidationErrorResponseDTO.class)))
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Login credentials",
            content = @Content(schema = @Schema(implementation = LoginDTO.class)))
    public Map<String, String> login(@RequestBody LoginDTO dto) {
        String token = authService.login(dto.getPhone(), dto.getPassword());
        if (token == null) {
            return Map.of("message", "invalid login");
        }
        return Map.of("token", token);
    }

    @PostMapping("/register")
        @Operation(summary = "Register", description = "Create a new customer account and return a JWT token")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration result",
                content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Success", value = "{\"message\":\"user created\",\"role\":\"CUSTOMER\",\"token\":\"eyJhbGciOiJIUzI1NiJ9...\"}"),
                    @ExampleObject(name = "Business error", value = "{\"message\":\"phone already exists\"}")
                })),
            @ApiResponse(responseCode = "400", description = "Malformed request",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ValidationErrorResponseDTO.class)))
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "New user payload",
            content = @Content(schema = @Schema(implementation = RegisterDTO.class)))
    public Map<String, String> register(@RequestBody RegisterDTO dto) {
        try {
            authService.createUser(dto.getFullname(), dto.getPhone(), dto.getPassword());
            String token = authService.login(dto.getPhone(), dto.getPassword());
            return Map.of(
                    "message", "user created",
                    "role", "CUSTOMER",
                    "token", token);
        } catch (IllegalArgumentException e) {
            return Map.of("message", e.getMessage());
        }
    }
}
