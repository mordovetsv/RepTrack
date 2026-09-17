package org.example.repository;

import org.example.entity.Goal;
import org.example.entity.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с целями пользователей ({@link Goal}).
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * Находит цель по идентификатору при условии, что она принадлежит
     * указанному пользователю. Используется для проверки права владения.
     *
     * @param userId идентификатор пользователя-владельца
     * @param goalId идентификатор цели
     * @return найденная цель, либо {@link Optional#empty()}
     */
    Optional<Goal> findByUser_IdAndId(Long userId, Long goalId);

    /**
     * Находит активные цели пользователя на указанный день недельного сплита.
     *
     * @param userId идентификатор пользователя
     * @param dayOfWeek день недели сплита
     * @return список активных целей на этот день
     */
    List<Goal> findByUser_IdAndDayOfWeekAndActiveTrue(Long userId, DayOfWeek dayOfWeek);

    /**
     * Находит все активные цели пользователя, независимо от дня недели.
     *
     * @param userId идентификатор пользователя
     * @return список активных целей пользователя
     */
    List<Goal> findByUser_IdAndActiveTrue(Long userId);
}
