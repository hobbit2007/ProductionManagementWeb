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

public class LdapAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Value("${ldap.url}")
    private String ldapURL;

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) auth;

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.PROVIDER_URL, ldapURL);
        env.put(Context.SECURITY_PRINCIPAL, authentication.getPrincipal() + "@" + ldapDomain);
        env.put(Context.SECURITY_CREDENTIALS, authentication.getCredentials().toString());

        try {
            InitialDirContext ctx = new InitialDirContext(env);
            User u = userRepository.findByLogin(authentication.getPrincipal().toString())
                    .orElseGet(() -> {
                        User user = new User();
                        user.setRole(com.vaadin.tutorial.crm.security.Authorities.USER.getAuthority());
                        user.setLogin(authentication.getPrincipal().toString());
                        return user;
                    });
            userRepository.saveAndFlush(u);
            return new com.vaadin.tutorial.crm.security.UserAuthentication(authentication.getPrincipal().toString(), com.vaadin.tutorial.crm.security.Authorities.parse(u.getRole()), u);
        } catch (javax.naming.AuthenticationException e) {
            throw new BadCredentialsException("Bad creds");
        } catch (NamingException e) {
            throw new AuthenticationServiceException("LDAP err", e);
        }
    }

    private String formatData() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");

        return formatForDateNow.format(dateNow);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
