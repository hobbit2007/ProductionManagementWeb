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

    @Override
    public void updateCoefficient1(Double value) {
        writeToDBRepository.updateCoefficient1(value);
    }

    @Override
    public void updateCoefficient2(Double value) {
        writeToDBRepository.updateCoefficient2(value);
    }

    @Override
    public void updateRTFor1C(Long value) {
        writeToDBRepository.updateRTFor1C(value);
    }

    @Override
    public void updateWriteFor1C(String value) {
        writeToDBRepository.updateWriteFor1C(value);
    }
}
