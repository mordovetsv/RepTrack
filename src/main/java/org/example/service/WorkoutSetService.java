package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Goal;
import org.example.entity.WorkoutSession;
import org.example.entity.WorkoutSet;
import org.example.repository.WorkoutSetRepository;
import org.example.validation.IdValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutSetService {
    private final WorkoutSetRepository repository;
    private final WorkoutSessionService sessionService;
    private final GoalService goalService;
    private final IdValidator idValidator;

    /**
     * Логирует новый подход по цели в рамках тренировки. Номер подхода
     * вычисляется сервисом автоматически, вес берётся из цели — вызывающий
     * код передаёт только фактическое количество повторений. Нельзя логировать
     * подход в уже завершённую тренировку, по неактивной цели, а также нельзя
     * начать новую цель, пока в этой тренировке есть другая цель, начатая, но
     * не закрытая до целевого количества подходов.
     *
     * @param userId    идентификатор пользователя
     * @param sessionId идентификатор тренировки
     * @param goalId    идентификатор цели
     * @param repsDone  фактическое количество выполненных повторений
     * @return залогированный подход
     * @throws IllegalArgumentException если {@code userId}/{@code sessionId}/{@code goalId} равны
     *                                  {@code null} либо не положительны, если {@code repsDone} равен
     *                                  {@code null} либо отрицателен, либо тренировка/цель не найдены
     *                                  или не принадлежат пользователю
     * @throws IllegalStateException    если тренировка уже завершена, цель неактивна, по цели уже
     *                                  залогировано целевое количество подходов, либо в этой
     *                                  тренировке есть другая незакрытая цель
     */
    @Transactional
    public WorkoutSet addSet(Long userId, Long sessionId, Long goalId, Integer repsDone) {
        if (repsDone == null || repsDone < 0) {
            throw new IllegalArgumentException("Reps done cannot be negative");
        }

        WorkoutSession session = sessionService.getOwnedByUserAndId(userId, sessionId);
        if (session.getFinishedAt() != null) {
            throw new IllegalStateException("Workout session is already finished");
        }

        Goal goal = goalService.getOwnedByUserAndId(userId, goalId);
        if (!goal.isActive()) {
            throw new IllegalStateException("Goal is not active");
        }

        if (!repository.findInProgressGoalIds(sessionId, goalId).isEmpty()) {
            throw new IllegalStateException("Finish the goal already in progress before starting another one");
        }

        int nextSetNumber = repository.findMaxSetNumber(sessionId, goalId) + 1;
        if (nextSetNumber > goal.getTargetSets()) {
            throw new IllegalStateException("Target number of sets for this goal is already logged");
        }

        WorkoutSet set = new WorkoutSet();
        set.setSession(session);
        set.setGoal(goal);
        set.setSetNumber(nextSetNumber);
        set.setRepsDone(repsDone);
        set.setWeightKg(goal.getWeightKg());

        return repository.save(set);
    }

    /**
     * Исправляет количество повторений уже залогированного подхода.
     * Разрешено только до тех пор, пока по этой же цели в этой же тренировке
     * не залогирован следующий подход.
     *
     * @param userId    идентификатор пользователя
     * @param sessionId идентификатор тренировки
     * @param setId     идентификатор подхода
     * @param repsDone  новое фактическое количество выполненных повторений
     * @return обновлённый подход
     * @throws IllegalArgumentException если {@code userId}/{@code sessionId}/{@code setId} равны
     *                                  {@code null} либо не положительны, если {@code repsDone} равен
     *                                  {@code null} либо отрицателен, либо подход не найден или не
     *                                  принадлежит этой тренировке/пользователю
     * @throws IllegalStateException    если по этой же цели уже залогирован следующий подход
     */
    @Transactional
    public WorkoutSet updateSet(Long userId, Long sessionId, Long setId, Integer repsDone) {
        idValidator.requirePositive(userId);
        idValidator.requirePositive(sessionId);
        idValidator.requirePositive(setId);
        if (repsDone == null || repsDone < 0) {
            throw new IllegalArgumentException("Reps done cannot be negative");
        }

        Optional<WorkoutSet> found = repository.findByIdAndSessionForUser(setId, sessionId, userId);
        if (found.isEmpty()) {
            throw new IllegalArgumentException("Workout set not found");
        }
        WorkoutSet set = found.get();

        boolean hasLaterSet = repository.existsBySession_IdAndGoal_IdAndSetNumberGreaterThan(
                sessionId, set.getGoal().getId(), set.getSetNumber());
        if (hasLaterSet) {
            throw new IllegalStateException("Cannot edit a set once the next set for this goal is logged");
        }

        set.setRepsDone(repsDone);
        return repository.save(set);
    }

    /**
     * Заполняет недостающие подходы по цели нулевыми повторениями, доводя их
     * количество до целевого — используется, когда пользователь завершает
     * работу по цели раньше, не выполнив все запланированные подходы.
     * Идемпотентен: если подходов уже достаточно, ничего не делает.
     *
     * @param userId    идентификатор пользователя
     * @param sessionId идентификатор тренировки
     * @param goalId    идентификатор цели
     * @throws IllegalArgumentException если {@code userId}/{@code sessionId}/{@code goalId} равны
     *                                  {@code null} либо не положительны, либо тренировка/цель не
     *                                  найдены или не принадлежат пользователю
     * @throws IllegalStateException    если тренировка уже завершена
     */
    @Transactional
    public void completeGoalInSession(Long userId, Long sessionId, Long goalId) {
        WorkoutSession session = sessionService.getOwnedByUserAndId(userId, sessionId);
        if (session.getFinishedAt() != null) {
            throw new IllegalStateException("Workout session is already finished");
        }
        Goal goal = goalService.getOwnedByUserAndId(userId, goalId);

        int alreadyLogged = repository.findMaxSetNumber(sessionId, goalId);
        for (int setNumber = alreadyLogged + 1; setNumber <= goal.getTargetSets(); setNumber++) {
            WorkoutSet set = new WorkoutSet();
            set.setSession(session);
            set.setGoal(goal);
            set.setSetNumber(setNumber);
            set.setRepsDone(0);
            set.setWeightKg(goal.getWeightKg());
            repository.save(set);
        }
    }

    /**
     * Возвращает все подходы тренировки, по порядку номера.
     *
     * @param userId    идентификатор пользователя-владельца тренировки
     * @param sessionId идентификатор тренировки
     * @return список подходов тренировки
     * @throws IllegalArgumentException если {@code userId} или {@code sessionId} равен {@code null}
     *                                  либо не положителен, либо тренировка не найдена или не
     *                                  принадлежит пользователю
     */
    @Transactional(readOnly = true)
    public List<WorkoutSet> getSetsForSession(Long userId, Long sessionId) {
        sessionService.getOwnedByUserAndId(userId, sessionId);
        return repository.findBySession_IdOrderBySetNumber(sessionId);
    }
}
