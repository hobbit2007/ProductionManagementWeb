package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.Department;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей department
 */
@Service
public interface DepartmentService {
    List<Department> getAll();
}
