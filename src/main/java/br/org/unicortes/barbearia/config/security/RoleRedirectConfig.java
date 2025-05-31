package br.org.unicortes.barbearia.config.security;

import br.org.unicortes.barbearia.enums.Roles;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "security.redirect")
public class RoleRedirectConfig {

    private Map<String, String> roles;

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    @PostConstruct
    public void init() {
        roles.forEach((roleName, url) -> {
            Roles role = Roles.valueOf(roleName.toUpperCase());
            role.setRedirectUrl(url);
        });
    }
}
