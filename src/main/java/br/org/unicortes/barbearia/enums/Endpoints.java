package br.org.unicortes.barbearia.enums;

import java.util.stream.Stream;

public enum Endpoints {
    PUBLIC_API_LOYALTY("/loyalty-cards/**"),
    PUBLIC_API_PROMOTIONS("/promotions/**"),
    PUBLIC_API_BARBER("/barber/**"),
    PUBLIC_API_SERVICES("/servicos/**"),
    PUBLIC_API_AVAILABLE_TIMES("/available-times/**"),
    PUBLIC_API_APPOINTMENTS_AVAILABLE("/appointments/available/**"),
    HOME("/home"),
    SWAGGER_UI(
            "/api-docs/**",
            "/api-docs",
            "/api-docs.json",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    ),

    ADMIN_REGISTER("/register"),
    ADMIN_STOCKS("/stocks/**"),

    AUTH_LOGIN("/auth/login"),
    AUTH_LOGOUT("/auth/logout"),

    STATIC_RESOURCES("/assets/**", "/css/**", "/js/**");

    private final String[] patterns;

    Endpoints(String... patterns) {
        this.patterns = patterns;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public static String[] getPublicEndpoints() {
        return Stream.of(
                        PUBLIC_API_LOYALTY,
                        PUBLIC_API_PROMOTIONS,
                        PUBLIC_API_BARBER,
                        PUBLIC_API_SERVICES,
                        PUBLIC_API_AVAILABLE_TIMES,
                        PUBLIC_API_APPOINTMENTS_AVAILABLE,
                        HOME,
                        SWAGGER_UI,
                        STATIC_RESOURCES,
                        AUTH_LOGIN,
                        AUTH_LOGOUT
                ).flatMap(e -> Stream.of(e.getPatterns()))
                .toArray(String[]::new);
    }

    public static String[] getAdminEndpoints() {
        return Stream.of(
                        ADMIN_REGISTER,
                        ADMIN_STOCKS
                ).flatMap(e -> Stream.of(e.getPatterns()))
                .toArray(String[]::new);
    }
}
