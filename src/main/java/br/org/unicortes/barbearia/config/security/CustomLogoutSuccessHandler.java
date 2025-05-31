package br.org.unicortes.barbearia.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    @Value("${app.security.logout.success-url}")
    private String logoutSuccessUrl;

    @Value("${app.security.logout.add-timestamp-param}")
    private boolean addTimestampParam;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        try {
            String targetUrl = buildTargetUrl(request);
            logger.debug("Logout realizado com sucesso. Redirecionando para: {}", targetUrl);
            response.sendRedirect(targetUrl);
        } catch (Exception e) {
            logger.error("Falha no redirecionamento pós-logout", e);
            response.sendRedirect("/error?reason=logout_failed");
        }
    }

    private String buildTargetUrl(HttpServletRequest request) {
        String url = logoutSuccessUrl;

        if (addTimestampParam) {
            url += (url.contains("?") ? "&" : "?");
            url += "t=" + System.currentTimeMillis();
        }

        return url;
    }
}
