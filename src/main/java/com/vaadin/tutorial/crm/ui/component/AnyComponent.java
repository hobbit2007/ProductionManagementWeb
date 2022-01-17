package com.vaadin.tutorial.crm.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Метод реализует создание новой метки класса Label
     * @param labelText - текст метки
     * @param colorText - цвет текста
     * @param sizeText - размер текста
     * @return - возвращает объект класса Label
     */
    public Component labelTitle(String labelText, String colorText, String sizeText) {
        labelTitle = new Label(labelText);
        labelTitle.getStyle().set("color", colorText);
        labelTitle.getStyle().set("font-weight", "bold");
        labelTitle.getStyle().set("font-size", sizeText);
        labelTitle.getStyle().set("margin-left", "30px");

        return labelTitle;
    }

    /**
     * Метод реализующий перевод на русский, календарь DatePicker
     * @return - объект класса DatePicker.DatePickerI18n с переводом
     */
    public static DatePicker.DatePickerI18n datePickerRus() {
        List<String> month = new ArrayList<>();
        List<String> days = new ArrayList<>();
        List<String> daysShort = new ArrayList<>();

        DatePicker.DatePickerI18n dp = new DatePicker.DatePickerI18n();
        month.add("Январь");
        month.add("Февраль");
        month.add("Март");
        month.add("Апрель");
        month.add("Май");
        month.add("Июнь");
        month.add("Июль");
        month.add("Август");
        month.add("Сентябрь");
        month.add("Октябрь");
        month.add("Ноябрь");
        month.add("Декабрь");

        days.add("Воскресенье");
        days.add("Понедельник");
        days.add("Вторник");
        days.add("Среде");
        days.add("Четверг");
        days.add("Пятница");
        days.add("Суббота");

        daysShort.add("Вс");
        daysShort.add("Пн");
        daysShort.add("Вт");
        daysShort.add("Ср");
        daysShort.add("Чт");
        daysShort.add("Пт");
        daysShort.add("Сб");

        dp.setMonthNames(month);
        dp.setWeekdays(days);
        dp.setWeekdaysShort(daysShort);
        dp.setCalendar("Календарь");
        dp.setToday("Сегодня");
        dp.setCancel("Отмена");
        dp.setDateFormat("dd-MM-yyyy");

        return dp;
    }

    /**
     * Метод проверки на наличие в начале вводимого текста спецсимволов
     * @param text - объект класса TextField
     * @return - возвращает true, если спецсимволы в начале не найдены или false, если найдены
     */
    public static boolean checkEscSymbol(TextField text) {
        if (text.getValue().charAt(0) != ' ' && text.getValue().charAt(0) != '~' &&
                text.getValue().charAt(0) != '`' && text.getValue().charAt(0) != '"' &&
                text.getValue().charAt(0) != ':' && text.getValue().charAt(0) != ';' &&
                text.getValue().charAt(0) != ',' && text.getValue().charAt(0) != '.' &&
                text.getValue().charAt(0) != '!' && text.getValue().charAt(0) != '@' &&
                text.getValue().charAt(0) != '#' && text.getValue().charAt(0) != '$' &&
                text.getValue().charAt(0) != '%' && text.getValue().charAt(0) != '^' &&
                text.getValue().charAt(0) != '&' && text.getValue().charAt(0) != '*' &&
                text.getValue().charAt(0) != '(' && text.getValue().charAt(0) != ')' &&
                text.getValue().charAt(0) != '-' && text.getValue().charAt(0) != '_' &&
                text.getValue().charAt(0) != '+' && text.getValue().charAt(0) != '+' &&
                text.getValue().charAt(0) != '\'')
            return true;
        else
            return false;
    }
}
