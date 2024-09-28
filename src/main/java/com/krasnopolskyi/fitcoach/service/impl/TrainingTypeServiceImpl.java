package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.entity.TrainingType;
import com.krasnopolskyi.fitcoach.repository.TrainingTypeRepository;
import com.krasnopolskyi.fitcoach.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository repository;

    public TrainingTypeServiceImpl(TrainingTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TrainingType> getAll() {
        return repository.findAll();
    }

    @Override
    public TrainingType findByName(String name) {
        return repository.findByTrainingTypeName(name).orElseThrow(
                () -> new IllegalArgumentException("Could not find training with type " + name));
    }
}
