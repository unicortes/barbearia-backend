package br.org.unicortes.barbearia.config;

import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = authService.getUsuarioByEmail(userDetails.getUsername());
        String redirectUrl = null;

       switch (usuario.getRole()) {
            case "ADMIN":
                redirectUrl = "/barbeariaUnicortes/admin/home";
                break;
            case "BARBER":
                redirectUrl = "/barbeariaUnicortes/barbeiro/home";
                break;
            case "CLIENT":
                redirectUrl = "/barbeariaUnicortes/cliente/home";
                break;
            default:
                throw new IllegalStateException("User has no valid role assigned!");
        }

        response.sendRedirect(redirectUrl);
    }
}
