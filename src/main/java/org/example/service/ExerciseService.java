package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Exercise;
import org.example.entity.User;
import org.example.entity.enums.MuscleGroup;
import org.example.repository.ExerciseRepository;
import org.example.validation.IdValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository repository;
    private final IdValidator idValidator;

    /**
     * Возвращает упражнения, доступные пользователю: глобальные упражнения
     * плюс собственные кастомные упражнения этого пользователя.
     *
     * @param userId идентификатор пользователя
     * @return доступные упражнения
     * @throws IllegalArgumentException если {@code userId} равен {@code null}
     */
    @Transactional(readOnly = true)
    public Collection<Exercise> getAvailableForUser(Long userId) {
        idValidator.requirePositive(userId);
        return repository.findAvailableForUser(userId);
    }

    /**
     * Находит кастомное упражнение по идентификатору, принадлежащее
     * указанному пользователю. Используется для проверки права владения
     * перед изменением или удалением упражнения — глобальные упражнения
     * и чужие кастомные упражнения этим методом не найдутся.
     *
     * @param userId     идентификатор пользователя-владельца
     * @param exerciseId идентификатор упражнения
     * @return найденное упражнение
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null},
     *                                  либо упражнение не найдено или не принадлежит пользователю
     */
    @Transactional(readOnly = true)
    public Exercise getOwnedByUserAndId(Long userId, Long exerciseId) {
        idValidator.requirePositive(userId);
        idValidator.requirePositive(exerciseId);
        Optional<Exercise> exercise = repository.findByUser_IdAndId(userId, exerciseId);
        if (exercise.isEmpty()) {
            throw new IllegalArgumentException("Exercise not found");
        }
        return exercise.get();
    }

    /**
     * Находит упражнение по идентификатору при условии, что пользователь
     * может его выбрать: глобальное упражнение, либо кастомное, принадлежащее
     * этому пользователю. Используется для проверки перед созданием сущности,
     * ссылающейся на упражнение (например, {@link org.example.entity.Goal}).
     *
     * @param userId     идентификатор пользователя
     * @param exerciseId идентификатор упражнения
     * @return найденное упражнение
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null},
     *                                  либо упражнение не найдено или недоступно пользователю
     */
    @Transactional(readOnly = true)
    public Exercise getSelectableForUser(Long userId, Long exerciseId) {
        idValidator.requirePositive(userId);
        idValidator.requirePositive(exerciseId);
        Optional<Exercise> exercise = repository.findSelectableByIdForUser(exerciseId, userId);
        if (exercise.isEmpty()) {
            throw new IllegalArgumentException("Exercise not found or not available for user");
        }
        return exercise.get();
    }

    /**
     * Возвращает упражнения, доступные пользователю (глобальные плюс собственные
     * кастомные), которые задействуют указанную группу мышц. Используется,
     * например, для отбора упражнений по конкретной группе мышц.
     *
     * @param userId      идентификатор пользователя
     * @param muscleGroup группа мышц, по которой фильтруем
     * @return доступные упражнения на эту группу мышц
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен,
     *                                  или {@code muscleGroup} равен {@code null}
     */
    @Transactional(readOnly = true)
    public Collection<Exercise> getAvailableForUserByMuscleGroup(Long userId, MuscleGroup muscleGroup) {
        idValidator.requirePositive(userId);
        if (muscleGroup == null) {
            throw new IllegalArgumentException("Muscle group cannot be null");
        }
        return repository.findAvailableForUserByMuscleGroup(userId, muscleGroup);
    }

    /**
     * Добавляет новое кастомное упражнение для указанного пользователя.
     * Владелец берётся из {@code userId}, а не из {@code exercise} — вызывающий
     * код (контроллер) отвечает за то, что {@code userId} принадлежит
     * легитимному текущему пользователю.
     *
     * @param userId   идентификатор пользователя-владельца
     * @param exercise новое упражнение (без выставленного {@code id})
     * @throws IllegalArgumentException если {@code userId} равен {@code null},
     *                                  либо у {@code exercise} уже выставлен {@code id}
     */
    @Transactional
    public void addExercise(Long userId, Exercise exercise) {
        idValidator.requirePositive(userId);
        if (exercise.getId() != null) {
            throw new IllegalArgumentException("Exercise ID must be null for a new exercise");
        }
        User owner = new User();
        owner.setId(userId);
        exercise.setUser(owner);
        exercise.setCustom(true);
        repository.save(exercise);
    }

    /**
     * Обновляет название кастомного упражнения, принадлежащего пользователю.
     *
     * @param userId     идентификатор пользователя-владельца
     * @param exerciseId идентификатор упражнения
     * @param newName    новое название упражнения
     * @return обновлённое упражнение
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null}
     *                                  либо не положителен, если {@code newName} равен {@code null}
     *                                  или пуст, либо упражнение не найдено или не принадлежит пользователю
     */
    @Transactional
    public Exercise updateName(Long userId, Long exerciseId, String newName) {

        idValidator.requirePositive(userId);
        idValidator.requirePositive(exerciseId);
        if (newName == null) {
            throw new IllegalArgumentException("New name cannot be null");
        }
        if (newName.isBlank()) {
            throw new IllegalArgumentException("New name cannot be blank");
        }
        Exercise exercise = getOwnedByUserAndId(userId, exerciseId);
        exercise.setName(newName);
        repository.save(exercise);
        return exercise;
    }

    /**
     * Обновляет набор групп мышц кастомного упражнения, принадлежащего пользователю.
     *
     * @param userId          идентификатор пользователя-владельца
     * @param exerciseId      идентификатор упражнения
     * @param newMuscleGroups новый набор групп мышц (не может быть пустым)
     * @return обновлённое упражнение
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null}
     *                                  либо не положителен, если {@code newMuscleGroups} равен
     *                                  {@code null} или пуст, либо упражнение не найдено или не
     *                                  принадлежит пользователю
     */
    @Transactional
    public Exercise updateMuscleGroups(Long userId, Long exerciseId, Set<MuscleGroup> newMuscleGroups) {

        idValidator.requirePositive(userId);
        idValidator.requirePositive(exerciseId);
        if (newMuscleGroups == null) {
            throw new IllegalArgumentException("Muscle groups cannot be null");
        }
        if (newMuscleGroups.isEmpty()) {
            throw new IllegalArgumentException("Muscle groups cannot be empty");
        }
        Exercise exercise = getOwnedByUserAndId(userId, exerciseId);
        exercise.setMuscleGroups(newMuscleGroups);
        repository.save(exercise);
        return exercise;
    }

    /**
     * Обновляет описание кастомного упражнения, принадлежащего пользователю.
     * {@code newDescription} может быть {@code null} — описание необязательно.
     *
     * @param userId         идентификатор пользователя-владельца
     * @param exerciseId     идентификатор упражнения
     * @param newDescription новое описание, либо {@code null}
     * @return обновлённое упражнение
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null}
     *                                  либо не положителен, либо упражнение не найдено или не
     *                                  принадлежит пользователю
     */
    @Transactional
    public Exercise updateDescription(Long userId, Long exerciseId, String newDescription) {

        idValidator.requirePositive(userId);
        idValidator.requirePositive(exerciseId);
        Exercise exercise = getOwnedByUserAndId(userId, exerciseId);
        exercise.setDescription(newDescription);
        repository.save(exercise);
        return exercise;
    }


    /**
     * Удаляет кастомное упражнение, принадлежащее пользователю.
     *
     * @param userId     идентификатор пользователя-владельца
     * @param exerciseId идентификатор упражнения
     * @throws IllegalArgumentException если {@code userId} или {@code exerciseId} равен {@code null}
     *                                  либо не положителен, либо упражнение не найдено или не
     *                                  принадлежит пользователю
     * @throws IllegalStateException    если на упражнение есть ссылки (например, {@link org.example.entity.Goal}),
     *                                  из-за которых его нельзя удалить
     */
    @Transactional
    public void deleteExercise(Long userId, Long exerciseId) {
        idValidator.requirePositive(userId);
        idValidator.requirePositive(exerciseId);
        getOwnedByUserAndId(userId, exerciseId);
        try {
            repository.deleteById(exerciseId);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Exercise is in use and cannot be deleted", e);
        }
    }

}
