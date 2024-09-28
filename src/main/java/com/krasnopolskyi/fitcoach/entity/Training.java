package com.krasnopolskyi.fitcoach.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id")
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private Coach coach;
    @ManyToOne
    @JoinColumn(name = "training_type_id", referencedColumnName = "id")
    private TrainingType trainingType;
    private String trainingName;
    private LocalDate trainingDate;
    private Integer trainingDuration;


    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainee=" + trainee +
                ", coach=" + coach +
                ", trainingType=" + trainingType +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}
