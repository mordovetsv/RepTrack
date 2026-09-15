package org.example.repository;

import org.example.entity.BodyWeightMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с замерами веса тела ({@link BodyWeightMeasurement}).
 */
public interface BodyWeightMeasurementRepository extends JpaRepository<BodyWeightMeasurement, Long> {
}
