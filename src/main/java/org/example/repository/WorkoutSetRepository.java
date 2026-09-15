package org.example.repository;

import org.example.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с фактически выполненными подходами ({@link WorkoutSet}).
 */
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
}
