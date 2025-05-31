package br.org.unicortes.barbearia.config.core;

import br.org.unicortes.barbearia.enums.Roles;
import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AdminInitializerConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializerConfig.class);

    @Autowired
    private AuthService authService;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name:ADMIN}")
    private String adminName;

    @Override
    public void run(String... args) {
        try {
            if (authService.getUsuarioByEmail(adminEmail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail(adminEmail);
                admin.setPassword(adminPassword);
                admin.setName(adminName);
                admin.setRole(Roles.ADMIN);

                authService.createUser(admin);
                logger.info("Admin user created successfully");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize admin user", e);
        }
    }
}
