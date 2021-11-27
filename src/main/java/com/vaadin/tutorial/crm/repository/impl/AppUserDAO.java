package com.vaadin.tutorial.crm.repository.impl;

import com.vaadin.tutorial.crm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

@Repository
@Transactional
public class AppUserDAO extends JdbcDaoSupport {

    @Autowired
    public AppUserDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public User findByUser(String u_name) {

       String sql = "select  id, user_name, role from userslist where user_name = "+"'"+u_name+"'"+";";

        Object[] params = new Object[] { u_name };
        EmployeeRowMapper mapper = new EmployeeRowMapper();
        try {
            User userInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return userInfo;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getRoleNames(Long userId) {
        String sql = "select  role from userslist where id = "+"'"+userId+"'"+";";

        Object[] params = new Object[] { userId };

        EmployeeRowMapper mapper = new EmployeeRowMapper();

        String roles = this.getJdbcTemplate().queryForObject(sql, params, String.class);

        return roles;
    }
}
