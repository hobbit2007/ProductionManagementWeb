package com.vaadin.tutorial.crm.ui.users;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.AdminLayout;

/**
 * Класс диалог информирующий о недостатке прав
 */
@Route(value = "ruleout", layout = AdminLayout.class)
@PageTitle("Недостаточно прав | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class RuleOut extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    Button info = new Button("ОК");
    public RuleOut() {
        this.open();
        info.getStyle().set("background-color", "#d3b342");
        info.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        vMain.add(new AnyComponent().labelTitle("У Вас недостаточно прав!"), info);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);
        info.addClickListener(e -> close());
    }
}
