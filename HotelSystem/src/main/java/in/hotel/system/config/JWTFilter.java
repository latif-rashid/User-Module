package in.hotel.system.config;

import in.hotel.system.entity.User;
import in.hotel.system.repository.UserRepository;
import in.hotel.system.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtservice;
    private final UserRepository userRepository;

    public JWTFilter(JWTService jwtservice, UserRepository userRepository) {
        this.jwtservice = jwtservice;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String tokenVal = token.substring(7);
            try {
                String username = jwtservice.getUsername(tokenVal);
                // Check if the username is already in the session return the username
                Optional<User> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isPresent()) {
                    // Set the authenticated user in the SecurityContext
                    User user = optionalUser.get();
                    // Add additional user details if needed (e.g., roles)
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))

                            );
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                // Log the exception or handle invalid token scenarios
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }
}
