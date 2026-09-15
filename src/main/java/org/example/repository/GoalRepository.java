package org.example.repository;

import org.example.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с целями пользователей ({@link Goal}).
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
