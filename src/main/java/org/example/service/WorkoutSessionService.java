package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.entity.WorkoutSession;
import org.example.entity.enums.DayOfWeek;
import org.example.repository.WorkoutSessionRepository;
import org.example.validation.IdValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {
    private final WorkoutSessionRepository repository;
    private final IdValidator idValidator;

    /**
     * Начинает новую тренировку пользователя на указанный день сплита.
     * У пользователя не может быть больше одной незавершённой тренировки
     * одновременно.
     *
     * @param userId    идентификатор пользователя
     * @param dayOfWeek плановый день недельного сплита
     * @param name      опциональное название тренировки
     * @return созданная тренировка
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен,
     *                                  или {@code dayOfWeek} равен {@code null}
     * @throws IllegalStateException    если у пользователя уже есть незавершённая тренировка
     */
    @Transactional
    public WorkoutSession startSession(Long userId, DayOfWeek dayOfWeek, String name) {
        idValidator.requirePositive(userId);
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        if (repository.findByUser_IdAndFinishedAtIsNull(userId).isPresent()) {
            throw new IllegalStateException("User already has an unfinished workout session");
        }

        User owner = new User();
        owner.setId(userId);

        WorkoutSession session = new WorkoutSession();
        session.setUser(owner);
        session.setDayOfWeek(dayOfWeek);
        session.setName(name);

        return repository.save(session);
    }

    /**
     * Завершает тренировку пользователя, проставляя {@code finishedAt} текущим моментом.
     *
     * @param userId    идентификатор пользователя-владельца
     * @param sessionId идентификатор тренировки
     * @return обновлённая тренировка
     * @throws IllegalArgumentException если {@code userId} или {@code sessionId} равен {@code null}
     *                                  либо не положителен, либо тренировка не найдена или не
     *                                  принадлежит пользователю
     * @throws IllegalStateException    если тренировка уже завершена
     */
    @Transactional
    public WorkoutSession finishSession(Long userId, Long sessionId) {
        WorkoutSession session = getOwnedByUserAndId(userId, sessionId);
        if (session.getFinishedAt() != null) {
            throw new IllegalStateException("Workout session is already finished");
        }
        session.setFinishedAt(OffsetDateTime.now());
        return repository.save(session);
    }

    /**
     * Находит тренировку по идентификатору при условии, что она принадлежит
     * указанному пользователю. Используется для проверки права владения.
     *
     * @param userId    идентификатор пользователя-владельца
     * @param sessionId идентификатор тренировки
     * @return найденная тренировка
     * @throws IllegalArgumentException если {@code userId} или {@code sessionId} равен {@code null}
     *                                  либо не положителен, либо тренировка не найдена или не
     *                                  принадлежит пользователю
     */
    @Transactional(readOnly = true)
    public WorkoutSession getOwnedByUserAndId(Long userId, Long sessionId) {
        idValidator.requirePositive(userId);
        idValidator.requirePositive(sessionId);
        Optional<WorkoutSession> session = repository.findByUser_IdAndId(userId, sessionId);
        if (session.isEmpty()) {
            throw new IllegalArgumentException("Workout session not found");
        }
        return session.get();
    }

    /**
     * Возвращает все тренировки пользователя, от последней начатой к первой.
     *
     * @param userId идентификатор пользователя
     * @return список тренировок пользователя
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен
     */
    @Transactional(readOnly = true)
    public List<WorkoutSession> getSessionsForUser(Long userId) {
        idValidator.requirePositive(userId);
        return repository.findByUser_IdOrderByStartedAtDesc(userId);
    }
}
