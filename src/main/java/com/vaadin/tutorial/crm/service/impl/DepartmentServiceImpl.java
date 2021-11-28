package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.tutorial.crm.entity.Department;
import com.vaadin.tutorial.crm.repository.DepartmentRepository;
import com.vaadin.tutorial.crm.service.DepartmentService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Класс реализует методы интерфейса DepartmentService
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.getAll();
    }
}
