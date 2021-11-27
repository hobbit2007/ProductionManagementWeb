package com.vaadin.tutorial.crm.security;

import com.vaadin.tutorial.crm.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Set;

@RequiredArgsConstructor
public class UserAuthentication implements Authentication, Serializable {
    private final String login;
    @Getter
    private final Set<Authorities> authorities;
    @Getter
    private final User details;
    @Getter
    @Setter
    private boolean authenticated;

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return login;
    }

    @Override
    public String getName() {
        return login;
    }

    public long getId() {
        return details.getUserId();
    }

    public String getRole() {
        return details.getRole();
    }
}
