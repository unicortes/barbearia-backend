package br.org.unicortes.barbearia.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    BARBER("ROLE_BARBER"),
    CLIENT("ROLE_CLIENT");

    private final String authority;
    private String redirectUrl;

    Roles(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public static Roles fromAuthority(String authority) {
        for (Roles role : values()) {
            if (role.authority.equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role authority: " + authority);
    }
}