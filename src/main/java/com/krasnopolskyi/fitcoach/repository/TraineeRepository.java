package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    @Query("SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :username")
    Optional<Trainee> findByUsername(@Param("username") String username);

//    @Query(value = "SELECT " +
//            "DISTINCT username, " +
//            "first_name firstname, " +
//            "last_name lastname, " +
//            "training_type_name specialization " +
//            "FROM training t " +
//            "JOIN trainee tr ON t.trainee_id = tr.id " +
//            "JOIN trainer trainer ON t.trainer_id = trainer.id " +
//            "JOIN training_type type ON trainer.specialization_id = type.id " +
//            "JOIN users u ON trainer.user_id = u.id " +
//            "WHERE tr.user_id = (SELECT id FROM users WHERE username = :username)",
//            nativeQuery = true)
//    List<CoachInfo> findAllCoachesByTraineeUsername(String username);
}
