package com.vaadin.tutorial.crm.ui.users;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.service.DepartmentService;
import com.vaadin.tutorial.crm.service.ShopService;
import com.vaadin.tutorial.crm.service.UserService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.UserLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Класс для редактирования пользователя
 */
@Route(value = "useredit", layout = UserLayout.class)
@PageTitle("Dashboard | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class UsersEdit extends VerticalLayout {
    private VerticalLayout vMain = new VerticalLayout();
    private Grid<User> grid = new Grid<>();
    private Grid.Column<User> colLogin, colFio, colPosition, colEmail, colDateCreate, colLastDate, colRole;
    private ListDataProvider<User> dataProvider;
    private FormUsersEdit formUsersEdit;
    private Div content;
    private long userID;
    private UserService userService;
    private DepartmentService departmentService;

    @Autowired
    public UsersEdit(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;

        AnyComponent anyComponent = new AnyComponent();

        addClassName("list-view");
        setSizeFull();

        configureGrid();

        vMain.add(anyComponent.labelTitle("Редактирование пользователя"));
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        formUsersEdit = new FormUsersEdit(userService.getAll(), departmentService.getAllByAll());
        formUsersEdit.addListener(FormUsersEdit.ContactFormEvent.EditEvent.class, e -> saveUser());
        formUsersEdit.addListener(FormUsersEdit.ContactFormEvent.CloseEvent.class, e -> close());

        content = new Div(grid, formUsersEdit);
        content.addClassName("content");
        content.setSizeFull();

        add(vMain, content);

        updateDataGrid();
        close();
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colLogin = grid.addColumn(userService -> userService.getLogin()).setHeader("Логин");
        colFio = grid.addColumn(userService -> userService.getFio()).setHeader("ФИО");
        colPosition = grid.addColumn(userService -> userService.getPosition()).setHeader("Должность");
        colEmail = grid.addColumn(userService -> userService.getEmail()).setHeader("E-mail");
        colDateCreate = grid.addColumn(userService -> userService.getDateCreate()).setHeader("Дата регистрации");
        colLastDate = grid.addColumn(userService -> userService.getLastDateActive()).setHeader("Дата входа в систему");
        colRole = grid.addColumn(userService -> userService.getRole()).setHeader("Роль");

        colLogin.setResizable(true);
        colFio.setResizable(true);
        colPosition.setResizable(true);
        colEmail.setResizable(true);
        colDateCreate.setResizable(true);
        colLastDate.setResizable(true);
        colRole.setResizable(true);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                userID = event.getValue().getId();
                editForm(event.getValue());
            }
        });
    }

    private void updateDataGrid() {
        dataProvider = new ListDataProvider<>(
                userService.getAll());
        grid.setItems(dataProvider);
    }

    private void saveUser() {
        try {
            User user = new User();
            user.setFio(FormUsersEdit.ContactFormEvent.EditEvent.getFio());
            user.setEmail(FormUsersEdit.ContactFormEvent.EditEvent.getEmail());
            user.setRole(FormUsersEdit.ContactFormEvent.EditEvent.getRole());
            user.setId(userID);

            userService.updateUserInfo(user);
            Notification.show("Сохранено!", 5000, Notification.Position.MIDDLE);
            close();
        }
        catch (Exception e) {

        }
    }

    private void close() {
        formUsersEdit.setUsers(null);
        formUsersEdit.setVisible(false);
        updateDataGrid();
    }

    private void editForm(User user) {
        if (user == null)
            close();
        formUsersEdit.setUsers(user);
        formUsersEdit.setVisible(true);
        addClassName("editing");
    }
}
