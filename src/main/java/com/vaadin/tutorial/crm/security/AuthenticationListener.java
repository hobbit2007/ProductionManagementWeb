package com.vaadin.tutorial.crm.security;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

public interface AuthenticationListener {
    void listenAuthenticationSuccess(@NonNull Authentication authentication);
}
