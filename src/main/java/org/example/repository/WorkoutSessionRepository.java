package org.example.repository;

import org.example.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с тренировками ({@link WorkoutSession}).
 */
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
}
