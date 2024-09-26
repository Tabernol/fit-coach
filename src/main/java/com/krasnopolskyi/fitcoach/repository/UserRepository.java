package com.krasnopolskyi.fitcoach.repository;

import com.krasnopolskyi.fitcoach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
