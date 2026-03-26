package mg.pizza.wsrest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mg.pizza.wsrest.dto.LoginDTO;
import mg.pizza.wsrest.dto.RegisterDTO;
import mg.pizza.wsrest.service.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO dto) {
        String token = authService.login(dto.getPhone(), dto.getPassword());
        if (token == null) {
            return Map.of("message", "invalid login");
        }
        return Map.of("token", token);
    }

    @PostMapping("/register")
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
