package com.vaadin.tutorial.crm;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;

/**
 * Класс с помощью которого можно обновлять пользовательский интерфейс с сервера без явного запроса
 * обновлений пользователем (посредством нажатия кнопки или другого взаимодействия).
 */
@Push(PushMode.MANUAL)
public class AppShell implements AppShellConfigurator {
}
