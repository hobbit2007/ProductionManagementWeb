package com.vaadin.tutorial.crm.security;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Authorities implements GrantedAuthority {
    USER,
    MANAGER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

    public static Set<Authorities> parse(String a) {
        return new HashSet<>(Arrays.asList(values()).subList(0, index(findEnum(a)) + 1));
    }

    @NonNull
    private static Authorities findEnum(String a) {
        String name = a.replace("ROLE_", "");
        for (Authorities value : values()) {
            if (value.name().equalsIgnoreCase(name))
                return value;
        }
        throw new IllegalArgumentException(a);
    }

    @NonNull
    private static int index(Authorities a) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i] == a)
                return i;
        }
        throw new Error();
    }
}
