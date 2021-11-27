package com.vaadin.tutorial.crm.repository.impl;

import com.vaadin.tutorial.crm.entity.User;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<User> {
    User users = new User();

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {


        users.setId(rs.getLong("id"));
        users.setLogin(rs.getString("login"));
        users.setRole(rs.getString("role"));

        return users;
    }
}
