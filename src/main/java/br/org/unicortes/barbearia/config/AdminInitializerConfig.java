package br.org.unicortes.barbearia.config;

import br.org.unicortes.barbearia.models.Usuario;
import br.org.unicortes.barbearia.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializerConfig implements CommandLineRunner {

    @Autowired
    private AuthService authService;


    @Override
    public void run(String... args) throws Exception {
        if(this.authService.getUsuarioByEmail("admin@admin.com") == null){
            Usuario admin = new Usuario();
            admin.setEmail("admin@admin.com");
            admin.setPassword("admin");
            admin.setName("Admin");
            admin.setRole("ADMIN");

            this.authService.createUser(admin);
        }
    }
}
