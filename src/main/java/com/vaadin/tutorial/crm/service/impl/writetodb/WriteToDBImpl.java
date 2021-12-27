package com.vaadin.tutorial.crm.service.impl.writetodb;

import com.vaadin.tutorial.crm.entity.writetodb.WriteToDB;
import com.vaadin.tutorial.crm.repository.writetodb.WriteToDBRepository;
import com.vaadin.tutorial.crm.service.writetodb.WriteToDBService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержит реализацию методов интерфейса WriteToDBService
 */
@Service
public class WriteToDBImpl implements WriteToDBService {
    private final WriteToDBRepository writeToDBRepository;

    public WriteToDBImpl(WriteToDBRepository writeToDBRepository) {
        this.writeToDBRepository = writeToDBRepository;
    }

    @Override
    public List<WriteToDB> getAll() {
        return writeToDBRepository.getAll();
    }
}
