package com.vaadin.tutorial.crm.ui.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.Department;
import com.vaadin.tutorial.crm.entity.Shop;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.service.DepartmentService;
import com.vaadin.tutorial.crm.service.ShopService;
import com.vaadin.tutorial.crm.service.UserService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.component.LabelComponent;
import com.vaadin.tutorial.crm.ui.layout.UserLayout;
import com.vaadin.tutorial.crm.validation.ValidTextField;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс реализующий создание нового пользователя
 */
@Route(value = "userscreate", layout = UserLayout.class)
@PageTitle("Создание нового пользователя | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class UsersCreate extends VerticalLayout {
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private DepartmentService departmentService;

    VerticalLayout vContent = new VerticalLayout();
    HorizontalLayout hContentComponent = new HorizontalLayout();
    HorizontalLayout hContentComponent1 = new HorizontalLayout();
    HorizontalLayout hContentComponent2 = new HorizontalLayout();
    HorizontalLayout hContentComponent3 = new HorizontalLayout();
    HorizontalLayout hContentComponent4 = new HorizontalLayout();
    AnyComponent anyComponent = new AnyComponent();

    ValidTextField loginText = new ValidTextField();
    PasswordField passwdText = new PasswordField("Введите пароль:");
    PasswordField repeatPasswdText = new PasswordField("Повторите пароль:");
    TextField fioText = new TextField("Введите ФИО:");
    ComboBox<Shop> shopList = new ComboBox<>("Выберите цех:");
    ComboBox<Department> departmentList = new ComboBox<>("Выберите подразделение:");
    ComboBox<String> roleList = new ComboBox<>("Выберите роль:");
    Button saveButton = new Button("Сохранить");
    TextField position = new TextField("Введите должность:");
    EmailField email = new EmailField("Введите email:");

    List<String> roleArray = new ArrayList<>();

    public UsersCreate(UserService userService, ShopService shopService, DepartmentService departmentService) {
        this.userService = userService;
        this.shopService = shopService;
        this.departmentService = departmentService;

        loginText.setRequired(true);
        passwdText.setRequired(true);
        repeatPasswdText.setRequired(true);
        fioText.setRequired(true);
        shopList.setRequired(true);
        departmentList.setRequired(true);
        roleList.setRequired(true);
        position.setRequired(true);
        email.setRequiredIndicatorVisible(true);

        loginText.setLabel("Введите логин:");

        departmentList.setEnabled(false);

        roleArray.add("USER");
        roleArray.add("ADMIN");

        shopList.setItems(shopService.getAll());
        shopList.setItemLabelGenerator(Shop::getShopName);
        shopList.addValueChangeListener(e -> {
            if (e != null) {
                departmentList.setEnabled(true);
                departmentList.setItems(departmentService.getAll(e.getValue().getId()));
                departmentList.setItemLabelGenerator(Department::getDepartmentName);
            }
            else
                departmentList.setEnabled(false);
        });

        roleList.setItems(roleArray);

        Icon iconSaveButton = new Icon(VaadinIcon.DISC);
        saveButton.setIcon(iconSaveButton);
        saveButton.getStyle().set("background-color", "#d3b342");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hContentComponent.add(loginText, passwdText);
        hContentComponent1.add(repeatPasswdText, fioText);
        hContentComponent2.add(shopList, departmentList);
        hContentComponent3.add(position, email);
        hContentComponent4.add(roleList, saveButton);
        hContentComponent4.setAlignItems(Alignment.BASELINE);

        vContent.add(anyComponent.labelTitle("Создание нового пользователя"), hContentComponent, hContentComponent1,
                hContentComponent2, hContentComponent3, hContentComponent4);
        vContent.setSizeFull();
        vContent.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(vContent);

        loginText.addValidator(new StringLengthValidator("Минимум 5 символов", 5, 30));

        saveButton.addClickListener(e -> {
           if (passwdText.getValue().isEmpty() || repeatPasswdText.getValue().isEmpty()) {
               Notification.show("Поле пароля не может быть пустым!", 3000, Notification.Position.MIDDLE);
               return;
           }
           if (!passwdText.getValue().equals(repeatPasswdText.getValue())) {
               Notification.show("Пароли не совпадают!", 3000, Notification.Position.MIDDLE);
               return;
           }
           if (!loginText.getValue().isEmpty() && !fioText.getValue().isEmpty() && !shopList.isEmpty() && !departmentList.isEmpty() && !roleList.isEmpty() &&
                !position.getValue().isEmpty() && !email.getValue().isEmpty()) {
               for (int i = 0; userService.getAll().size() < i; i++) {
                   if (loginText.getValue().equals(userService.getAll().get(i).getLogin())) {
                       Notification.show("Такой пользователь уже зарегистрирован в системе!", 3000, Notification.Position.MIDDLE);
                       return;
                   }
               }
               try {
                   User user = new User();
                   user.setLogin(loginText.getValue());
                   user.setUserPasswd(DigestUtils.md5Hex(passwdText.getValue()));
                   user.setFio(fioText.getValue());
                   user.setIdDepartment(departmentList.getValue().getId());
                   user.setRole(roleList.getValue());
                   user.setPosition(position.getValue());
                   user.setEmail(email.getValue());
                   user.setDateCreate(new Date());
                   user.setLastDateActive(new Date());
                   user.setDelete(0L);

                   userService.saveAll(user);
                   Notification.show("Пользователь создан успешно!", 3000, Notification.Position.MIDDLE);
                   userSaveComplite();
               }
               catch (Exception ex) {
                   Notification.show("Не могу сохранить пользователя в БД! " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
               }
           }
           else
               Notification.show("Не все поля заполнены!", 3000, Notification.Position.MIDDLE);
        });
    }

    private void userSaveComplite() {
        Shop shop = new Shop();
        Department department = new Department();
        loginText.setValue("");
        passwdText.setValue("");
        repeatPasswdText.setValue("");
        fioText.setValue("");
        shop.setShopName("");
        shopList.setValue(shop);
        department.setDepartmentName("");
        departmentList.setValue(department);
        position.setValue("");
        email.setValue("");
        roleList.setValue("");
    }
}
