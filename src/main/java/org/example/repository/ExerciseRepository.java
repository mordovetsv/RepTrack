package org.example.repository;

import org.example.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с упражнениями ({@link Exercise}).
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
