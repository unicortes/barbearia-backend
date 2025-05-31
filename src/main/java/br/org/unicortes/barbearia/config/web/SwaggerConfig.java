package br.org.unicortes.barbearia.config.web;

import br.org.unicortes.barbearia.enums.Endpoints;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Barbearia Unicortes API")
                        .description("Sistema de gestão para barbearias")
                        .version("v5.0.0")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@barbeariaunicortes.com.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-access")
                .pathsToMatch(getPathsFromEnum(
                        Endpoints.PUBLIC_API_LOYALTY,
                        Endpoints.PUBLIC_API_PROMOTIONS,
                        Endpoints.PUBLIC_API_BARBER,
                        Endpoints.PUBLIC_API_SERVICES,
                        Endpoints.PUBLIC_API_AVAILABLE_TIMES,
                        Endpoints.PUBLIC_API_APPOINTMENTS_AVAILABLE,
                        Endpoints.HOME,
                        Endpoints.SWAGGER_UI,
                        Endpoints.STATIC_RESOURCES
                ))
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-access")
                .pathsToMatch(getPathsFromEnum(
                        Endpoints.ADMIN_REGISTER,
                        Endpoints.ADMIN_STOCKS
                ))
                .addOpenApiCustomizer(openApi -> openApi
                        .addSecurityItem(new SecurityRequirement()
                                .addList("bearer-key")))
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("authentication")
                .pathsToMatch(getPathsFromEnum(
                        Endpoints.AUTH_LOGIN,
                        Endpoints.AUTH_LOGOUT
                ))
                .build();
    }

    private String[] getPathsFromEnum(Endpoints... endpoints) {
        return Arrays.stream(endpoints)
                .flatMap(e -> Stream.of(e.getPatterns()))
                .toArray(String[]::new);
    }
}
