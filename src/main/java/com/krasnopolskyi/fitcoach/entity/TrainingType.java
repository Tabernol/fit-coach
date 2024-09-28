package com.krasnopolskyi.fitcoach.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "training_type")
@Getter
@Setter
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String trainingTypeName;
    private String testField;
}
