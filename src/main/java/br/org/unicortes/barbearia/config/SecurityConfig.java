package br.org.unicortes.barbearia.config;

import br.org.unicortes.barbearia.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/admin/**", "/register/**").hasRole("ADMIN")
                        .requestMatchers("/barbeiro/**").hasRole("BARBER")
                        .requestMatchers("/cliente/**").hasRole("CLIENT")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/api/promocoes", true)// Especifique sua própria página de login se desejar
                        .permitAll()
                )

                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }

    /*@Bean
    public UserDetailsService clientDetailsService() {

        UserDetails admin = User.builder()
                .username("admin")
                .password("$2a$12$RW1lyC0rdNaLDaT13dtJIezP8XStjZFGivxZpW47hGKOJyvNz3j.G")
                .roles("ADMIN", "CLIENT", "BARBER")
                .build();

        System.out.println(admin);

        return new InMemoryUserDetailsManager(admin);
    }*/


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


}
