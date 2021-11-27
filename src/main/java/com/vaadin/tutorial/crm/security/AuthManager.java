package com.vaadin.tutorial.crm.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.login.LoginView;
import com.vaadin.tutorial.crm.ui.MainView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthManager {
    private final com.vaadin.tutorial.crm.security.LdapAuthenticationProvider ldapAuthenticationProvider;
    private final List<com.vaadin.tutorial.crm.security.AuthenticationListener> listeners;
    @Autowired(required = false)
    private AuthenticationEventPublisher authenticationEventPublisher;


    public void login(String login, String passwd) {
        UsernamePasswordAuthenticationToken baseAuth = new UsernamePasswordAuthenticationToken(login, passwd);

        try {
            Authentication auth = ldapAuthenticationProvider.authenticate(baseAuth);
            auth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(auth);
            authenticationEventPublisher.publishAuthenticationSuccess(auth);
            listeners.forEach(authenticationListener -> authenticationListener.listenAuthenticationSuccess(auth));
        } catch (AuthenticationException e) {
            authenticationEventPublisher.publishAuthenticationFailure(e, baseAuth);
            Notification notification = new Notification("Неверное имя пользователя или пароль!", 5000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }
}
