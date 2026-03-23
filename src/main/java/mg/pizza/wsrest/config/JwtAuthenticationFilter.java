package mg.pizza.wsrest.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.pizza.wsrest.service.JwtService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
            || path.startsWith("/h2-console") || path.startsWith("/api/auth");
    }

    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {

        System.out.println("JWT FILTER EXECUTED");
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (!jwtService.isValid(token)) {
                System.out.println("JWT invalide: " + token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT invalide");
                return;
            } 

            // AUTHENTIFICATION SPRING SECURITY
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);
            String authority = (role != null && !role.isBlank()) ? "ROLE_" + role : "ROLE_USER";

            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(
                    email, 
                    null, 
                    List.of(new SimpleGrantedAuthority(authority))
                );

            auth.getAuthorities().forEach(a -> System.out.println("Granted: " + a.getAuthority()));
            
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}
