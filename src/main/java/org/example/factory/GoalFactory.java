package org.example.factory;

import org.example.entity.Exercise;
import org.example.entity.Goal;
import org.example.entity.User;
import org.example.entity.enums.DayOfWeek;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Собирает новый объект {@link Goal}, валидируя значения его полей.
 * Не занимается персистентностью и не обращается к другим сервисам/репозиториям —
 * {@code userId} и {@code exercise} должны быть уже резолвлены и провалидированы
 * вызывающим кодом.
 */
@Component
public class GoalFactory {

    /**
     * Создаёт новую (несохранённую) цель.
     *
     * @param userId     идентификатор пользователя-владельца (уже провалидированный)
     * @param exercise   упражнение, по которому ставится цель (уже резолвленное и доступное пользователю)
     * @param dayOfWeek  день недели сплита
     * @param targetSets целевое количество подходов
     * @param targetReps целевое количество повторений в подходе
     * @param weightKg   целевой рабочий вес в килограммах (может быть {@code 0} для упражнений
     *                   с собственным весом, но не может быть отрицательным)
     * @return собранный, но ещё не сохранённый объект {@link Goal}
     * @throws IllegalArgumentException если {@code dayOfWeek} равен {@code null}, если
     *                                  {@code targetSets}/{@code targetReps} равны {@code null}
     *                                  либо не положительны, либо {@code weightKg} равен
     *                                  {@code null} или отрицателен
     */
    public Goal create(Long userId, Exercise exercise, DayOfWeek dayOfWeek,
                        Integer targetSets, Integer targetReps, BigDecimal weightKg) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        if (targetSets == null || targetSets <= 0) {
            throw new IllegalArgumentException("Target sets must be positive");
        }
        if (targetReps == null || targetReps <= 0) {
            throw new IllegalArgumentException("Target reps must be positive");
        }
        if (weightKg == null || weightKg.signum() < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        User owner = new User();
        owner.setId(userId);

        Goal goal = new Goal();
        goal.setUser(owner);
        goal.setExercise(exercise);
        goal.setDayOfWeek(dayOfWeek);
        goal.setTargetSets(targetSets);
        goal.setTargetReps(targetReps);
        goal.setWeightKg(weightKg);
        return goal;
    }
}
