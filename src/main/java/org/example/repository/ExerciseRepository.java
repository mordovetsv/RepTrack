package org.example.repository;

import org.example.entity.Exercise;
import org.example.entity.enums.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с упражнениями ({@link Exercise}).
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    /**
     * Находит все упражнения, доступные пользователю: глобальные
     * (не кастомные) плюс собственные кастомные упражнения этого пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список доступных упражнений
     */
    @Query("SELECT e FROM Exercise e WHERE e.custom = false OR e.user.id = :userId")
    List<Exercise> findAvailableForUser(@Param("userId") Long userId);

    /**
     * Находит кастомное упражнение по идентификатору при условии, что оно
     * принадлежит указанному пользователю. Используется для проверки права
     * владения перед изменением/удалением упражнения.
     *
     * @param userId идентификатор пользователя-владельца
     * @param exerciseId идентификатор упражнения
     * @return найденное упражнение, либо {@link Optional#empty()}
     */
    Optional<Exercise> findByUser_IdAndId(Long userId, Long exerciseId);

    /**
     * Находит упражнение по идентификатору при условии, что оно доступно
     * указанному пользователю для выбора: глобальное, либо кастомное,
     * принадлежащее этому пользователю.
     *
     * @param exerciseId идентификатор упражнения
     * @param userId идентификатор пользователя
     * @return найденное упражнение, либо {@link Optional#empty()}
     */
    @Query("SELECT e FROM Exercise e WHERE e.id = :exerciseId AND (e.custom = false OR e.user.id = :userId)")
    Optional<Exercise> findSelectableByIdForUser(@Param("exerciseId") Long exerciseId, @Param("userId") Long userId);

    /**
     * Находит упражнения, доступные пользователю (глобальные плюс собственные
     * кастомные), которые задействуют указанную группу мышц.
     *
     * @param userId идентификатор пользователя
     * @param muscleGroup группа мышц, по которой фильтруем
     * @return список подходящих упражнений
     */
    @Query("SELECT e FROM Exercise e WHERE (e.custom = false OR e.user.id = :userId) AND :muscleGroup MEMBER OF e.muscleGroups")
    List<Exercise> findAvailableForUserByMuscleGroup(@Param("userId") Long userId, @Param("muscleGroup") MuscleGroup muscleGroup);
}
