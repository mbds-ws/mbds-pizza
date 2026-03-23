package mg.pizza.wsrest.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mg.pizza.wsrest.model.Role;
import mg.pizza.wsrest.model.User;
import mg.pizza.wsrest.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    UserRepository repo;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public String login(String phone, String password) {
        Optional<User> userOpt = repo.findByPhone(phone);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        boolean validPassword = passwordEncoder.matches(password, user.getPassword())
                || password.equals(user.getPassword());
        if (!validPassword) {
            return null;
        }
       
        return jwtService.generateToken(user.getPhone(), user.getRole());
    }

    public User createUser(String fullname, String phone, String password) {
        if (fullname == null || fullname.isBlank() || phone == null || phone.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("fullname, phone and password are required");
        }

        if (repo.existsByPhone(phone)) {
            throw new IllegalArgumentException("phone already exists");
        }

        User user = new User();
        user.setFullname(fullname.trim());
        user.setPhone(phone.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.CUSTOMER);

        return repo.save(user);
    }
}
