package org.example.repository;

import org.example.entity.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с тренировками ({@link WorkoutSession}).
 */
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    /**
     * Находит тренировку по идентификатору при условии, что она принадлежит
     * указанному пользователю. Используется для проверки права владения.
     *
     * @param userId    идентификатор пользователя-владельца
     * @param sessionId идентификатор тренировки
     * @return найденная тренировка, либо {@link Optional#empty()}
     */
    Optional<WorkoutSession> findByUser_IdAndId(Long userId, Long sessionId);

    /**
     * Находит незавершённую тренировку пользователя, если такая есть.
     * У пользователя не может быть больше одной незавершённой тренировки
     * одновременно.
     *
     * @param userId идентификатор пользователя
     * @return незавершённая тренировка, либо {@link Optional#empty()}
     */
    Optional<WorkoutSession> findByUser_IdAndFinishedAtIsNull(Long userId);

    /**
     * Находит все тренировки пользователя, от последней начатой к первой.
     *
     * @param userId идентификатор пользователя
     * @return список тренировок пользователя
     */
    List<WorkoutSession> findByUser_IdOrderByStartedAtDesc(Long userId);
}
