package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Query("SELECT t FROM Trainer t JOIN t.user u WHERE u.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    //    @Query(value = "SELECT " +
//            "DISTINCT username, " +
//            "first_name firstname, " +
//            "last_name lastname " +
//            "FROM training t " +
//            "JOIN trainee tr ON t.trainee_id = tr.id " +
//            "JOIN trainer trainer ON t.trainer_id = trainer.id "+
//            "JOIN users u ON tr.user_id = u.id " +
//            "WHERE trainer.user_id = (SELECT id FROM users WHERE username = :username)",
//            nativeQuery = true)
//    List<TraineeInfo> findAllTraineesByCoachUsername(String username);
    @Query("SELECT t FROM Trainer t JOIN t.user u WHERE u.isActive = true")
    List<Trainer> findAllActiveTrainers();
}
