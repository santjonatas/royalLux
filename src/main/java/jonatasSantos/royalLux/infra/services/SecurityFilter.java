package jonatasSantos.royalLux.infra.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jonatasSantos.royalLux.core.application.contracts.services.AuthenticationService;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    AuthenticationService authenticationServiceImpl;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = authenticationServiceImpl.validateToken(token);

        if(login != null){
            User user = userRepository.findByUsername(login).orElseThrow(() -> new RuntimeException("User Not Found"));
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}