package com.vaadin.tutorial.crm.service.writetodb;

import com.vaadin.tutorial.crm.entity.writetodb.WriteToDB;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей settings
 */
@Service
public interface WriteToDBService {
    List<WriteToDB> getAll();
}
