package com.vaadin.tutorial.crm.ui.users;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDateConverter;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.entity.Department;
import com.vaadin.tutorial.crm.entity.Shop;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Класс форма для редактирования пользователя
 */
public class FormUsersEdit extends FormLayout {

    private TextField login = new TextField("Логин:");
    private TextField fio = new TextField("ФИО:");
    private ComboBox<Department> departmentName = new ComboBox<>("Департамент:");
    private TextField position = new TextField("Должность:");
    private TextField email = new TextField("E-mail:");
    private ComboBox<String> role = new ComboBox<>("Роль:");
    private TextField dateCreate = new TextField("Дата регистрации:");
    private TextField dateLast = new TextField("Дата входа:");
    private Button save = new Button("Сохранить");
    private Button close = new Button("Закрыть");
    Binder<User> binder = new BeanValidationBinder<>(User.class);
    private FormLayout formLayout = new FormLayout();
    private UserService userService;
    private User user = new User();

    @Autowired
    public FormUsersEdit(List<User> users, List<Department> departments) {

        addClassName("contact-form");
        binder.forField(dateCreate).withConverter(new StringToDateConverter()).bind(User::getDateCreate, null);
        binder.forField(dateLast).withConverter(new StringToDateConverter()).bind(User::getLastDateActive, null);
        binder.bindInstanceFields(this);

        role.setItems("USER", "ADMIN");
        role.setValue(users.get(0).getRole());

        departmentName.setItems(departments);
        departmentName.setItemLabelGenerator(Department::getDepartmentName);

        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setIcon(new Icon(VaadinIcon.DISC));

        close.getStyle().set("background-color", "#d3b342");
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.setIcon(new Icon(VaadinIcon.CLOSE));

        viewDetail();
        disComponent();

        save.addClickListener(e -> {
           fireEvent(new FormUsersEdit.ContactFormEvent.EditEvent(this, fio.getValue(), email.getValue(), role.getValue()));
        });
        close.addClickListener(e -> {
           fireEvent(new FormUsersEdit.ContactFormEvent.CloseEvent(this));
        });
    }

    /**
     * Метод для отображения компонентов детального просмотра
     */
    private void viewDetail() {
        add(
            login,
            fio,
            departmentName,
            position,
            email,
            role,
            dateCreate,
            dateLast,
            save,
            close
        );
    }

    private void disComponent() {
        login.setReadOnly(true);
        departmentName.setReadOnly(true);
        position.setReadOnly(true);
        dateCreate.setReadOnly(true);
        dateLast.setReadOnly(true);
    }

    public void setUsers(User user) {
        this.user = user;
        binder.readBean(user);
    }

    public static abstract class ContactFormEvent extends ComponentEvent<FormUsersEdit> {
        private static String fio;
        private static String email;
        private static String role;
        protected ContactFormEvent(FormUsersEdit source, String fio, String email, String role) {
            super(source, false);
            this.fio = fio;
            this.email = email;
            this.role = role;
        }

        public static String getFio() {
            return fio;
        }
        public static String getEmail() {
            return email;
        }
        public static String getRole() {
            return role;
        }

        public static class EditEvent extends ContactFormEvent {
            EditEvent(FormUsersEdit source, String fio, String email, String role) {
                super(source, fio, email, role);
            }
        }

        public static class CloseEvent extends ContactFormEvent {
            CloseEvent(FormUsersEdit source) {
                super(source, "", "", "");
            }
        }

    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
