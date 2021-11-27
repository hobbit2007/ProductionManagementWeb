package com.vaadin.tutorial.crm.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity(name = "userslist")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fio;
    private String login;
    private String fio_1;
    private String fioSender;
    private String role;

    public User(Long id, String login, String role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public User() {

    }

    public Long getUserId() {
        return id;
    }

    public String getUserName() {
        return login;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getFio() { return fio; }

    public void setFioCopy(String fioCopy) {
        this.fio_1 = fioCopy;
    }

    public String getFioCopy() { return fio_1; }

    public void setFioSender(String fioSender) {
        this.fioSender = fioSender;
    }

    public String getFioSender() { return fioSender; }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
