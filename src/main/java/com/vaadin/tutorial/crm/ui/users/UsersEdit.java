package com.vaadin.tutorial.crm.ui.users;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.User;
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
    private Grid.Column<User> colFio, colPosition, colEmail, colDateCreate, colLastDate, colRole;
    private ListDataProvider<User> dataProvider;
    private UserService userService;

    @Autowired
    public UsersEdit(UserService userService) {
        this.userService = userService;
        AnyComponent anyComponent = new AnyComponent();

        addClassName("list-view");
        setSizeFull();

        configureGrid();

        vMain.add(anyComponent.labelTitle("Редактирование пользователя"), grid);
        vMain.setSizeFull();
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(vMain);

        updateDataGrid();
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colFio = grid.addColumn(userService -> userService.getFio()).setHeader("ФИО");
        colPosition = grid.addColumn(userService -> userService.getPosition()).setHeader("Должность");
        colEmail = grid.addColumn(userService -> userService.getEmail()).setHeader("E-mail");
        colDateCreate = grid.addColumn(userService -> userService.getDateCreate()).setHeader("Дата регистрации");
        colLastDate = grid.addColumn(userService -> userService.getLastDateActive()).setHeader("Дата входа в систему");
        colRole = grid.addColumn(userService -> userService.getRole()).setHeader("Роль");

        colFio.setResizable(true);
        colPosition.setResizable(true);
        colEmail.setResizable(true);
        colDateCreate.setResizable(true);
        colLastDate.setResizable(true);
        colRole.setResizable(true);
    }

    private void updateDataGrid() {
        dataProvider = new ListDataProvider<>(
                userService.getAll());
        grid.setItems(dataProvider);
    }
}
