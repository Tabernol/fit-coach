package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CoachRepository extends JpaRepository<Coach, Long> {

    @Query("SELECT t FROM Coach t JOIN t.user u WHERE u.username = :username")
    Optional<Coach> findByUsername(@Param("username") String username);
}
