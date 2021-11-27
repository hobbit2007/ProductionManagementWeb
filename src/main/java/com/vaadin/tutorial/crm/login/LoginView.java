package com.vaadin.tutorial.crm.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.security.AuthManager;
import com.vaadin.tutorial.crm.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.NamingException;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

@Route("login")
@PageTitle("Войти | Система управления несоответствиями")
public class LoginView extends AppLayout implements Serializable {
    VerticalLayout verticalLayout;
    HorizontalLayout horizontalLayout;
    TextField textLogin;
    PasswordField textPasswd;
    Button buttonLogin;
    FlexComponent.Alignment alignment;
    Label label, label1;

    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    List<User> userList;
    private static DataSource dataSource;

    // @Autowired
    private AuthManager authManager;

    @Autowired
    public LoginView(AuthManager authManager) {

        this.authManager = authManager;
        verticalLayout = new VerticalLayout();
        horizontalLayout = new HorizontalLayout();
        verticalLayout.setSizeFull();

        label = new Label("Вход в систему");
        label.getStyle().set("color", "black");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "25pt");

        label1 = new Label("Система управления несоответствиями");
        label1.getStyle().set("color", "blue");
        label1.getStyle().set("font-weight", "bold");
        label1.getStyle().set("font-size", "15pt");
        label1.getStyle().set("margin-left", "30px");

        LoginForm component = new LoginForm();

        verticalLayout.add(label);
        verticalLayout.add(component);

        horizontalLayout.setAlignItems(alignment.CENTER);
        verticalLayout.add(horizontalLayout);

        addToNavbar(label1);
        verticalLayout.setAlignItems(alignment.CENTER);
        setContent(verticalLayout);

        component.addLoginListener(e -> {
            authenticateUser(e.getUsername(), e.getPassword());
        });
    }

    public void authenticateUser(String login, String passwd) throws NamingException {
        authManager.login(login, passwd);
        UI.getCurrent().navigate(MainView.class);
    }
}