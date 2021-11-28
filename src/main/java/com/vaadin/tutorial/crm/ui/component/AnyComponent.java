package com.vaadin.tutorial.crm.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Класс реализующий создание компонентов, которые часто используется в приложении
 */
public class AnyComponent extends VerticalLayout {

    private Label labelTitle;

    public AnyComponent() {

    }

    /**
     * Метод реализует создание новой метки класса Label
     * @param labelText - текст метки
     * @return - возвращает объект класса Label
     */
    public Component labelTitle(String labelText) {
        labelTitle = new Label(labelText);
        labelTitle.getStyle().set("color", "#d3b342");
        labelTitle.getStyle().set("font-weight", "bold");
        labelTitle.getStyle().set("font-size", "16pt");
        labelTitle.getStyle().set("margin-left", "30px");

        return labelTitle;
    }
}
