package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Exercise;
import org.example.entity.Goal;
import org.example.entity.enums.DayOfWeek;
import org.example.factory.GoalFactory;
import org.example.repository.GoalRepository;
import org.example.validation.IdValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository repository;
    private final ExerciseService exerciseService;
    private final GoalFactory goalFactory;
    private final IdValidator idValidator;

    /**
     * Создаёт новую цель пользователя по упражнению на конкретный день сплита.
     * Упражнение должно быть доступно пользователю для выбора (глобальное
     * либо его собственное кастомное) — проверяется через {@link ExerciseService}.
     *
     * @param userId     идентификатор пользователя
     * @param exerciseId идентификатор упражнения
     * @param dayOfWeek  день недели сплита
     * @param targetSets целевое количество подходов
     * @param targetReps целевое количество повторений в подходе
     * @param weightKg   целевой рабочий вес в килограммах
     * @return созданная цель
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null}
     *                                  либо не положителен, если {@code dayOfWeek} равен {@code null},
     *                                  если {@code targetSets}/{@code targetReps} равны {@code null}
     *                                  либо не положительны, если {@code weightKg} равен {@code null}
     *                                  либо отрицателен, либо упражнение недоступно пользователю
     */
    @Transactional
    public Goal createGoal(Long userId, Long exerciseId, DayOfWeek dayOfWeek,
                            Integer targetSets, Integer targetReps, BigDecimal weightKg) {
        Exercise exercise = exerciseService.getSelectableForUser(userId, exerciseId);
        Goal goal = goalFactory.create(userId, exercise, dayOfWeek, targetSets, targetReps, weightKg);
        return repository.save(goal);
    }

    /**
     * Находит цель по идентификатору при условии, что она принадлежит
     * указанному пользователю. Используется для проверки права владения
     * перед изменением цели.
     *
     * @param userId идентификатор пользователя-владельца
     * @param goalId идентификатор цели
     * @return найденная цель
     * @throws IllegalArgumentException если {@code userId} или {@code goalId} равен {@code null}
     *                                  либо не положителен, либо цель не найдена или не принадлежит
     *                                  пользователю
     */
    @Transactional(readOnly = true)
    public Goal getOwnedByUserAndId(Long userId, Long goalId) {
        idValidator.requirePositive(userId);
        idValidator.requirePositive(goalId);
        Optional<Goal> goal = repository.findByUser_IdAndId(userId, goalId);
        if (goal.isEmpty()) {
            throw new IllegalArgumentException("Goal not found");
        }
        return goal.get();
    }

    /**
     * Возвращает активные цели пользователя на указанный день недельного сплита.
     *
     * @param userId    идентификатор пользователя
     * @param dayOfWeek день недели сплита
     * @return список активных целей на этот день
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен,
     *                                  или {@code dayOfWeek} равен {@code null}
     */
    @Transactional(readOnly = true)
    public List<Goal> getActiveGoalsForUserAndDay(Long userId, DayOfWeek dayOfWeek) {
        idValidator.requirePositive(userId);
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        return repository.findByUser_IdAndDayOfWeekAndActiveTrue(userId, dayOfWeek);
    }

    /**
     * Возвращает все активные цели пользователя, независимо от дня недели.
     *
     * @param userId идентификатор пользователя
     * @return список активных целей пользователя
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен
     */
    @Transactional(readOnly = true)
    public List<Goal> getActiveGoalsForUser(Long userId) {
        idValidator.requirePositive(userId);
        return repository.findByUser_IdAndActiveTrue(userId);
    }

    /**
     * Отмечает цель как достигнутую: проставляет {@code achievedAt} текущим
     * моментом и переводит цель в неактивную. Решение о том, что делать
     * дальше (например, создать новую цель с повышенной нагрузкой), остаётся
     * за вызывающим кодом — этот метод только закрывает текущую цель.
     *
     * @param userId идентификатор пользователя-владельца
     * @param goalId идентификатор цели
     * @return обновлённая цель
     * @throws IllegalArgumentException если {@code userId} или {@code goalId} равен {@code null}
     *                                  либо не положителен, либо цель не найдена или не принадлежит
     *                                  пользователю
     * @throws IllegalStateException    если цель уже неактивна (достигнута либо заброшена ранее)
     */
    @Transactional
    public Goal achieveGoal(Long userId, Long goalId) {
        Goal goal = getOwnedByUserAndId(userId, goalId);
        if (!goal.isActive()) {
            throw new IllegalStateException("Goal is already inactive");
        }
        goal.setAchievedAt(OffsetDateTime.now());
        goal.setActive(false);
        return repository.save(goal);
    }

    /**
     * Отмечает цель как заброшенную: переводит цель в неактивную без
     * проставления {@code achievedAt} — в отличие от {@link #achieveGoal},
     * цель не была достигнута, пользователь просто отказался от неё.
     *
     * @param userId идентификатор пользователя-владельца
     * @param goalId идентификатор цели
     * @return обновлённая цель
     * @throws IllegalArgumentException если {@code userId} или {@code goalId} равен {@code null}
     *                                  либо не положителен, либо цель не найдена или не принадлежит
     *                                  пользователю
     * @throws IllegalStateException    если цель уже неактивна (достигнута либо заброшена ранее)
     */
    @Transactional
    public Goal abandonGoal(Long userId, Long goalId) {
        Goal goal = getOwnedByUserAndId(userId, goalId);
        if (!goal.isActive()) {
            throw new IllegalStateException("Goal is already inactive");
        }
        goal.setActive(false);
        return repository.save(goal);
    }
}
