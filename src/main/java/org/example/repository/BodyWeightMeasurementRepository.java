package org.example.repository;

import org.example.entity.BodyWeightMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с замерами веса тела ({@link BodyWeightMeasurement}).
 */
public interface BodyWeightMeasurementRepository extends JpaRepository<BodyWeightMeasurement, Long> {

    /**
     * Возвращает всю историю замеров веса пользователя, от последнего к первому.
     *
     * @param userId идентификатор пользователя
     * @return список замеров пользователя
     */
    List<BodyWeightMeasurement> findByUser_IdOrderByCreatedAtDesc(Long userId);

    /**
     * Возвращает самый последний замер веса пользователя, если он есть.
     *
     * @param userId идентификатор пользователя
     * @return последний замер, либо {@link Optional#empty()}, если замеров ещё нет
     */
    Optional<BodyWeightMeasurement> findFirstByUser_IdOrderByCreatedAtDesc(Long userId);
}
