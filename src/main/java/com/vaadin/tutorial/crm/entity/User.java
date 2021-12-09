package com.vaadin.tutorial.crm.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Класс модель описывающий таблицу userslist
 */
@Data
@Entity(name = "userslist")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String login;

    private String userPasswd;

    private String fio;

    private String position;

    private Long idDepartment;

    private String email;

    private String role;

    private Date dateCreate;

    private Date lastDateActive;

    private Long delete;

    @ManyToOne
    @JoinColumn(name = "id_department", referencedColumnName = "id", insertable = false, updatable = false)
    private Department departmentName;

    public User(Long id, String login, String role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public User() {

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
