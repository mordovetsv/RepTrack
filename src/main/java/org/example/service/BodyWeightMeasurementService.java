package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.BodyWeightMeasurement;
import org.example.entity.User;
import org.example.repository.BodyWeightMeasurementRepository;
import org.example.validation.IdValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BodyWeightMeasurementService {
    private final BodyWeightMeasurementRepository repository;
    private final IdValidator idValidator;

    /**
     * Логирует новый замер веса тела пользователя. Момент замера проставляет
     * база данных — несколько замеров в день разрешены.
     *
     * @param userId   идентификатор пользователя
     * @param weightKg вес тела в килограммах
     * @return созданный замер
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен,
     *                                  или {@code weightKg} равен {@code null} либо не положителен
     */
    @Transactional
    public BodyWeightMeasurement logWeight(Long userId, BigDecimal weightKg) {
        idValidator.requirePositive(userId);
        if (weightKg == null || weightKg.signum() <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }

        User owner = new User();
        owner.setId(userId);

        BodyWeightMeasurement measurement = new BodyWeightMeasurement();
        measurement.setUser(owner);
        measurement.setWeightKg(weightKg);

        return repository.save(measurement);
    }

    /**
     * Возвращает всю историю замеров веса пользователя, от последнего к первому.
     *
     * @param userId идентификатор пользователя
     * @return список замеров пользователя
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен
     */
    @Transactional(readOnly = true)
    public List<BodyWeightMeasurement> getHistoryForUser(Long userId) {
        idValidator.requirePositive(userId);
        return repository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    /**
     * Возвращает самый последний ("текущий") замер веса пользователя.
     *
     * @param userId идентификатор пользователя
     * @return последний замер, либо {@link Optional#empty()}, если замеров ещё нет
     * @throws IllegalArgumentException если {@code userId} равен {@code null} либо не положителен
     */
    @Transactional(readOnly = true)
    public Optional<BodyWeightMeasurement> getLatestForUser(Long userId) {
        idValidator.requirePositive(userId);
        return repository.findFirstByUser_IdOrderByCreatedAtDesc(userId);
    }
}
