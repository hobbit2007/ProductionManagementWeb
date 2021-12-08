package com.vaadin.tutorial.crm.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.security.AuthManager;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.UserService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;
import com.vaadin.tutorial.crm.ui.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс реализующий авторизацию в системе
 */
@Route("login")
@PageTitle("Войти | Управление производством")
public class LoginView extends AppLayout implements Serializable {
    VerticalLayout verticalLayout;
    FlexComponent.Alignment alignment;
    Label label1;

    private AuthManager authManager;
    private UserService userService;

    @Autowired
    public LoginView(AuthManager authManager, UserService userService) {

        this.authManager = authManager;
        this.userService = userService;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        label1 = new Label("ИстАгроДон - Система управление производством");
        label1.getStyle().set("color", "blue");
        label1.getStyle().set("font-weight", "bold");
        label1.getStyle().set("font-size", "15pt");
        label1.getStyle().set("margin-left", "30px");

        LoginForm component = new LoginForm();
        component.setForgotPasswordButtonVisible(false);
        LoginI18n loginI18n = new LoginI18n();
        LoginI18n.Form form = new LoginI18n.Form();
        form.setUsername("Логин");
        form.setPassword("Пароль");
        form.setTitle("Вход в систему");
        form.setSubmit("Войти");
        loginI18n.setForm(form);
        component.setI18n(loginI18n);

        verticalLayout.add(component);

        addToNavbar(label1);
        verticalLayout.setAlignItems(alignment.CENTER);
        setContent(verticalLayout);

        component.addLoginListener(e -> {
            authenticateUser(e.getUsername(), e.getPassword());
            component.setEnabled(true);
        });
    }

    public void authenticateUser(String login, String passwd) {
        authManager.login(login, passwd);
        if (authManager.authFlag) {
            User user = new User();
            user.setLastDateActive(new Date());
            user.setId(SecurityUtils.getAuthentication().getDetails().getId());
            userService.updateDateActive(user);
            PLCConnect.controllerStatusWashing("10.100.10.101");
            PLCConnect.controllerStatusDiffusion("10.100.10.102");
            PLCConnect.controllerStatusFermentation("10.100.10.103");
            PLCConnect.controllerStatus("10.100.10.106");
            PLCConnect.controllerStatusBottling("10.100.10.107");
            PLCConnect.controllerStatusDrying("10.100.10.108");
            UI.getCurrent().navigate(MainView.class);
        }
    }
}