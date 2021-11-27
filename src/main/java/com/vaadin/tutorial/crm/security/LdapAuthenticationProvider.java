package com.vaadin.tutorial.crm.security;

import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Optional;

public class LdapAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) auth;
        try {
            Optional<User> u = userRepository.findByLogin(authentication.getPrincipal().toString());
            if (u.get().getUserPasswd().equals(authentication.getCredentials()))
                return new com.vaadin.tutorial.crm.security.UserAuthentication(authentication.getPrincipal().toString(), com.vaadin.tutorial.crm.security.Authorities.parse(u.get().getRole()), u.get());
            else
                return new com.vaadin.tutorial.crm.security.UserAuthentication("No auth", com.vaadin.tutorial.crm.security.Authorities.parse(u.get().getRole()), u.get());
        } catch (Exception e) {
            throw new BadCredentialsException("Bad creds");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
