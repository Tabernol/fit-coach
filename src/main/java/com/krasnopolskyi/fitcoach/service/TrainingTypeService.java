package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingType> getAll();
    TrainingType findByName(String name);
}
