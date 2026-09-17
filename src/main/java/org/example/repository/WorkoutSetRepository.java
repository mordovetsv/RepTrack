package org.example.repository;

import org.example.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с фактически выполненными подходами ({@link WorkoutSet}).
 */
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {

    /**
     * Находит подход по идентификатору при условии, что он принадлежит
     * указанной тренировке этого пользователя. Используется для проверки
     * права владения.
     *
     * @param setId     идентификатор подхода
     * @param sessionId идентификатор тренировки
     * @param userId    идентификатор пользователя-владельца тренировки
     * @return найденный подход, либо {@link Optional#empty()}
     */
    @Query("SELECT s FROM WorkoutSet s WHERE s.id = :setId AND s.session.id = :sessionId AND s.session.user.id = :userId")
    Optional<WorkoutSet> findByIdAndSessionForUser(@Param("setId") Long setId,
                                                    @Param("sessionId") Long sessionId,
                                                    @Param("userId") Long userId);

    /**
     * Возвращает наибольший номер подхода, уже залогированного в рамках
     * данной тренировки по данной цели, либо {@code 0}, если подходов ещё нет.
     *
     * @param sessionId идентификатор тренировки
     * @param goalId    идентификатор цели
     * @return наибольший номер подхода, либо {@code 0}
     */
    @Query("SELECT COALESCE(MAX(s.setNumber), 0) FROM WorkoutSet s WHERE s.session.id = :sessionId AND s.goal.id = :goalId")
    Integer findMaxSetNumber(@Param("sessionId") Long sessionId, @Param("goalId") Long goalId);

    /**
     * Проверяет, есть ли по данной цели в данной тренировке подход с номером
     * больше указанного. Используется, чтобы разрешать исправление подхода
     * только пока следующий подход по этой же цели ещё не залогирован.
     *
     * @param sessionId идентификатор тренировки
     * @param goalId    идентификатор цели
     * @param setNumber номер подхода, относительно которого проверяем
     * @return {@code true}, если есть более поздний подход по этой цели
     */
    boolean existsBySession_IdAndGoal_IdAndSetNumberGreaterThan(Long sessionId, Long goalId, Integer setNumber);

    /**
     * Находит идентификаторы целей, по которым в рамках тренировки уже
     * залогирован хотя бы один подход, но количество подходов ещё меньше
     * целевого — то есть цель "начата, но не закончена". Используется, чтобы
     * запретить переход к другой цели, пока текущая не завершена.
     *
     * @param sessionId       идентификатор тренировки
     * @param excludingGoalId идентификатор цели, которую не нужно учитывать в результате
     * @return идентификаторы незакрытых целей (кроме {@code excludingGoalId})
     */
    @Query("SELECT g.id FROM WorkoutSet s JOIN s.goal g WHERE s.session.id = :sessionId AND g.id <> :excludingGoalId "
            + "GROUP BY g.id, g.targetSets HAVING COUNT(s) < g.targetSets")
    List<Long> findInProgressGoalIds(@Param("sessionId") Long sessionId, @Param("excludingGoalId") Long excludingGoalId);

    /**
     * Возвращает все подходы тренировки, по порядку номера.
     *
     * @param sessionId идентификатор тренировки
     * @return список подходов тренировки
     */
    List<WorkoutSet> findBySession_IdOrderBySetNumber(Long sessionId);
}
